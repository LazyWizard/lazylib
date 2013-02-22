package org.lazywizard.lazylib;

import java.util.*;

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
     * Combines and separates a {@link Collection} of strings. Useful for comma-separated lists.
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
     * Creates a comma-separated {@link String} from a {@link Collection}.
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

    public static void main(String[] args)
    {
        Map fleets = new HashMap();
        fleets.put("supplyConvoy", 75f);
        fleets.put("fuelConvoy", 25f);
        fleets.put("personnelConvoy", 25f);
        fleets.put("shipConvoy", 25f);

        //System.out.println((String) weightedRandom(fleets));

        List types = CollectionUtils.weightedRandom(fleets, 30);
        for (int x = 1; x <= types.size(); x++)
        {
            System.out.print((String) types.get(x - 1) + " ");
            if (x % 10 == 0)
            {
                System.out.println();
            }
        }
    }

    private CollectionUtils()
    {
    }
}
