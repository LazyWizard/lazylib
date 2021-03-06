package org.lazywizard.lazylib;

import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import org.lwjgl.util.vector.Vector2f;

import java.util.*;

/**
 * Contains methods for working with Collections.
 *
 * @author LazyWizard
 * @since 1.0
 */
@SuppressWarnings("rawtypes")
public class CollectionUtils
{
    /**
     * Combines and separates a {@link Collection} of {@link String}s. Useful
     * for comma-separated lists.
     *
     * @param toImplode A {@link Collection} whose contents should be combined
     *                  into one {@link String}.
     * @param separator The separator character to split {@code toImplode} with.
     *
     * @return A single {@link String} consisting of {@code toImplode}'s values
     *         separated with {@code separator}.
     *
     * @since 1.0
     */
    public static String implode(Collection toImplode, String separator)
    {
        if (toImplode.isEmpty())
        {
            return "";
        }

        StringBuilder ret = new StringBuilder(toImplode.size() * 16);
        for (Iterator iter = toImplode.iterator(); iter.hasNext(); )
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
     * @param toImplode A {@link Collection} to be combined (using each entry's {@code toString} method).
     *
     * @return A single {@link String} consisting of {@code toImplode}'s values
     *         separated with commas.
     *
     * @see CollectionUtils#implode(java.util.Collection, java.lang.String)
     * @since 1.0
     */
    public static String implode(Collection toImplode)
    {
        return implode(toImplode, ", ");
    }

    /**
     * Creates a {@link String} containing the names of an {@link Enum}'s entries.
     *
     * @param toImplode An {@link Enum} whose contents should be combined into one {@link String}.
     * @param separator The separator character to split {@code toImplode} with.
     *
     * @return A single {@link String} consisting of {@code toImplode}'s values
     *         separated with {@code separator}.
     *
     * @since 2.3
     */
    public static <T> String implode(Class<? extends Enum> toImplode, String separator)
    {
        final ArrayList<String> tmp = new ArrayList<>();
        for (Enum value : toImplode.getEnumConstants())
        {
            tmp.add(value.name());
        }

        return implode(tmp, separator);
    }

    /**
     * Creates a comma-separated {@link String} containing the names of an {@link Enum}'s entries.
     *
     * @param toImplode An {@link Enum} to be combined (using each entry's {@code name()} method).
     *
     * @return A single {@link String} consisting of {@code toImplode}'s values separated with commas.
     *
     * @see CollectionUtils#implode(Class, String)
     * @since 2.3
     */
    public static <T> String implode(Class<? extends Enum> toImplode)
    {
        return implode(toImplode, ", ");
    }

    /**
     * Returns a combined {@link List} from several {@link Collection}s.
     *
     * @param toCombine The {@link Collection} of elements to combine.
     *
     * @return A new {@link ArrayList} containing the combined contents of {@code toCombine}.
     *
     * @since 2.1
     */
    @SafeVarargs
    public static <T> List<T> combinedList(Collection<T>... toCombine)
    {
        int size = 0;
        for (Collection<T> tmp : toCombine)
        {
            size += tmp.size();
        }

        final List<T> combined = new ArrayList<>(size);
        for (Collection<T> tmp : toCombine)
        {
            combined.addAll(tmp);
        }

        return combined;
    }

    /**
     * Returns a combined {@link Set} from several {@link Collection}s.
     *
     * @param toCombine The {@link Collection} of elements to combine.
     *
     * @return A new {@link HashSet} containing the combined contents of
     *         {@code toCombine}. The capacity will be the total size of all
     *         source {@link Collection}s.
     *
     * @since 2.1
     */
    @SafeVarargs
    public static <T> Set<T> combinedSet(Collection<T>... toCombine)
    {
        int size = 0;
        for (Collection<T> tmp : toCombine)
        {
            size += tmp.size();
        }

        final Set<T> combined = new HashSet<>(size);
        for (Collection<T> tmp : toCombine)
        {
            combined.addAll(tmp);
        }

        return combined;
    }

    /**
     * Filters a {@link Collection} and returns a {@link List} containing only
     * the entries that the filter accepted.
     *
     * @param toFilter The {@link Collection} to filter.
     * @param filter   A {@link CollectionFilter} that will be used to filter
     *                 {@code toFilter}.
     *
     * @return A {@link List} containing only the entries of {@code toFilter}
     *         that passed {@code filter}'s {@code accept()} method.
     *
     * @since 1.7
     */
    public static <T> List<T> filter(Collection<T> toFilter, CollectionFilter<T> filter)
    {
        List<T> filtered = new ArrayList<>();
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
     * Filters a {@link Collection} and returns a {@link List} containing only
     * the entries that the filters accepted.
     *
     * @param toFilter The {@link Collection} to filter.
     * @param filters  A {@link List} of {@link CollectionFilter}s that will be
     *                 used to filter {@code toFilter}.
     *
     * @return A {@link List} containing only the entries of {@code toFilter}
     *         that passed {@code filters}' {@code accept()} methods.
     *
     * @see CollectionUtils#filter(Collection, CollectionUtils.CollectionFilter)
     * @since 1.7
     */
    public static <T> List<T> filter(Collection<T> toFilter, List<CollectionFilter<T>> filters)
    {
        List<T> filtered = new ArrayList<>();
        outer:
        for (T tmp : toFilter)
        {
            for (CollectionFilter<T> filter : filters)
            {
                if (!filter.accept(tmp))
                {
                    continue outer;
                }
            }

            filtered.add(tmp);
        }

        return filtered;
    }

    /**
     * Used with
     * {@link CollectionUtils#filter(Collection, CollectionUtils.CollectionFilter)}
     * to filter out unwanted entries in a {@link Collection}.
     *
     * @param <T> The type of object to be filtered.
     *
     * @since 1.7
     */
    public interface CollectionFilter<T>
    {
        /**
         * Checks if an object should be allowed past this filter.
         *
         * @param t The object to be checked.
         *
         * @return {@code true} if this object should be kept in the
         *         {@link Collection}.
         *
         * @since 1.7
         */
        boolean accept(T t);
    }

    /**
     * A {@link Comparator} that sorts {@link CombatEntityAPI}s by distance from
     * the {@link Vector2f} passed into the constructor.
     *
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
         *
         * @since 1.6
         */
        public SortEntitiesByDistance(Vector2f location, boolean includeRadius)
        {
            this.location = location;
            this.includeRadius = includeRadius;
        }

        /**
         * @param location The central location to judge distance from.
         *
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
         *
         * @return A comparison of the distances of {@code o1} and {@code o2}
         *         from {@code location}.
         *
         * @since 1.1
         */
        @Override
        public int compare(CombatEntityAPI o1, CombatEntityAPI o2)
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
     * A {@link Comparator} that sorts {@link SectorEntityToken}s by distance
     * the {@link Vector2f} passed into the constructor.
     *
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
         *
         * @since 1.6
         */
        public SortTokensByDistance(Vector2f location, boolean includeRadius)
        {
            this.location = location;
            this.includeRadius = includeRadius;
        }

        /**
         * @param location The central location to judge distance from.
         *
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
         *
         * @return A comparison of the distances of {@code o1} and {@code o2}
         *         from {@code location}.
         *
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

    private CollectionUtils()
    {
    }
}
