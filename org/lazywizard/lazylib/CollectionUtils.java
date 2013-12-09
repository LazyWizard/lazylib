package org.lazywizard.lazylib;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.combat.BattleObjectiveAPI;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.util.WeightedRandomPicker;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Level;
import org.lwjgl.util.vector.Vector2f;

/**
 * Contains methods for working with Collections.
 *
 * @author LazyWizard
 * @since 1.0
 */
public class CollectionUtils
{
    /**
     * Combines and separates a {@link Collection} of {@link String}s. Useful
     * for comma-separated lists.
     *
     * @param toImplode A {@link Collection} whose contents should be combined
     *                  into one {@link String}.
     * @param separator The separator character to split {@code toImplode} with.
     * <p>
     * @return A single {@link String} consisting of {@code toImplode}'s values
     *         separated with {@code separator}.
     * <p>
     * @since 1.0
     */
    public static String implode(Collection toImplode, String separator)
    {
        if (toImplode.isEmpty())
        {
            return "";
        }

        StringBuilder ret = new StringBuilder(toImplode.size() * 16);
        for (Iterator iter = toImplode.iterator(); iter.hasNext();)
        {
            ret.append(iter.next().toString());
            if (iter.hasNext())
            {
                ret.append(separator);
            }
        }

        return ret.toString();
    }

    /**
     * Creates a comma-separated {@link String} from a {@link Collection}'s
     * entries.
     *
     * @param toImplode A {@link Collection} to be combined (using each
     *                  entry's {@code toString} method).
     * <p>
     * @return A single {@link String} consisting of {@code toImplode}'s values
     *         separated with commas.
     * <p>
     * @see CollectionUtils#implode(java.util.Collection, java.lang.String)
     * @since 1.0
     */
    public static String implode(Collection toImplode)
    {
        return implode(toImplode, ", ");
    }

    /**
     * Filters a {@link Collection} and returns a {@link List} containing only
     * the entries that the filters accepted.
     *
     * @param toFilter The {@link Collection} to filter.
     * @param filters  A {@link List} of {@link CollectionFilter}s that will be
     *                 used to filter {@code toFilter}.
     * <p>
     * @return A {@link List} containing only the entries of {@code toFilter}
     *         that passed {@code filters}' {@code accept()} method.
     * <p>
     * @see CollectionUtils#filter(java.util.Collection,
     * org.lazywizard.lazylib.CollectionUtils.CollectionFilter)
     * @since 1.7
     */
    public static <T> List<T> filter(Collection<T> toFilter, List<CollectionFilter<T>> filters)
    {
        List<T> filtered = new ArrayList<T>(toFilter.size());
        for (T tmp : toFilter)
        {
            for (CollectionFilter<T> filter : filters)
            {
                if (!filter.accept(tmp))
                {
                    continue;
                }

                filtered.add(tmp);
            }
        }

        return filtered;
    }

    /**
     * Filters a {@link Collection} and returns a {@link List} containing only
     * the entries that the filter accepted.
     *
     * @param toFilter The {@link Collection} to filter.
     * @param filter   A {@link CollectionFilter} that will be used to filter
     *                 {@code toFilter}.
     * <p>
     * @return A {@link List} containing only the entries of {@code toFilter}
     *         that passed {@code filter}'s {@code accept()} method.
     * <p>
     * @since 1.7
     */
    public static <T> List<T> filter(Collection<T> toFilter, CollectionFilter<T> filter)
    {
        List<T> filtered = new ArrayList<T>(toFilter.size());
        for (T tmp : toFilter)
        {
            if (filter.accept(tmp))
            {
                filtered.add(tmp);
            }
        }

        return filtered;
    }

    /**
     * Used with
     * {@link CollectionUtils#filter(java.util.Collection,
     * org.lazywizard.lazylib.CollectionUtils.CollectionFilter)} to filter out
     * unwanted entries in a {@link Collection}.
     * <p>
     * @param <T> The type of object to be filtered.
     */
    public static interface CollectionFilter<T>
    {
        /**
         * Returns if an object should be allowed past this filter.
         *
         * @param t The object to be checked.
         * <p>
         * @return {@code true} if this object should be kept in the
         *         {@link Collection}.
         */
        public boolean accept(T t);
    }

    /**
     * A {@link Comparator} that sorts {@link CombatEntityAPI}s by distance from
     * the {@link Vector2f} passed into the constructor.
     * <p>
     * @since 1.1
     */
    public static class SortEntitiesByDistance implements Comparator<CombatEntityAPI>
    {
        private Vector2f location;
        private boolean includeRadius = true;

        private SortEntitiesByDistance()
        {
        }

        /**
         * @param location      The central location to judge distance from.
         * @param includeRadius Whether to include collision radius in the
         *                      check.
         * <p>
         * @since 1.6
         */
        public SortEntitiesByDistance(Vector2f location, boolean includeRadius)
        {
            this.location = location;
            this.includeRadius = includeRadius;
        }

        /**
         * @param location The central location to judge distance from.
         * <p>
         * @since 1.1
         */
        public SortEntitiesByDistance(Vector2f location)
        {
            this.location = location;
        }

        /**
         * Compares the distances of two {@link CombatEntityAPI}s from a central
         * location.
         *
         * @param o1 The first {@link CombatEntityAPI}.
         * @param o2 The second {@link CombatEntityAPI}.
         * <p>
         * @return A comparison of the distances of {@code o1} and {@code o2}
         *         from {@code location}.
         * <p>
         * @since 1.1
         */
        @Override
        public int compare(CombatEntityAPI o1, CombatEntityAPI o2)
        {
            if (includeRadius)
            {
                return Float.compare(MathUtils.getDistance(o1, location),
                        MathUtils.getDistance(o2, location));
            }

            return Float.compare(MathUtils.getDistanceSquared(o1.getLocation(),
                    location), MathUtils.getDistanceSquared(o2.getLocation(), location));
        }
    }

