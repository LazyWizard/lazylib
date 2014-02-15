package org.lazywizard.lazylib.campaign;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.lazywizard.lazylib.CollectionUtils;
import org.lazywizard.lazylib.CollectionUtils.SortTokensByDistance;
import org.lazywizard.lazylib.LazyLib;
import org.lazywizard.lazylib.MathUtils;

/**
 * Contains methods for working with fleets and fleet data.
 *
 * @author LazyWizard
 * @since 1.0
 */
public class FleetUtils
{
    /**
     * Checks if a fleet is the player fleet.
     *
     * @param fleet The {@link CampaignFleetAPI} to be checked.
     * <p>
     * @return {@code true} if {@code fleet} is the player fleet, {@code false}
     *         otherwise.
     * <p>
     * @since 1.0
     */
    public static boolean isPlayer(CampaignFleetAPI fleet)
    {
        if (fleet == null)
        {
            return false;
        }

        return fleet == Global.getSector().getPlayerFleet();
    }

    /**
     * Checks if two fleets are allies.
     * <p>
     * @param fleet1 The first {@link CampaignFleetAPI} to check.
     * @param fleet2 The second {@link CampaignFleetAPI} to check.
     * <p>
     * @return {@code true} if the fleets are allies, {@code false} otherwise.
     * <p>
     * @since 1.2
     */
    public static boolean areAllies(CampaignFleetAPI fleet1, CampaignFleetAPI fleet2)
    {
        return (fleet1.getFaction().getRelationship(
                fleet2.getFaction().getId()) > 0);
    }

    /**
     * Checks if two fleets are enemies.
     * <p>
     * @param fleet1 The first {@link CampaignFleetAPI} to check.
     * @param fleet2 The second {@link CampaignFleetAPI} to check.
     * <p>
     * @return {@code true} if the fleets are enemies, {@code false} otherwise.
     * <p>
     * @since 1.2
     */
    public static boolean areEnemies(CampaignFleetAPI fleet1, CampaignFleetAPI fleet2)
    {
        return (fleet1.getFaction().getRelationship(
                fleet2.getFaction().getId()) < 0);
    }

    /**
     * Returns the faction relationship between two fleets. Equivalent to
     * calling fleet1.getFaction().getRelationship(fleet2.getFaction().getId()),
     * but much more readable.
     *
     * @param fleet1 A {@link CampaignFleetAPI} whose faction relationship will
     *               be tested.
     * @param fleet2 The {@link CampaignFleetAPI} to test relationship with.
     * <p>
     * @return The faction relationship between {@code fleet1} and
     *         {@code fleet2}.
     * <p>
     * @since 1.7
     */
    public static float getRelation(CampaignFleetAPI fleet1, CampaignFleetAPI fleet2)
    {
        return fleet1.getFaction().getRelationship(fleet2.getFaction().getId());
    }

    /**
     * Checks if two fleets are neutral towards each other.
     * <p>
     * @param fleet1 The first {@link CampaignFleetAPI} to check.
     * @param fleet2 The second {@link CampaignFleetAPI} to check.
     * <p>
     * @return {@code true} if the fleets are neutral, {@code false} otherwise.
     * <p>
     * @since 1.2
     */
    public static boolean areNeutral(CampaignFleetAPI fleet1, CampaignFleetAPI fleet2)
    {
        return fleet1.getFaction().getRelationship(fleet2.getFaction().getId()) == 0;
    }

    /**
     * Checks if a fleet contains a specific ship.
     * <p>
     * @param fleetMemberId The fleet member ID of the ship to check. This can
     *                      be retrieved with {@link ShipAPI#getFleetMemberId()}
     *                      or {@link FleetMemberAPI#getId()}.
     * @param fleet         The fleet to check for the presence of this ship in.
     * <p>
     * @return {@code true} if {@code fleet} contains a ship with this ID,
     *         {@code false} otherwise.
     * <p>
     * @since 1.8
     */
    public static boolean isShipInFleet(String fleetMemberId, CampaignFleetAPI fleet)
    {
        if (fleetMemberId == null || fleetMemberId.isEmpty())
        {
            return false;
        }

        for (FleetMemberAPI tmp : fleet.getFleetData().getMembersListCopy())
        {
            if (fleetMemberId.equals(tmp.getId()))
            {
                return true;
            }
        }

        return false;
    }

    /**
     * Find the closest orbital station to a {@link SectorEntityToken}.
     *
     * @param token The {@link SectorEntityToken} to search around.
     * <p>
     * @return The orbital station closest to {@code token}.
     * <p>
     * @since 1.2
     */
    // TODO: rewrite to include radius, if possible to do so efficiently
    public static SectorEntityToken getNearestStation(SectorEntityToken token)
    {
        SectorEntityToken closest = null;
        float distanceSquared, closestDistanceSquared = Float.MAX_VALUE;

        for (SectorEntityToken station : token.getContainingLocation().getOrbitalStations())
        {
            if (station == token)
            {
                continue;
            }

            distanceSquared = MathUtils.getDistanceSquared(station.getLocation(),
                    token.getLocation());

            if (distanceSquared < closestDistanceSquared)
            {
                closest = station;
                closestDistanceSquared = distanceSquared;
            }
        }

        return closest;
    }

