package org.lazywizard.lazylib.combat;

import java.util.ArrayList;
import java.util.List;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BattleObjectiveAPI;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.CombatFleetManagerAPI;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.DeployedFleetMemberAPI;
import com.fs.starfarer.api.combat.FogOfWarAPI;
import com.fs.starfarer.api.combat.MissileAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ViewportAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.fleet.FleetMemberType;
import com.fs.starfarer.api.mission.FleetSide;
import org.apache.log4j.Level;
import org.lazywizard.lazylib.LazyLib;
import org.lazywizard.lazylib.MathUtils;
import org.lwjgl.util.vector.Vector2f;

/**
 * Contains methods that deal with the battle map in general. These methods do
 * not respect fog of war, and assume that you are in combat
 * ({@code Global.getCombatEngine() != null}). If you wish to check fog of war
 * visibility you should use the methods in {@link AIUtils}.
 *
 * @author LazyWizard
 * @since 1.0
 */
public class CombatUtils
{
    /**
     * Find a {@link ShipAPI}'s corresponding {@link FleetMemberAPI}. Due to the
     * way the game keeps tracks of ship ownership, this will be extremely slow
     * when used with hulks.
     *
     * @param ship The {@link ShipAPI} whose corresponding
     *             {@link FleetMemberAPI} we are trying to find.
     * <p>
     * @return The {@link FleetMemberAPI} that represents this {@link ShipAPI}
     *         in the campaign, or {@code null} if no match is found.
     * <p>
     * @since 1.5
     */
    public static FleetMemberAPI getFleetMember(ShipAPI ship)
    {
        CombatFleetManagerAPI fm = Global.getCombatEngine()
                .getFleetManager(ship.getOriginalOwner());
        DeployedFleetMemberAPI dfm = fm.getDeployedFleetMemberEvenIfDisabled(ship);

        // Compatibility fix for main menu battles
        if (dfm != null && dfm.getMember() != null)
        {
            return dfm.getMember();
        }

        // Directly spawned ships won't have a fleet member assigned
        String id = ship.getFleetMemberId();
        if (id == null)
        {
            return null;
        }

        // Not found in fleet manager? Check reserves
        for (FleetMemberAPI member : fm.getReservesCopy())
        {
            if (id.equals(member.getId()))
            {
                return member;
            }
        }

        // No match was found
        return null;
    }

    /**
     * Checks if a {@link CombatEntityAPI} is visible to a side of battle.
     * Note1: Allied and neutral entities are always visible.
     * Note2: All {@link AIUtils} methods already filter by visibility, so use
     * of this method is not necessary on their results.
     *
     * @param entity The {@link CombatEntityAPI} to check visibility of.
     * @param side   The side whose fog of war will be tested.
     * <p>
     * @return {@code true} if {@code entity} is visible to {@code side},
     *         {@code false} otherwise.
     * <p>
     * @since 1.7
     */
    public static boolean isVisibleToSide(CombatEntityAPI entity, int side)
    {
        // This is a VERY fast check for neutral/allied status
        // Player+enemy (0+1) is the only combination that will return 1
        // Always visible: neutrals (side 100) and allies (0+0 or 1+1)
        // Warning: this optimization will cause bugs if SS ever
        // adds support for more than two sides in a battle!
        if (side + entity.getOwner() != 1)
        {
            return true;
        }

        // There have been reports of null pointer exceptions in this method
        // If this block is ever tripped, it's a vanilla bug
        FogOfWarAPI fog = Global.getCombatEngine().getFogOfWar(side);
        if (fog == null)
        {
            Global.getLogger(AIUtils.class).log(Level.ERROR,
                    "Fog of war not found for side " + side
                    + ", defaulting to visible!");
            return true;
        }

        return fog.isVisible(entity.getLocation());
    }

    /**
     * Returns all projectiles in range of a given location, excluding missiles.
     *
     * @param location The location to search around.
     * @param range    How far around {@code location} to search.
     * <p>
     * @return A {@link List} of {@link DamagingProjectileAPI}s within range of
     *         {@code location}.
     * <p>
     * @since 1.0
     */
    public static List<DamagingProjectileAPI> getProjectilesWithinRange(Vector2f location, float range)
    {
        List<DamagingProjectileAPI> projectiles = new ArrayList<>();

        for (DamagingProjectileAPI tmp : Global.getCombatEngine().getProjectiles())
        {
            if (tmp instanceof MissileAPI)
            {
                continue;
            }

            if (MathUtils.isWithinRange(tmp.getLocation(), location, range))
            {
                projectiles.add(tmp);
            }
        }

        return projectiles;
    }