    /**
     * A {@link Comparator} that sorts {@link SectorEntityToken}s by distance
     * the {@link Vector2f} passed into the constructor.
     * <p>
     * @since 1.1
     */
    public static class SortTokensByDistance implements Comparator<SectorEntityToken>
    {
        private Vector2f location;
        private boolean includeRadius = true;

        private SortTokensByDistance()
        {
        }

        /**
         * @param location      The central location to judge distance from.
         * @param includeRadius Whether to include collision radius in the
         *                      check.
         * <p>
         * @since 1.6
         */
        public SortTokensByDistance(Vector2f location, boolean includeRadius)
        {
            this.location = location;
            this.includeRadius = includeRadius;
        }

        /**
         * @param location The central location to judge distance from.
         * <p>
         * @since 1.1
         */
        public SortTokensByDistance(Vector2f location)
        {
            this.location = location;
        }

        /**
         * Compares the distances of two {@link SectorEntityToken}s from a
         * central location.
         *
         * @param o1 The first {@link SectorEntityToken}.
         * @param o2 The second {@link SectorEntityToken}.
         * <p>
         * @return A comparison of the distances of {@code o1} and {@code o2}
         *         from {@code location}.
         * <p>
         * @since 1.1
         */
        @Override
        public int compare(SectorEntityToken o1, SectorEntityToken o2)
        {
            if (includeRadius)
            {
                return Float.compare(MathUtils.getDistanceSquared(o1, location),
                        MathUtils.getDistanceSquared(o2, location));
            }

            return Float.compare(MathUtils.getDistanceSquared(o1.getLocation(),
                    location), MathUtils.getDistanceSquared(o2.getLocation(), location));
        }
    }

    /**
     * @deprecated Use {@link WeightedRandomPicker} instead (call pick()
     * multiple times).
     * @since 1.0
     */
    @Deprecated
    public static <T> List<T> weightedRandom(Map<T, Float> pickFrom, int numToPick)
    {
        Global.getLogger(CollectionUtils.class).log(Level.WARN,
                "Using deprecated method weightedRandom(Map<T, Float> pickFrom,"
                + " int numToPick)");

        if (pickFrom.isEmpty() || numToPick <= 0)
        {
            return Collections.<T>emptyList();
        }

        float totalWeight = 0.0f;
        for (Float tmp : pickFrom.values())
        {
            totalWeight += tmp;
        }

        List<T> ret = new ArrayList<T>(numToPick);
        float random;

        for (int x = 0; x < numToPick; x++)
        {
            random = MathUtils.getRandom().nextFloat() * totalWeight;
            for (Map.Entry<T, Float> tmp : pickFrom.entrySet())
            {
                random -= tmp.getValue();

                if (random <= 0.0f)
                {
                    ret.add(tmp.getKey());
                    break;
                }
            }
        }

        return ret;
    }

    /**
     * @deprecated Use {@link WeightedRandomPicker} instead.
     * @since 1.0
     */
    @Deprecated
    public static <T> T weightedRandom(Map<T, Float> pickFrom)
    {
        Global.getLogger(CollectionUtils.class).log(Level.WARN,
                "Using deprecated method weightedRandom(Map<T, Float> pickFrom)");

        if (pickFrom.isEmpty())
        {
            return null;
        }

        float totalWeight = 0.0f;
        for (Float tmp : pickFrom.values())
        {
            totalWeight += tmp;
        }

        float random = MathUtils.getRandom().nextFloat() * totalWeight;
        for (Map.Entry<T, Float> tmp : pickFrom.entrySet())
        {
            random -= tmp.getValue();

            if (random <= 0.0f)
            {
                return tmp.getKey();
            }
        }

        throw new RuntimeException("weightedRandom() failed to return a value!");
        //return (pickFrom.isEmpty() ? null : weightedRandom(pickFrom, 1).get(0));
    }

    /**
     * @deprecated Use {@link SortEntitiesByDistance} instead.
     * @since 1.1
     */
    @Deprecated
    public static class SortObjectivesByDistance implements Comparator<BattleObjectiveAPI>
    {
        private Vector2f location;

        private SortObjectivesByDistance()
        {
            Global.getLogger(CollectionUtils.class).log(Level.WARN,
                    "Using deprecated class SortObjectivesByDistance");
        }

        /**
         * @param location The central location to judge distance from.
         * <p>
         * @since 1.1
         */
        public SortObjectivesByDistance(Vector2f location)
        {
            this.location = location;
        }

        /**
         * Compares the distances of two {@link BattleObjectiveAPI}s from a
         * central location.
         *
         * @param o1 The first {@link BattleObjectiveAPI}.
         * @param o2 The second {@link BattleObjectiveAPI}.
         * <p>
         * @return A comparison of the distances of {@code o1} and {@code o2}
         *         from {@code location}.
         * <p>
         * @since 1.1
         */
        @Override
        public int compare(BattleObjectiveAPI o1, BattleObjectiveAPI o2)
        {
            return Float.compare(MathUtils.getDistanceSquared(o1.getLocation(),
                    location), MathUtils.getDistanceSquared(o2.getLocation(),
                            location));
        }
    }

    private CollectionUtils()
    {
    }
}
