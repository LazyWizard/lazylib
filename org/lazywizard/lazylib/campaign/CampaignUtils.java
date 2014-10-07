package org.lazywizard.lazylib.campaign;

import java.util.ArrayList;
import java.util.List;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.campaign.CargoAPI;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import org.lazywizard.lazylib.MathUtils;

/**
 * Contains methods for working with campaign objects and in-system tasks.
 *
 * @author LazyWizard
 * @since 2.0
 */
// TODO: Replace enemy/allied with RepLevel once .65a lands
// TODO: TEST THIS!
public class CampaignUtils
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
        return (fleet == Global.getSector().getPlayerFleet());
    }

    /**
     * Checks if two campaign objects are allies.
     * <p>
     * @param token1 The first {@link SectorEntityToken} to check.
     * @param fleet2 The second {@link SectorEntityToken} to check.
     * <p>
     * @return {@code true} if the tokens are allies, {@code false} otherwise.
     * <p>
     * @since 1.2
     */
    public static boolean areAllies(SectorEntityToken token1, SectorEntityToken fleet2)
    {
        return (token1.getFaction().getRelationship(
                fleet2.getFaction().getId()) > 0);
    }

    /**
     * Checks if two campaign objects are enemies.
     * <p>
     * @param token1 The first {@link SectorEntityToken} to check.
     * @param token2 The second {@link SectorEntityToken} to check.
     * <p>
     * @return {@code true} if the tokens are enemies, {@code false} otherwise.
     * <p>
     * @since 1.2
     */
    public static boolean areEnemies(SectorEntityToken token1, SectorEntityToken token2)
    {
        return (token1.getFaction().getRelationship(
                token2.getFaction().getId()) < 0);
    }

    /**
     * Returns the faction relationship between two campaign objects. Equivalent
     * to calling
     * token1.getFaction().getRelationship(token2.getFaction().getId()),
     * but much more readable.
     *
     * @param token1 A {@link SectorEntityToken} whose faction relationship will
     *               be tested.
     * @param token2 The {@link SectorEntityToken} to test relationship with.
     * <p>
     * @return The faction relationship between {@code token1} and
     *         {@code token2}.
     * <p>
     * @since 1.7
     */
    public static float getRelation(SectorEntityToken token1, SectorEntityToken token2)
    {
        return token1.getFaction().getRelationship(token2.getFaction().getId());
    }

    /**
     * Checks if two campaign objects are neutral towards each other.
     * <p>
     * @param token1 The first {@link SectorEntityToken} to check.
     * @param token2 The second {@link SectorEntityToken} to check.
     * <p>
     * @return {@code true} if the tokens are neutral, {@code false} otherwise.
     * <p>
     * @since 1.2
     */
    public static boolean areNeutral(SectorEntityToken token1, SectorEntityToken token2)
    {
        return token1.getFaction().getRelationship(token2.getFaction().getId()) == 0;
    }

    /**
     * Checks if two campaign objects are owned by the same faction.
     *
     * @param token1 The first {@link SectorEntityToken} to check.
     * @param token2 The second {@link SectorEntityToken} to check.
     * <p>
     * @return {@code true} if both tokens share a faction, {@code false}
     *         otherwise.
     * <p>
     * @since 2.0
     */
    public static boolean areSameFaction(SectorEntityToken token1, SectorEntityToken token2)
    {
        return (token1.getFaction() == token2.getFaction());
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
     * Checks if a cargo contains a specific mothballed ship.
     * <p>
     * @param fleetMemberId The fleet member ID of the ship to check. This can
     *                      be retrieved with {@link ShipAPI#getFleetMemberId()}
     *                      or {@link FleetMemberAPI#getId()}.
     * @param cargo         The cargo to check for the presence of this ship in.
     * <p>
     * @return {@code true} if {@code cargo} contains a ship with this ID,
     *         {@code false} otherwise.
     * <p>
     * @since 2.0
     */
    public static boolean isShipInMothballed(String fleetMemberId, CargoAPI cargo)
    {
        if (fleetMemberId == null || fleetMemberId.isEmpty() || cargo == null)
        {
            return false;
        }

        for (FleetMemberAPI tmp : cargo.getMothballedShips().getMembersListCopy())
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
     * Find the closest enemy fleet to a {@link SectorEntityToken}.
     *
     * @param token The {@link SectorEntityToken} to search around.
     * <p>
     * @return The enemy {@link CampaignFleetAPI} closest to {@code token}.
     * <p>
     * @since 1.2
     */
    public static CampaignFleetAPI getNearestEnemyFleet(SectorEntityToken token)
    {
        CampaignFleetAPI closest = null;
        float distance, closestDistance = Float.MAX_VALUE;

        for (CampaignFleetAPI tmp : token.getContainingLocation().getFleets())
        {
            if (!tmp.isAlive() || !areEnemies(token, tmp))
            {
                continue;
            }

            distance = MathUtils.getDistance(tmp, token.getLocation());
            if (distance < closestDistance)
            {
                closest = tmp;
                closestDistance = distance;
            }
        }

        return closest;
    }

    /**
     * Find the closest allied fleet to a {@link SectorEntityToken}.
     *
     * @param token The {@link SectorEntityToken} to search around.
     * <p>
     * @return The allied {@link CampaignFleetAPI} closest to {@code fleet}.
     * <p>
     * @since 1.2
     */
    public static CampaignFleetAPI getNearestAlliedFleet(SectorEntityToken token)
    {
        CampaignFleetAPI closest = null;
        float distance, closestDistance = Float.MAX_VALUE;

        for (CampaignFleetAPI tmp : token.getContainingLocation().getFleets())
        {
            if (tmp == token || !tmp.isAlive() || !areAllies(token, tmp))
            {
                continue;
            }

            distance = MathUtils.getDistance(tmp, token.getLocation());
            if (distance < closestDistance)
            {
                closest = tmp;
                closestDistance = distance;
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
    public static CampaignFleetAPI getNearestFleet(SectorEntityToken token)
    {
        CampaignFleetAPI closest = null;
        float distance, closestDistance = Float.MAX_VALUE;

        for (CampaignFleetAPI tmp : token.getContainingLocation().getFleets())
        {
            if (tmp == token || !tmp.isAlive())
            {
                continue;
            }

            distance = MathUtils.getDistance(tmp, token.getLocation());
            if (distance < closestDistance)
            {
                closest = tmp;
                closestDistance = distance;
            }
        }

        return closest;
    }

    /**
     * Find all enemies of a {@link SectorEntityToken} present in the system.
     *
     * @param token The {@link SectorEntityToken} to find enemies of.
     * <p>
     * @return All enemies of {@code token} in the system.
     * <p>
     * @since 1.2
     */
    public static List<CampaignFleetAPI> getEnemyFleetsInSystem(SectorEntityToken token)
    {
        List<CampaignFleetAPI> enemies = new ArrayList<>();

        for (CampaignFleetAPI tmp : token.getContainingLocation().getFleets())
        {
            if (tmp.isAlive() && areEnemies(token, tmp))
            {
                enemies.add(tmp);
            }
        }

        return enemies;
    }

    /**
     * Finds all enemy fleets within a certain range around a
     * {@link SectorEntityToken}.
     *
     * @param token The entity to search around.
     * @param range How far around {@code token} to search.
     * <p>
     * @return A {@link List} containing all enemy fleets within range.
     * <p>
     * @since 1.2
     */
    public static List<CampaignFleetAPI> getNearbyEnemyFleets(SectorEntityToken token, float range)
    {
        List<CampaignFleetAPI> enemies = new ArrayList<>();

        for (CampaignFleetAPI enemy : getEnemyFleetsInSystem(token))
        {
            if (MathUtils.isWithinRange(token, enemy, range))
            {
                enemies.add(enemy);
            }
        }

        return enemies;
    }

    /**
     * Find all allied fleets of a {@link SectorEntityToken} present in the
     * system.
     *
     * @param token The {@link CampaignFleetAPI} to search around.
     * <p>
     * @return All allied fleets of {@code token} in the system.
     * <p>
     * @since 1.2
     */
    public static List<CampaignFleetAPI> getAlliedFleetsInSystem(SectorEntityToken token)
    {
        List<CampaignFleetAPI> allies = new ArrayList<>();

        for (CampaignFleetAPI tmp : token.getContainingLocation().getFleets())
        {
            if (tmp != token && tmp.isAlive() && areAllies(token, tmp))
            {
                allies.add(tmp);
            }
        }

        return allies;
    }

    /**
     * Finds all allied fleets within a certain range around a
     * {@link SectorEntityToken}.
     *
     * @param token The entity to search around.
     * @param range How far around {@code token} to search.
     * <p>
     * @return A {@link List} containing all allied fleets within range.
     * <p>
     * @since 1.2
     */
    public static List<CampaignFleetAPI> getNearbyAlliedFleets(SectorEntityToken token, float range)
    {
        List<CampaignFleetAPI> allies = new ArrayList<>();

        for (CampaignFleetAPI ally : getAlliedFleetsInSystem(token))
        {
            if (MathUtils.isWithinRange(token, ally, range))
            {
                allies.add(ally);
            }
        }

        return allies;
    }

    /**
     * Finds all fleets within a certain range around a
     * {@link SectorEntityToken}.
     *
     * @param token The entity to search around.
     * @param range How far around {@code token} to search.
     * <p>
     * @return A {@link List} containing all fleets within range.
     * <p>
     * @since 1.7
     */
    public static List<CampaignFleetAPI> getNearbyFleets(SectorEntityToken token, float range)
    {
        List<CampaignFleetAPI> fleets = new ArrayList<>();

        for (CampaignFleetAPI tmp : token.getContainingLocation().getFleets())
        {
            if (tmp.isAlive() && MathUtils.isWithinRange(token, tmp, range))
            {
                fleets.add(tmp);
            }
        }

        return fleets;
    }

    CampaignUtils()
    {
    }
}