    /**
     * Returns all missiles in range of a given location.
     *
     * @param location The location to search around.
     * @param range    How far around {@code location} to search.
     * <p>
     * @return A {@link List} of {@link MissileAPI}s within range of
     *         {@code location}.
     * <p>
     * @since 1.0
     */
    public static List<MissileAPI> getMissilesWithinRange(Vector2f location, float range)
    {
        List<MissileAPI> missiles = new ArrayList<>();

        for (MissileAPI tmp : Global.getCombatEngine().getMissiles())
        {
            if (MathUtils.isWithinRange(tmp.getLocation(), location, range))
            {
                missiles.add(tmp);
            }
        }

        return missiles;
    }

    /**
     * Returns all ships in range of a given location, excluding the shuttle
     * pod.
     *
     * @param location The location to search around.
     * @param range    How far around {@code location} to search.
     * <p>
     * @return A {@link List} of {@link ShipAPI}s within range of
     *         {@code location}.
     * <p>
     * @since 1.0
     */
    public static List<ShipAPI> getShipsWithinRange(Vector2f location, float range)
    {
        List<ShipAPI> ships = new ArrayList<>();

        for (ShipAPI tmp : Global.getCombatEngine().getShips())
        {
            if (tmp.isShuttlePod())
            {
                continue;
            }

            if (MathUtils.isWithinRange(tmp, location, range))
            {
                ships.add(tmp);
            }
        }

        return ships;
    }

    /**
     * Returns all asteroids in range of a given location.
     *
     * @param location The location to search around.
     * @param range    How far around {@code location} to search.
     * <p>
     * @return A {@link List} of asteroids within range of {@code location}.
     * <p>
     * @since 1.0
     */
    public static List<CombatEntityAPI> getAsteroidsWithinRange(Vector2f location, float range)
    {
        List<CombatEntityAPI> asteroids = new ArrayList<>();

        for (CombatEntityAPI tmp : Global.getCombatEngine().getAsteroids())
        {
            if (MathUtils.isWithinRange(tmp, location, range))
            {
                asteroids.add(tmp);
            }
        }

        return asteroids;
    }

    /**
     * Returns all objectives in range of a given location.
     *
     * @param location The location to search around.
     * @param range    How far around {@code location} to search.
     * <p>
     * @return A {@link List} of {@link BattleObjectiveAPI}s within range of
     *         {@code location}.
     * <p>
     * @since 1.0
     */
    public static List<BattleObjectiveAPI> getObjectivesWithinRange(Vector2f location,
            float range)
    {
        List<BattleObjectiveAPI> objectives = new ArrayList<>();

        for (BattleObjectiveAPI tmp : Global.getCombatEngine().getObjectives())
        {
            if (MathUtils.isWithinRange(tmp.getLocation(), location, range))
            {
                objectives.add(tmp);
            }
        }

        return objectives;
    }

    /**
     * Returns all entities in range of a given location.
     *
     * @param location The location to search around.
     * @param range    How far around {@code location} to search.
     * <p>
     * @return A {@link List} of {@link CombatEntityAPI}s within range of
     *         {@code location}.
     * <p>
     * @since 1.0
     */
    public static List<CombatEntityAPI> getEntitiesWithinRange(Vector2f location, float range)
    {
        List<CombatEntityAPI> entities = new ArrayList<>();

        for (CombatEntityAPI tmp : Global.getCombatEngine().getShips())
        {
            if (MathUtils.isWithinRange(tmp, location, range))
            {
                entities.add(tmp);
            }
        }

        // This also includes missiles
        for (CombatEntityAPI tmp : Global.getCombatEngine().getProjectiles())
        {
            if (MathUtils.isWithinRange(tmp, location, range))
            {
                entities.add(tmp);
            }
        }

        for (CombatEntityAPI tmp : Global.getCombatEngine().getAsteroids())
        {
            if (MathUtils.isWithinRange(tmp, location, range))
            {
                entities.add(tmp);
            }
        }

        return entities;
    }

