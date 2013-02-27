package org.lazywizard.lazylib;

import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.lwjgl.util.vector.Vector2f;

/**
 * Contains methods for working with Collections.
 * @author LazyWizard
 */
public class CollectionUtils
{
    private static final Random rng = new Random();

    /**
     * Returns a {@link List} of items chosen via a weighted random from a {@link Map}.
     *
     * @param pickFrom A {@link Map} of items to choose from.
     * The value is the weight, in float form, of that item being chosen.
     * @param numToPick How many items to choose from {@code pickFrom}'s keys.
     * @return A {@link List} containing the subset of {@code pickFrom} chosen.
     */
    public static <T> List<T> weightedRandom(Map<T, Float> pickFrom, int numToPick)
    {
        if (pickFrom.isEmpty() || numToPick == 0)
        {
            return Collections.EMPTY_LIST;
        }

        float totalWeight = 0.0f;
        for (Float tmp : pickFrom.values())
        {
            totalWeight += tmp;
        }

        List<T> ret = new ArrayList();
        float random;

        for (int x = 0; x < numToPick; x++)
        {
            random = rng.nextFloat() * totalWeight;
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
     * Returns a single item chosen via a weighted random from a {@link Map}.
     *
     * @param pickFrom A {@link Map} of items to choose from.
     * The value is the weight, in float form, of that item being chosen.
     * @return A single item chosen from {@code pickFrom}'s keys.
     */
    public static <T> T weightedRandom(Map<T, Float> pickFrom)
    {
        return (pickFrom.isEmpty() ? null : weightedRandom(pickFrom, 1).get(0));
    }

    /**
     * Combines and separates a {@link Collection} of {@link String}s. Useful for comma-separated lists.
     *
     * @param toImplode A {@link Collection} of {@link String}s to be combined.
     * @param separator The separator character to split [@code toImplode} with.
     * @return A single {@link String} consisting of {@code toImplode}'s values
     * separated with [@code separator}.
     */
    public static String implode(Collection<String> toImplode, String separator)
    {
        if (toImplode.isEmpty())
        {
            return "";
        }

        StringBuilder ret = new StringBuilder(toImplode.size() * 16);
        String[] tmp = toImplode.toArray(new String[toImplode.size()]);

        for (int x = 0; x < tmp.length; x++)
        {
            if (x != 0 && separator != null)
            {
                ret.append(separator);
            }

            ret.append(tmp[x]);
        }

        return ret.toString();
    }

    /**
     * Creates a comma-separated {@link String} from a {@link Collection} of {@link String}s.
     *
     * @param toImplode A {@link Collection} of {@link String}s to be combined.
     * @return A single {@link String} consisting of {@code toImplode}'s values
     * separated with commas.
     * @see CollectionUtils#implode(java.util.Collection, java.lang.String)
     */
    public static String implode(Collection<String> toImplode)
    {
        return implode(toImplode, ", ");
    }

    /**
     * A {@link Comparator} that sorts {@link CombatEntityAPI}s by distance from a {@link Vector2f}.
     */
    public static class SortEntitiesByDistance implements Comparator<CombatEntityAPI>
    {
        private Vector2f location;

        private SortEntitiesByDistance()
        {
        }

        /**
         * @param location The central location to judge distance from.
         */
        public SortEntitiesByDistance(Vector2f location)
        {
            this.location = location;
        }

        /**
         * Compares the distances of two {@link CombatEntityAPI}s from a central location.
         *
         * @param o1 The first {@link CombatEntityAPI}.
         * @param o2 The second {@link CombatEntityAPI}.
         * @return A comparison of the distances of {@code o1} and {@code o2}
         * from {@code location}.
         */
        @Override
        public int compare(CombatEntityAPI o1, CombatEntityAPI o2)
        {
            return ((Float) MathUtils.getDistanceSquared(o1, location)).compareTo(
                    (Float) MathUtils.getDistanceSquared(o2, location));
        }
    }

    /**
     * A {@link Comparator} that sorts {@link SectorEntityToken}s by distance from a {@link Vector2f}.
     */
    public static class SortTokensByDistance implements Comparator<SectorEntityToken>
    {
        private Vector2f location;

        private SortTokensByDistance()
        {
        }

        /**
         * @param location The central location to judge distance from.
         */
        public SortTokensByDistance(Vector2f location)
        {
            this.location = location;
        }

        /**
         * Compares the distances of two {@link SectorEntityToken}s from a central location.
         *
         * @param o1 The first {@link SectorEntityToken}.
         * @param o2 The second {@link SectorEntityToken}.
         * @return A comparison of the distances of {@code o1} and {@code o2}
         * from {@code location}.
         */
        @Override
        public int compare(SectorEntityToken o1, SectorEntityToken o2)
        {
            return ((Float) MathUtils.getDistanceSquared(o1, location)).compareTo(
                    (Float) MathUtils.getDistanceSquared(o2, location));
        }
    }

    private CollectionUtils()
    {
    }
}
