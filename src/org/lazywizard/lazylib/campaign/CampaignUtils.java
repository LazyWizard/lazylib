package org.lazywizard.lazylib.campaign;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.campaign.FactionAPI;
import com.fs.starfarer.api.campaign.LocationAPI;
import com.fs.starfarer.api.campaign.RepLevel;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.fleet.FleetMemberType;
import com.fs.starfarer.api.impl.campaign.ids.Tags;
import org.jetbrains.annotations.Nullable;
import org.lazywizard.lazylib.MathUtils;

/**
 * Contains methods for working with campaign objects. Unless noted otherwise,
 * all methods in this class only deal with the {@link LocationAPI} the
 * specified token is located in.
 *
 * @author LazyWizard
 * @since 2.0
 */
// TODO: TEST THIS!
@SuppressWarnings("unchecked")
public class CampaignUtils
{
    /**
     * Controls what {@link RepLevel}s will be accepted by methods that check
     * reputation relative to the {@link RepLevel} passed in. For example,
     * {@link RepLevel#FAVORABLE} is {@link IncludeRep#AT_OR_HIGHER}
     * {@link RepLevel#NEUTRAL}.
     * <p>
     * @since 2.0
     */
    public enum IncludeRep
    {
        /**
         * Equivalent to {@code <}.
         */
        LOWER,
        /**
         * Equivalent to {@code <=}.
         */
        AT_OR_LOWER,
        /**
         * Equivalent to {@code ==}.
         */
        AT,
        /**
         * Equivalent to {@code >=}.
         */
        AT_OR_HIGHER,
        /**
         * Equivalent to {@code >}.
         */
        HIGHER
    }

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
     * Returns the faction relationship between two campaign objects. Equivalent
     * to calling token1.getFaction().getRelationshipLevel(token2.getFaction()),
     * but much more readable.
     *
     * @param token1 A {@link SectorEntityToken} whose faction reputation will
     *               be tested.
     * @param token2 The {@link SectorEntityToken} to test reputation with.
     * <p>
     * @return The faction reputation between {@code token1} and {@code token2}.
     * <p>
     * @since 2.0
     */
    public static RepLevel getReputation(SectorEntityToken token1, SectorEntityToken token2)
    {
        return token1.getFaction().getRelationshipLevel(token2.getFaction());
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
     * Checks if two campaign objects have a specific reputation with each
     * other.
     *
     * @param token1  The first {@link SectorEntityToken} to check.
     * @param token2  The second {@link SectorEntityToken} to check.
     * @param include What relative range of reputations to include.
     * @param rep     The base reputation level {@code token1} and {token2} must
     *                be at with each other for this to return {@code true},
     *                modified by {@code include}.
     * <p>
     * @return {@code true} if both tokens share a faction, {@code false}
     *         otherwise.
     * <p>
     * @since 2.0
     */
    // TODO: Test this!
    public static boolean areAtRep(SectorEntityToken token1, SectorEntityToken token2,
            IncludeRep include, RepLevel rep)
    {
        final RepLevel actualRep = getReputation(token1, token2);
        switch (include)
        {
            case LOWER:
                return actualRep.ordinal() < rep.ordinal();
            case AT_OR_LOWER:
                return actualRep.ordinal() <= rep.ordinal();
            case AT:
                return actualRep.ordinal() == rep.ordinal();
            case AT_OR_HIGHER:
                return actualRep.ordinal() >= rep.ordinal();
            case HIGHER:
                return actualRep.ordinal() > rep.ordinal();
            default:
                throw new RuntimeException("Unsupported IncludeRep: " + include.name());
        }
    }

    /**
     * Checks if a fleet contains a specific ship in its roster. Does NOT
     * check mothballed ships - call
     * {@link CargoUtils#isShipInMothballed(java.lang.String,
     * com.fs.starfarer.api.campaign.CargoAPI)} for that.
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
        // ShipAPI's getFleetMemberId() can return null for manually created ships
        if (fleetMemberId == null || fleetMemberId.isEmpty())
        {
            return false;
        }

        for (FleetMemberAPI tmp : fleet.getFleetData().getMembersListCopy())
        {
            if (Objects.equals(fleetMemberId, tmp.getId()))
            {
                return true;
            }
        }

        return false;
    }

    /**
     * Adds a fully crewed, fully combat ready ship to the designated fleet.
     *
     * @param wingOrVariantId The wing or variant ID to create.
     * @param type            Whether to add a fighter wing or a ship.
     * @param fleet           The fleet to add this ship to.
     * <p>
     * @return The {@link FleetMemberAPI} of the created ship.
     * <p>
     * @since 2.1
     */
    public static FleetMemberAPI addShipToFleet(String wingOrVariantId,
            FleetMemberType type, CampaignFleetAPI fleet)
    {
        FleetMemberAPI ship = Global.getFactory().createFleetMember(
                type, wingOrVariantId);
        fleet.getFleetData().addFleetMember(ship);

        fleet.getCargo().addCrew((int) ship.getNeededCrew());
        ship.getStatus().repairFully();
        ship.getRepairTracker().setCR(ship.getRepairTracker().getMaxCR());
        ship.setStatUpdateNeeded(true);

        return ship;
    }

    /**
     * Find the closest hostile fleet to a {@link SectorEntityToken}.
     *
     * @param token The {@link SectorEntityToken} to search around.
     * <p>
     * @return The hostile {@link CampaignFleetAPI} closest to {@code token}, or {@code null} if none are found.
     * <p>
     * @since 1.2
     */
    @Nullable
    public static CampaignFleetAPI getNearestHostileFleet(SectorEntityToken token)
    {
        CampaignFleetAPI closest = null;
        float distance, closestDistance = Float.MAX_VALUE;

        for (CampaignFleetAPI tmp : token.getContainingLocation().getFleets())
        {
            if (tmp == token || !tmp.isAlive()
                    || !tmp.getFaction().isHostileTo(token.getFaction()))
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
     * Finds all <i>visible</i> hostile fleets within a certain range around a
     * {@link SectorEntityToken}.
     *
     * @param token The entity to search around.
     * @param range How far around {@code token} to search.
     * <p>
     * @return A {@link List} containing all fleets within range that are
     *         hostile towards {@code token} and can be seen by it.
     * <p>
     * @since 1.2
     */
    public static List<CampaignFleetAPI> getNearbyHostileFleets(
            SectorEntityToken token, float range)
    {
        List<CampaignFleetAPI> enemies = new ArrayList<>();

        for (CampaignFleetAPI tmp : token.getContainingLocation().getFleets())
        {
            if (tmp == token)
            {
                continue;
            }

            if (token instanceof CampaignFleetAPI && !tmp.isVisibleToSensorsOf(token))
            {
                continue;
            }

            if (tmp.isAlive() && tmp.getFaction().isHostileTo(token.getFaction())
                    && MathUtils.isWithinRange(token, tmp, range))
            {
                enemies.add(tmp);
            }
        }

        return enemies;
    }

    /**
     * Find all hostile fleets towards a {@link SectorEntityToken} present in
     * that token's location.
     *
     * @param token The {@link SectorEntityToken} to find enemies of.
     * <p>
     * @return All enemies of {@code token} in the system that are actively
     *         hostile.
     * <p>
     * @since 1.2
     */
    public static List<CampaignFleetAPI> getHostileFleetsInSystem(SectorEntityToken token)
    {
        List<CampaignFleetAPI> enemies = new ArrayList<>();

        for (CampaignFleetAPI tmp : token.getContainingLocation().getFleets())
        {
            if (tmp == token)
            {
                continue;
            }

            if (tmp.isAlive() && tmp.isHostileTo(token))
            {
                enemies.add(tmp);
            }
        }

        return enemies;
    }

    /**
     * Find the closest entity with a specifc tag to a
     * {@link SectorEntityToken}, excluding itself.
     *
     * @param token     The {@link SectorEntityToken} to search around.
     * @param entityTag The tag we should be searching for; for example:
     *                  {@link Tags#STATION} or {@link Tags#JUMP_POINT}.
     * <p>
     * @return The object with tag {@code entityTag} closest to {@code token}, or {@code null} if none are found.
     * <p>
     * @since 2.0
     */
    @Nullable
    public static <T extends SectorEntityToken> T getNearestEntityWithTag(
            SectorEntityToken token, String entityTag)
    {
        T closest = null;
        float distanceSquared, closestDistanceSquared = Float.MAX_VALUE;

        for (SectorEntityToken tmp : token.getContainingLocation().getEntitiesWithTag(entityTag))
        {
            T entity = (T) tmp;

            if (entity == token)
            {
                continue;
            }

            distanceSquared = MathUtils.getDistanceSquared(token.getLocation(),
                    entity.getLocation());
            if (distanceSquared < closestDistanceSquared)
            {
                closest = entity;
                closestDistanceSquared = distanceSquared;
            }
        }

        return closest;
    }

    /**
     * Find entities with a specific tag near a {@link SectorEntityToken},
     * excluding itself.
     *
     * @param token     The {@link SectorEntityToken} to search around.
     * @param range     How far around {@code token} to search.
     * @param entityTag The tag we should be searching for; for example:
     *                  {@link Tags#STATION} or {@link Tags#JUMP_POINT}.
     * <p>
     * @return All objects with tag {@code entityTag} within range of
     *         {@code token}.
     * <p>
     * @since 2.0
     */
    public static <T extends SectorEntityToken> List<T> getNearbyEntitiesWithTag(
            SectorEntityToken token, float range, String entityTag)
    {
        List<T> entities = new ArrayList<>();

        // Find all tokens of the given type within range
        for (SectorEntityToken tmp : token.getContainingLocation().getEntitiesWithTag(entityTag))
        {
            T entity = (T) tmp;

            // Exclude passed in token
            if (entity == token)
            {
                continue;
            }

            // Add any token within range
            if (MathUtils.isWithinRange(token, entity, range))
            {
                entities.add(entity);
            }
        }

        return entities;
    }

    /**
     * Find the closest entity with a specific tag and faction near a
     * {@link SectorEntityToken}, excluding itself.
     *
     * @param token     The {@link SectorEntityToken} to search around.
     * @param entityTag The tag we should be searching for; for example:
     *                  {@link Tags#STATION} or {@link Tags#JUMP_POINT}.
     * @param faction   The faction ownership we are looking for.
     * <p>
     * @return The object with tag {@code entityTag} closest to {@code token}
     *         that is owned by {@code faction}, or {@code null} if none are found.
     * <p>
     * @since 2.0
     */
    @Nullable
    public static <T extends SectorEntityToken> T getNearestEntityFromFaction(
            SectorEntityToken token, String entityTag, FactionAPI faction)
    {
        T closest = null;
        float distanceSquared, closestDistanceSquared = Float.MAX_VALUE;

        for (SectorEntityToken tmp : token.getContainingLocation().getEntitiesWithTag(entityTag))
        {
            T entity = (T) tmp;

            if (entity == token || faction != entity.getFaction())
            {
                continue;
            }

            distanceSquared = MathUtils.getDistanceSquared(token.getLocation(),
                    entity.getLocation());
            if (distanceSquared < closestDistanceSquared)
            {
                closest = entity;
                closestDistanceSquared = distanceSquared;
            }
        }

        return closest;
    }

    /**
     * Find entities with a specific tag and from a specific faction near a
     * {@link SectorEntityToken}, excluding itself.
     *
     * @param token     The {@link SectorEntityToken} to search around.
     * @param range     How far around {@code token} to search.
     * @param entityTag The tag we should be searching for; for example:
     *                  {@link Tags#STATION} or {@link Tags#JUMP_POINT}.
     * @param faction   What faction the entity must be owned by.
     * <p>
     * @return All objects with tag {@code entityTag} and faction
     *         {@code faction} within range of {@code token}.
     * <p>
     * @since 2.0
     */
    public static <T extends SectorEntityToken> List<T> getNearbyEntitiesFromFaction(
            SectorEntityToken token, float range, String entityTag, FactionAPI faction)
    {
        List<T> entities = new ArrayList<>();

        // Find all tokens of the given type within range
        for (SectorEntityToken tmp : token.getContainingLocation().getEntitiesWithTag(entityTag))
        {
            T entity = (T) tmp;

            // Exclude passed in token and tokens of wrong faction
            if (entity == token || faction != entity.getFaction())
            {
                continue;
            }

            // Add any token within range
            if (MathUtils.isWithinRange(token, entity, range))
            {
                entities.add(entity);
            }
        }

        return entities;
    }

    /**
     * Find all entities with a specific tag and faction within a location.
     *
     * @param location  The {@link LocationAPI} to search in.
     * @param entityTag The tag we should be searching for; for example:
     *                  {@link Tags#STATION} or {@link Tags#JUMP_POINT}.
     * @param faction   What faction entities must belong to.
     * <p>
     * @return All objects of faction {@code faction} and with tag
     *         {@code entityTag} within {@code location}.
     * <p>
     * @since 2.0
     */
    public static <T extends SectorEntityToken> List<T> getEntitiesFromFaction(
            LocationAPI location, String entityTag, FactionAPI faction)
    {
        List<T> entities = new ArrayList<>();

        // Find all tokens from the given faction
        for (SectorEntityToken tmp : location.getEntitiesWithTag(entityTag))
        {
            T entity = (T) tmp;

            if (faction == entity.getFaction())
            {
                entities.add(entity);
            }
        }

        return entities;
    }

    /**
     * Find the closest entity with a specific tag and reputation with a
     * {@link SectorEntityToken}, excluding itself.
     *
     * @param token     The {@link SectorEntityToken} to search around.
     * @param entityTag The tag we should be searching for; for example:
     *                  {@link Tags#STATION} or {@link Tags#JUMP_POINT}.
     * @param include   What range of {@link RepLevel}s to accept, relative to
     *                  {@code rep}.
     * @param rep       The base reputation to check against.
     * <p>
     * @return The object with tag {@code entityTag} closest to {@code token}
     *         within the reputation range specified by {@code include} and
     *         {@code rep}, or {@code null} if none are found.
     * <p>
     * @since 2.0
     */
    // TODO: Test this
    @Nullable
    public static <T extends SectorEntityToken> T getNearestEntityWithRep(
            SectorEntityToken token, String entityTag, IncludeRep include, RepLevel rep)
    {
        T closest = null;
        float distanceSquared, closestDistanceSquared = Float.MAX_VALUE;

        for (SectorEntityToken tmp : token.getContainingLocation().getEntitiesWithTag(entityTag))
        {
            T entity = (T) tmp;

            if (tmp == token || areSameFaction(entity, token)
                    || !areAtRep(token, entity, include, rep))
            {
                continue;
            }

            distanceSquared = MathUtils.getDistanceSquared(token.getLocation(),
                    entity.getLocation());
            if (distanceSquared < closestDistanceSquared)
            {
                closest = entity;
                closestDistanceSquared = distanceSquared;
            }
        }

        return closest;
    }

    /**
     * Find nearby entities with a specific tag and reputation with a
     * {@link SectorEntityToken}, excluding itself.
     *
     * @param token     The {@link SectorEntityToken} to search around.
     * @param range     How far around {@code token} to search.
     * @param entityTag The tag we should be searching for; for example:
     *                  {@link Tags#STATION} or {@link Tags#JUMP_POINT}.
     * @param include   What range of {@link RepLevel}s to accept, relative to
     *                  {@code rep}.
     * @param rep       The base reputation to check against.
     * <p>
     * @return All objects with tag {@code entityTag} within range of
     *         {@code token}.
     * <p>
     * @since 2.0
     */
    // TODO: Test this!
    public static <T extends SectorEntityToken> List<T> getNearbyEntitiesWithRep(
            SectorEntityToken token, float range, String entityTag,
            IncludeRep include, RepLevel rep)
    {
        List<T> entities = new ArrayList<>();

        // Optimization: check for parameters that equal "just give me everything"
        if ((include == IncludeRep.AT_OR_HIGHER && rep.ordinal() == 0)
                || (include == IncludeRep.AT_OR_LOWER
                && rep.ordinal() == (RepLevel.values().length - 1)))
        {
            for (SectorEntityToken tmp : token.getContainingLocation().getEntitiesWithTag(entityTag))
            {
                if (tmp == token)
                {
                    continue;
                }

                if (!areSameFaction(tmp, token))
                {
                    entities.add((T) tmp);
                }
            }

            return entities;
        }

        // Find all tokens of the given type within reputation range
        for (SectorEntityToken tmp : token.getContainingLocation().getEntitiesWithTag(entityTag))
        {
            if (tmp == token)
            {
                continue;
            }

            T entity = (T) tmp;

            // Exclude tokens of our faction
            if (areSameFaction(entity, token))
            {
                continue;
            }

            // Add any token whose reputation falls within the given range
            if (areAtRep(token, entity, include, rep)
                    && MathUtils.isWithinRange(token, entity, range))
            {
                entities.add(entity);
            }
        }

        return entities;
    }

    /**
     * Find all entities with a specific tag and reputation with a
     * {@link SectorEntityToken} in that token's location, excluding itself.
     *
     * @param token     The {@link SectorEntityToken} to search around.
     * @param entityTag The tag we should be searching for; for example:
     *                  {@link Tags#STATION} or {@link Tags#JUMP_POINT}.
     * @param include   What range of {@link RepLevel}s to accept, relative to
     *                  {@code rep}.
     * @param rep       The base reputation to check against.
     * <p>
     * @return All objects with tag {@code entityTag} within range of
     *         {@code token}.
     * <p>
     * @since 2.0
     */
    // TODO: Test this!
    public static <T extends SectorEntityToken> List<T> getEntitiesWithRep(
            SectorEntityToken token, String entityTag, IncludeRep include, RepLevel rep)
    {
        // TODO: Properly implement this later if needed
        // Doing it this way is only slightly less efficient, so might not be necessary
        return getNearbyEntitiesWithRep(token, Float.MAX_VALUE, entityTag, include, rep);
    }

    /**
     * Finds all <i>visible</i> fleets within a certain range around a
     * {@link SectorEntityToken}.
     *
     * @param token The entity to search around.
     * @param range How far around {@code token} to search.
     * <p>
     * @return A {@link List} containing all fleets within range that
     *         {@code token} can see, excluding itself if it is a
     *         {@link CampaignFleetAPI}.
     * <p>
     * @since 1.7
     */
    public static List<CampaignFleetAPI> getNearbyFleets(SectorEntityToken token, float range)
    {
        List<CampaignFleetAPI> fleets = new ArrayList<>();

        for (CampaignFleetAPI tmp : token.getContainingLocation().getFleets())
        {
            if (tmp == token)
            {
                continue;
            }

            if (token instanceof CampaignFleetAPI && !tmp.isVisibleToSensorsOf(token))
            {
                continue;
            }

            if (tmp.isAlive() && MathUtils.isWithinRange(token, tmp, range))
            {
                fleets.add(tmp);
            }
        }

        return fleets;
    }

    private CampaignUtils()
    {
    }
}