    /**
     * Find the closest enemy fleet to a {@link CampaignFleetAPI}.
     *
     * @param fleet The {@link CampaignFleetAPI} to search around.
     * <p>
     * @return The enemy {@link CampaignFleetAPI} closest to {@code fleet}.
     * <p>
     * @since 1.2
     */
    // TODO: rewrite to include radius, if possible to do so efficiently
    public static CampaignFleetAPI getNearestEnemyFleet(CampaignFleetAPI fleet)
    {
        CampaignFleetAPI closest = null;
        float distanceSquared, closestDistanceSquared = Float.MAX_VALUE;

        for (CampaignFleetAPI tmp : fleet.getContainingLocation().getFleets())
        {
            if (!tmp.isAlive() || !areEnemies(fleet, tmp))
            {
                continue;
            }

            distanceSquared = MathUtils.getDistanceSquared(tmp.getLocation(),
                    fleet.getLocation());

            if (distanceSquared < closestDistanceSquared)
            {
                closest = tmp;
                closestDistanceSquared = distanceSquared;
            }
        }

        return closest;
    }

    /**
     * Find the closest allied fleet to a {@link CampaignFleetAPI}.
     *
     * @param fleet The {@link CampaignFleetAPI} to search around.
     * <p>
     * @return The allied {@link CampaignFleetAPI} closest to {@code fleet}.
     * <p>
     * @since 1.2
     */
    // TODO: rewrite to include radius, if possible to do so efficiently
    public static CampaignFleetAPI getNearestAlliedFleet(CampaignFleetAPI fleet)
    {
        CampaignFleetAPI closest = null;
        float distanceSquared, closestDistanceSquared = Float.MAX_VALUE;

        for (CampaignFleetAPI tmp : fleet.getContainingLocation().getFleets())
        {
            if (tmp == fleet || !tmp.isAlive() || !areAllies(fleet, tmp))
            {
                continue;
            }

            distanceSquared = MathUtils.getDistanceSquared(tmp.getLocation(),
                    fleet.getLocation());

            if (distanceSquared < closestDistanceSquared)
            {
                closest = tmp;
                closestDistanceSquared = distanceSquared;
            }
        }

        return closest;
    }

    /**
     * Find the closest {@link CampaignFleetAPI} near a
     * {@link SectorEntityToken}.
     *
     * @param token The {@link SectorEntityToken} to search around.
     * <p>
     * @return The {@link CampaignFleetAPI} closest to {@code token}.
     * <p>
     * @since 1.2
     */
    // TODO: rewrite to include radius, if possible to do so efficiently
    public static CampaignFleetAPI getNearestFleet(SectorEntityToken token)
    {
        CampaignFleetAPI closest = null;
        float distanceSquared, closestDistanceSquared = Float.MAX_VALUE;

        for (CampaignFleetAPI tmp : token.getContainingLocation().getFleets())
        {
            if (tmp == token || !tmp.isAlive())
            {
                continue;
            }

            distanceSquared = MathUtils.getDistanceSquared(tmp.getLocation(),
                    token.getLocation());

            if (distanceSquared < closestDistanceSquared)
            {
                closest = tmp;
                closestDistanceSquared = distanceSquared;
            }
        }

        return closest;
    }

    /**
     * Find all enemies of a {@link CampaignFleetAPI} present in the system.
     *
     * @param fleet The {@link CampaignFleetAPI} to search around.
     * <p>
     * @return All enemies of {@code fleet} in the system.
     * <p>
     * @since 1.2
     */
    public static List<CampaignFleetAPI> getEnemyFleetsInSystem(CampaignFleetAPI fleet)
    {
        List<CampaignFleetAPI> enemies = new ArrayList<>();

        for (CampaignFleetAPI tmp : fleet.getContainingLocation().getFleets())
        {
            if (tmp.isAlive() && areEnemies(fleet, tmp))
            {
                enemies.add(tmp);
            }
        }

        return enemies;
    }

    /**
     * Finds all enemy fleets within a certain range around a
     * {@link CampaignFleetAPI}.
     *
     * @param fleet The entity to search around.
     * @param range How far around {@code fleet} to search.
     * <p>
     * @return A {@link List} containing all enemy fleets within range.
     * <p>
     * @since 1.2
     */
    public static List<CampaignFleetAPI> getNearbyEnemyFleets(CampaignFleetAPI fleet, float range)
    {
        List<CampaignFleetAPI> enemies = new ArrayList<>();

        for (CampaignFleetAPI enemy : getEnemyFleetsInSystem(fleet))
        {
            if (MathUtils.isWithinRange(fleet, enemy, range))
            {
                enemies.add(enemy);
            }
        }

        return enemies;
    }

