package org.lazywizard.lazylib.campaign;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.campaign.StarSystemAPI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.lazywizard.lazylib.CollectionUtils;
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
        return fleet1.getFaction().getRelationship(fleet2.getFaction().getId()) > 0;
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
        return fleet1.getFaction().getRelationship(fleet2.getFaction().getId()) < 0;
    }

    // TODO: JavaDoc this!
    // TODO: Add to changelog
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
        StarSystemAPI system = (StarSystemAPI) token.getContainingLocation();
        SectorEntityToken closest = null;
        float distanceSquared, closestDistanceSquared = Float.MAX_VALUE;

        for (SectorEntityToken station : system.getOrbitalStations())
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
    public static CampaignFleetAPI getNearestEnemyFleet(CampaignFleetAPI fleet)
    {
        StarSystemAPI system = (StarSystemAPI) fleet.getContainingLocation();
        CampaignFleetAPI closest = null;
        float distanceSquared, closestDistanceSquared = Float.MAX_VALUE;

        for (CampaignFleetAPI tmp : system.getFleets())
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
    public static CampaignFleetAPI getNearestAlliedFleet(CampaignFleetAPI fleet)
    {
        StarSystemAPI system = (StarSystemAPI) fleet.getContainingLocation();
        CampaignFleetAPI closest = null;
        float distanceSquared, closestDistanceSquared = Float.MAX_VALUE;

        for (CampaignFleetAPI tmp : system.getFleets())
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
    public static CampaignFleetAPI getNearestFleet(SectorEntityToken token)
    {
        StarSystemAPI system = (StarSystemAPI) token.getContainingLocation();
        CampaignFleetAPI closest = null;
        float distanceSquared, closestDistanceSquared = Float.MAX_VALUE;

        for (CampaignFleetAPI tmp : system.getFleets())
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
     * @param fleet          The {@link CampaignFleetAPI} to search around.
     * @param sortByDistance Whether to sort the results by distance from
     *                       {@code fleet}.
     * <p>
     * @return All enemies of {@code fleet} in the system.
     * <p>
     * @since 1.2
     */
    public static List<CampaignFleetAPI> getEnemyFleetsInSystem(CampaignFleetAPI fleet,
            boolean sortByDistance)
    {
        StarSystemAPI system = (StarSystemAPI) fleet.getContainingLocation();
        List<CampaignFleetAPI> enemies = new ArrayList<CampaignFleetAPI>();

        for (CampaignFleetAPI tmp : system.getFleets())
        {
            if (tmp.isAlive() && areEnemies(fleet, tmp))
            {
                enemies.add(tmp);
            }
        }

        if (sortByDistance)
        {
            Collections.sort(enemies,
                    new CollectionUtils.SortTokensByDistance(fleet.getLocation()));
        }

        return enemies;
    }

    /**
     * Find all enemies of a {@link CampaignFleetAPI} present in the system.
     *
     * @param fleet The {@link CampaignFleetAPI} to search around.
     * <p>
     * @return All enemies of {@code fleet} in the system.
     * <p>
     * @see
     * FleetUtils#getEnemyFleetsInSystem(com.fs.starfarer.api.campaign.CampaignFleetAPI,
     * boolean)
     * @since 1.2
     */
    public static List<CampaignFleetAPI> getEnemyFleetsInSystem(CampaignFleetAPI fleet)
    {
        return getEnemyFleetsInSystem(fleet, false);
    }

    /**
     * Finds all enemy fleets within a certain range around a
     * {@link CampaignFleetAPI}.
     *
     * @param fleet          The entity to search around.
     * @param range          How far around {@code fleet} to search.
     * @param sortByDistance Whether to sort the results by distance from
     *                       {@code fleet}.
     * <p>
     * @return A {@link List} containing all enemy fleets within range.
     * <p>
     * @since 1.2
     */
    public static List<CampaignFleetAPI> getNearbyEnemyFleets(CampaignFleetAPI fleet,
            float range, boolean sortByDistance)
    {
        List<CampaignFleetAPI> enemies = new ArrayList<CampaignFleetAPI>();
        range *= range;

        for (CampaignFleetAPI enemy : getEnemyFleetsInSystem(fleet))
        {
            if (MathUtils.getDistanceSquared(fleet, enemy) <= range)
            {
                enemies.add(enemy);
            }
        }

        if (sortByDistance)
        {
            Collections.sort(enemies,
                    new CollectionUtils.SortTokensByDistance(fleet.getLocation()));
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
     * @see
     * FleetUtils#getNearbyEnemyFleets(com.fs.starfarer.api.campaign.CampaignFleetAPI,
     * float, boolean)
     * @since 1.2
     */
    public static List<CampaignFleetAPI> getNearbyEnemyFleets(CampaignFleetAPI fleet, float range)
    {
        return getNearbyEnemyFleets(fleet, range, false);
    }

    /**
     * Find all allies of a {@link CampaignFleetAPI} present in the system.
     *
     * @param fleet          The {@link CampaignFleetAPI} to search around.
     * @param sortByDistance Whether to sort the results by distance from
     *                       {@code fleet}.
     * <p>
     * @return All allies of {@code fleet} in the system.
     * <p>
     * @since 1.2
     */
    public static List<CampaignFleetAPI> getAlliedFleetsInSystem(CampaignFleetAPI fleet,
            boolean sortByDistance)
    {
        StarSystemAPI system = (StarSystemAPI) fleet.getContainingLocation();
        List<CampaignFleetAPI> allies = new ArrayList<CampaignFleetAPI>();

        for (CampaignFleetAPI tmp : system.getFleets())
        {
            if (tmp != fleet && tmp.isAlive() && areAllies(fleet, tmp))
            {
                allies.add(tmp);
            }
        }

        if (sortByDistance)
        {
            Collections.sort(allies,
                    new CollectionUtils.SortTokensByDistance(fleet.getLocation()));
        }

        return allies;
    }

    /**
     * Find all allies of a {@link CampaignFleetAPI} present in the system.
     *
     * @param fleet The {@link CampaignFleetAPI} to search around.
     * <p>
     * @return All allies of {@code fleet} in the system.
     * <p>
     * @see
     * FleetUtils#getAlliedFleetsInSystem(com.fs.starfarer.api.campaign.CampaignFleetAPI,
     * boolean)
     * @since 1.2
     */
    public static List<CampaignFleetAPI> getAlliedFleetsInSystem(CampaignFleetAPI fleet)
    {
        return getAlliedFleetsInSystem(fleet, false);
    }

    /**
     * Finds all allied fleets within a certain range around a
     * {@link CampaignFleetAPI}.
     *
     * @param fleet          The entity to search around.
     * @param range          How far around {@code fleet} to search.
     * @param sortByDistance Whether to sort the results by distance from
     *                       {@code fleet}.
     * <p>
     * @return A {@link List} containing all allied fleets within range.
     * <p>
     * @since 1.2
     */
    public static List<CampaignFleetAPI> getNearbyAlliedFleets(CampaignFleetAPI fleet,
            float range, boolean sortByDistance)
    {
        List<CampaignFleetAPI> allies = new ArrayList<CampaignFleetAPI>();
        range *= range;

        for (CampaignFleetAPI ally : getAlliedFleetsInSystem(fleet))
        {
            if (MathUtils.getDistanceSquared(fleet, ally) <= range)
            {
                allies.add(ally);
            }
        }

        if (sortByDistance)
        {
            Collections.sort(allies,
                    new CollectionUtils.SortTokensByDistance(fleet.getLocation()));
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
     * @see
     * FleetUtils#getNearbyAlliedFleets(com.fs.starfarer.api.campaign.CampaignFleetAPI,
     * float, boolean)
     * @since 1.2
     */
    public static List<CampaignFleetAPI> getNearbyAlliedFleets(CampaignFleetAPI fleet, float range)
    {
        return getNearbyAlliedFleets(fleet, range, false);
    }

    private FleetUtils()
    {
    }
}