    /**
     * Spawns a ship directly onto the battle map, bypassing the fleet reserves.
     * <p>
     * <b>NOTE: this method will not work out of the box in the campaign (.6.2a)
     * due to an after-battle crash in the default FleetEncounterContextPlugin
     * caused by spawned ships not being registered with the fleet.</b>
     * <p>
     * @param variantId       The ID of the ship variant to spawn.
     * @param type            Whether this is a ship or a fighter wing.
     * @param side            What side of the battle this ship should fight on.
     * @param combatReadiness The CR the ship should start with.
     * @param location        The location on the battle map the ship should
     *                        spawn at.
     * @param facing          The initial facing of the ship on the battle map.
     * <p>
     * @return The {@link ShipAPI} that was spawned by this method.
     * <p>
     * @since 1.9
     */
    public static ShipAPI spawnShipOrWingDirectly(String variantId,
            FleetMemberType type, FleetSide side, float combatReadiness,
            Vector2f location, float facing)
    {
        // Warn the player about the FleetEncounterContext bug in .6.2a
        if (Global.getCombatEngine().isInCampaign()
                && "0.6.2a".equals(LazyLib.getSupportedGameVersion()))
        {
            Global.getLogger(CombatUtils.class).log(Level.WARN,
                    "spawnShipOrWingDirectly may not function correctly in the"
                    + " campaign using the vanilla fleet encounter"
                    + " code! A modified fleet InteractionDialogPlugin"
                    + " using a custom FleetEncounterContextPlugin is"
                    + " required to avoid an after-battle crash.");
        }

        // Create the ship, set its stats and spawn it on the combat map
        FleetMemberAPI member = Global.getFactory().createFleetMember(type, variantId);
        member.getCrewComposition().addRegular(member.getNeededCrew());
        ShipAPI ship = Global.getCombatEngine().getFleetManager(side)
                .spawnFleetMember(member, location, facing, 0f);
        ship.setCRAtDeployment(combatReadiness);
        ship.setCurrentCR(combatReadiness);
        ship.setOwner(side.ordinal());
        ship.getShipAI().forceCircumstanceEvaluation();
        return ship;
    }

    /**
     * Recenters the viewport at a specific point.
     * <p>
     * @param newCenter The new center point of the {@link ViewportAPI}.
     * <p>
     * @since 2.0
     */
    public static void centerViewport(Vector2f newCenter)
    {
        final ViewportAPI view = Global.getCombatEngine().getViewport();
        final float width = view.getVisibleWidth(), height = view.getVisibleHeight();
        view.set(newCenter.x - (width / 2f), newCenter.y - (height / 2f), width, height);
    }

    /**
     * Apply force to an object. Remember Newton's Second Law.
     *
     * Force is multiplied by 100 to avoid requiring ridiculous force amounts.
     *
     * @param entity    The {@link CombatEntityAPI} to apply the force to.
     * @param direction The directional vector of the force (this will
     *                  automatically be normalized).
     * @param force     How much force to apply. Unit is how much it takes
     *                  to modify a 100 weight object's velocity by 1 su/sec.
     * <p>
     * @since 1.2
     */
    public static void applyForce(CombatEntityAPI entity, Vector2f direction, float force)
    {
        // Filter out forces without a direction
        if (direction.lengthSquared() == 0)
        {
            return;
        }

        // Force is far too weak otherwise
        force *= 100f;

        // Avoid divide-by-zero errors...
        float mass = Math.max(1f, entity.getMass());
        // Calculate the velocity change and its resulting vector
        // Don't bother going over Starsector's speed cap
        float velChange = Math.min(1250f, force / mass);
        Vector2f dir = new Vector2f();
        direction.normalise(dir);
        dir.scale(velChange);
        // Apply our velocity change
        Vector2f.add(dir, entity.getVelocity(), entity.getVelocity());
    }

    /**
     * Apply force to an object. Remember Newton's Second Law.
     *
     * Force is multiplied by 100 to avoid requiring ridiculous force amounts.
     *
     * @param entity    The {@link CombatEntityAPI} to apply the force to.
     * @param direction The angle the force will be applied towards.
     * @param force     How much force to apply. Unit is how much it takes
     *                  to modify a 100 weight object's velocity by 1 su/sec.
     * <p>
     * @since 1.2
     */
    public static void applyForce(CombatEntityAPI entity, float direction, float force)
    {
        applyForce(entity, MathUtils.getPointOnCircumference(new Vector2f(0, 0),
                1f, direction), force);
    }

    private CombatUtils()
    {
    }
}