    /**
     * Find all allies of a {@link CampaignFleetAPI} present in the system.
     *
     * @param fleet The {@link CampaignFleetAPI} to search around.
     * <p>
     * @return All allies of {@code fleet} in the system.
     * <p>
     * @since 1.2
     */
    public static List<CampaignFleetAPI> getAlliedFleetsInSystem(CampaignFleetAPI fleet)
    {
        List<CampaignFleetAPI> allies = new ArrayList<>();

        for (CampaignFleetAPI tmp : fleet.getContainingLocation().getFleets())
        {
            if (tmp != fleet && tmp.isAlive() && areAllies(fleet, tmp))
            {
                allies.add(tmp);
            }
        }

        return allies;
    }

    /**
     * Finds all allied fleets within a certain range around a
     * {@link CampaignFleetAPI}.
     *
     * @param fleet The entity to search around.
     * @param range How far around {@code fleet} to search.
     * <p>
     * @return A {@link List} containing all allied fleets within range.
     * <p>
     * @since 1.2
     */
    public static List<CampaignFleetAPI> getNearbyAlliedFleets(CampaignFleetAPI fleet, float range)
    {
        List<CampaignFleetAPI> allies = new ArrayList<>();

        for (CampaignFleetAPI ally : getAlliedFleetsInSystem(fleet))
        {
            if (MathUtils.isWithinRange(fleet, ally, range))
            {
                allies.add(ally);
            }
        }

        return allies;
    }

    /**
     * Finds all fleets within a certain range around a
     * {@link CampaignFleetAPI}.
     *
     * @param fleet The entity to search around.
     * @param range How far around {@code fleet} to search.
     * <p>
     * @return A {@link List} containing all fleets within range.
     * <p>
     * @since 1.7
     */
    public static List<CampaignFleetAPI> getNearbyFleets(CampaignFleetAPI fleet, float range)
    {
        List<CampaignFleetAPI> fleets = new ArrayList<>();

        for (CampaignFleetAPI tmp : fleet.getContainingLocation().getFleets())
        {
            if (MathUtils.isWithinRange(fleet, tmp, range))
            {
                fleets.add(tmp);
            }
        }

        return fleets;
    }

    /**
     * @deprecated Use the normal version of this method and call
     * {@link Collections#sort(List, Comparator)} using a
     * {@link SortTokensByDistance} as the {@link Comparator}.
     * @since 1.2
     */
    @Deprecated
    public static List<CampaignFleetAPI> getEnemyFleetsInSystem(CampaignFleetAPI fleet,
            boolean sortByDistance)
    {
        LazyLib.onDeprecatedMethodUsage();

        List<CampaignFleetAPI> enemies = getEnemyFleetsInSystem(fleet);

        if (sortByDistance)
        {
            Collections.sort(enemies,
                    new CollectionUtils.SortTokensByDistance(fleet.getLocation()));
        }

        return enemies;
    }

    /**
     * @deprecated Use the normal version of this method and call
     * {@link Collections#sort(List, Comparator)} using a
     * {@link SortTokensByDistance} as the {@link Comparator}.
     * @since 1.2
     */
    @Deprecated
    public static List<CampaignFleetAPI> getNearbyEnemyFleets(CampaignFleetAPI fleet,
            float range, boolean sortByDistance)
    {
        LazyLib.onDeprecatedMethodUsage();

        List<CampaignFleetAPI> enemies = getNearbyEnemyFleets(fleet, range);

        if (sortByDistance)
        {
            Collections.sort(enemies,
                    new CollectionUtils.SortTokensByDistance(fleet.getLocation()));
        }

        return enemies;
    }

    /**
     * @deprecated Use the normal version of this method and call
     * {@link Collections#sort(List, Comparator)} using a
     * {@link SortTokensByDistance} as the {@link Comparator}.
     * @since 1.2
     */
    @Deprecated
    public static List<CampaignFleetAPI> getAlliedFleetsInSystem(CampaignFleetAPI fleet,
            boolean sortByDistance)
    {
        LazyLib.onDeprecatedMethodUsage();

        List<CampaignFleetAPI> allies = getAlliedFleetsInSystem(fleet);

        if (sortByDistance)
        {
            Collections.sort(allies,
                    new CollectionUtils.SortTokensByDistance(fleet.getLocation()));
        }

        return allies;
    }

    /**
     * @deprecated Use the normal version of this method and call
     * {@link Collections#sort(List, Comparator)} using a
     * {@link SortTokensByDistance} as the {@link Comparator}.
     * @since 1.2
     */
    @Deprecated
    public static List<CampaignFleetAPI> getNearbyAlliedFleets(CampaignFleetAPI fleet,
            float range, boolean sortByDistance)
    {
        LazyLib.onDeprecatedMethodUsage();

        List<CampaignFleetAPI> allies = getNearbyAlliedFleets(fleet, range);

        if (sortByDistance)
        {
            Collections.sort(allies,
                    new CollectionUtils.SortTokensByDistance(fleet.getLocation()));
        }

        return allies;
    }

    private FleetUtils()
    {
    }
}
