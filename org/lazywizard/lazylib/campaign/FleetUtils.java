package org.lazywizard.lazylib.campaign;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.campaign.StarSystemAPI;
import java.util.ArrayList;
import java.util.List;
import org.lazywizard.lazylib.MathUtils;

/**
 * Contains methods for working with fleets and fleet data.
 * @author LazyWizard
 */
public class FleetUtils
{
    /**
     * Checks if a fleet is the player fleet.
     *
     * @param fleet The {@link CampaignFleetAPI} to be checked.
     * @return {@code true} if {@code fleet} is the player fleet, {@code false} otherwise.
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
     * Find the closest orbital station to a {@link SectorEntityToken}.
     *
     * @param token The {@link SectorEntityToken} to search around.
     * @return The orbital station closest to {@code token}.
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
     * @return The enemy {@link CampaignFleetAPI} closest to {@code fleet}.
     */
    public static CampaignFleetAPI getNearestEnemyFleet(CampaignFleetAPI fleet)
    {
        StarSystemAPI system = (StarSystemAPI) fleet.getContainingLocation();
        CampaignFleetAPI closest = null;
        float distanceSquared, closestDistanceSquared = Float.MAX_VALUE;

        for (CampaignFleetAPI tmp : system.getFleets())
        {
            if (tmp.getFaction() == fleet.getFaction())
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
     * @return The allied {@link CampaignFleetAPI} closest to {@code fleet}.
     */
    public static CampaignFleetAPI getNearestAlliedFleet(CampaignFleetAPI fleet)
    {
        StarSystemAPI system = (StarSystemAPI) fleet.getContainingLocation();
        CampaignFleetAPI closest = null;
        float distanceSquared, closestDistanceSquared = Float.MAX_VALUE;

        for (CampaignFleetAPI tmp : system.getFleets())
        {
            if (tmp == fleet || tmp.getFaction() != fleet.getFaction())
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
     * Find the closest {@link CampaignFleetAPI} near a {@link SectorEntityToken}.
     *
     * @param token The {@link SectorEntityToken} to search around.
     * @return The {@link CampaignFleetAPI} closest to {@code token}.
     */
    public static CampaignFleetAPI getNearestFleet(SectorEntityToken token)
    {
        StarSystemAPI system = (StarSystemAPI) token.getContainingLocation();
        CampaignFleetAPI closest = null;
        float distanceSquared, closestDistanceSquared = Float.MAX_VALUE;

        for (CampaignFleetAPI tmp : system.getFleets())
        {
            if (tmp == token)
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
     * Find all present enemies of an entity.
     *
     * @param entity The {@link CombatEntityAPI} to search around.
     * @param sortByDistance Whether to sort the results by distance from {@code entity}.
     * @return All enemies of {@code entity} on the battle map.
     */
    public static List<CampaignFleetAPI> getEnemyFleetsInSystem(CombatEntityAPI entity,
            boolean sortByDistance)
    {
        StarSystemAPI system = (StarSystemAPI) fleet.getContainingLocation();
        List<CampaignFleetAPI> enemies = new ArrayList();

        for (CampaignFleetAPI tmp : CombatUtils.getCombatEngine().getShips())
        {
            if (tmp.getFaction() != entity.getFaction() && !tmp.isHulk() && !tmp.isShuttlePod())
            {
                enemies.add(tmp);
            }
        }

        if (sortByDistance)
        {
            Collections.sort(enemies,
                    new CollectionUtils.SortEntitiesByDistance(entity.getLocation()));
        }

        return enemies;
    }

    /**
     * Find all present enemies of an entity.
     *
     * @param entity The {@link CombatEntityAPI} to search around.
     * @return All enemies of {@code entity} on the battle map.
     * @see AIUtils#getEnemiesOnMap(com.fs.starfarer.api.combat.CombatEntityAPI, boolean)
     */
    public static List<CampaignFleetAPI> getEnemyFleetsInSystem(CombatEntityAPI entity)
    {
        return getEnemyFleetsInSystem(entity, false);
    }

    /**
     * Finds all enemies within a certain range around an entity.
     *
     * @param entity The entity to search around.
     * @param range How far around {@code entity} to search.
     * @param sortByDistance Whether to sort the results by distance from {@code entity}.
     * @return A {@link List} containing all enemy ships within range.
     */
    public static List<CampaignFleetAPI> getNearbyEnemyFleets(CombatEntityAPI entity,
            float range, boolean sortByDistance)
    {
        List<CampaignFleetAPI> enemies = new ArrayList();
        range *= range;

        for (CampaignFleetAPI enemy : getEnemyFleetsInSystem(entity))
        {
            if (MathUtils.getDistanceSquared(entity, enemy) <= range)
            {
                enemies.add(enemy);
            }
        }

        if (sortByDistance)
        {
            Collections.sort(enemies,
                    new CollectionUtils.SortEntitiesByDistance(entity.getLocation()));
        }

        return enemies;
    }

    /**
     * Finds all enemies within a certain range around an entity.
     *
     * @param entity The entity to search around.
     * @param range How far around {@code entity} to search.
     * @return A {@link List} containing all enemy ships within range.
     * @see AIUtils#getNearbyEnemies(com.fs.starfarer.api.combat.CombatEntityAPI, float, boolean)
     */
    public static List<CampaignFleetAPI> getNearbyEnemyFleets(CombatEntityAPI entity, float range)
    {
        return getNearbyEnemyFleets(entity, range, false);
    }

    /**
     * Find all present allies of an entity.
     *
     * @param entity The {@link CombatEntityAPI} to search around.
     * @param sortByDistance Whether to sort the results by distance from {@code entity}.
     * @return All allies of {@code entity} on the battle map.
     */
    public static List<CampaignFleetAPI> getAlliedFleetsInSystem(CombatEntityAPI entity,
            boolean sortByDistance)
    {
        StarSystemAPI system = (StarSystemAPI) fleet.getContainingLocation();
        List<CampaignFleetAPI> allies = new ArrayList();

        for (CampaignFleetAPI tmp : CombatUtils.getCombatEngine().getShips())
        {
            if (tmp != entity && tmp.getFaction() == entity.getFaction()
                    && !tmp.isHulk() && !tmp.isShuttlePod())
            {
                allies.add(tmp);
            }
        }

        if (sortByDistance)
        {
            Collections.sort(allies,
                    new CollectionUtils.SortEntitiesByDistance(entity.getLocation()));
        }

        return allies;
    }

    /**
     * Find all present allies of an entity.
     *
     * @param entity The {@link CombatEntityAPI} to search around.
     * @return All allies of {@code entity} on the battle map.
     * @see AIUtils#getAlliesOnMap(com.fs.starfarer.api.combat.CombatEntityAPI, boolean)
     */
    public static List<CampaignFleetAPI> getAlliedFleetsInSystem(CombatEntityAPI entity)
    {
        return getAlliedFleetsInSystem(entity, false);
    }

    /**
     * Finds all allies within a certain range around an entity.
     *
     * @param entity The entity to search around.
     * @param range How far around {@code entity} to search.
     * @param sortByDistance Whether to sort the results by distance from {@code entity}.
     * @return A {@link List} containing all allied ships within range.
     */
    public static List<CampaignFleetAPI> getNearbyAlliedFleets(CombatEntityAPI entity,
            float range, boolean sortByDistance)
    {
        List<CampaignFleetAPI> allies = new ArrayList();
        range *= range;

        for (CampaignFleetAPI ally : getAlliedFleetsInSystem(entity))
        {
            if (MathUtils.getDistanceSquared(entity, ally) <= range)
            {
                allies.add(ally);
            }
        }

        if (sortByDistance)
        {
            Collections.sort(allies,
                    new CollectionUtils.SortEntitiesByDistance(entity.getLocation()));
        }

        return allies;
    }

    /**
     * Finds all allies within a certain range around an entity.
     *
     * @param entity The entity to search around.
     * @param range How far around {@code entity} to search.
     * @return A {@link List} containing all allied ships within range.
     * @see AIUtils#getNearbyAllies(com.fs.starfarer.api.combat.CombatEntityAPI, float, boolean)
     */
    public static List<CampaignFleetAPI> getNearbyAlliedFleets(CombatEntityAPI entity, float range)
    {
        return getNearbyAlliedFleets(entity, range, false);
    }

    private FleetUtils()
    {
    }
}
