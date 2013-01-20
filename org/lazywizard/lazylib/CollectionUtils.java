package org.lazywizard.lazylib;

import java.util.*;

public class CollectionUtils
{
    public static <T> T weightedRandom(Map<T, Float> pickFrom)
    {
        float totalWeight = 0.0f;
        for (Float tmp : pickFrom.values())
        {
            totalWeight += tmp;
        }

        float random = (float) Math.random() * totalWeight;
        for (Map.Entry<T, Float> tmp : pickFrom.entrySet())
        {
            random -= tmp.getValue();

            if (random <= 0.0f)
            {
                return tmp.getKey();
            }
        }

        return null;
    }

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
            if (x != 0)
            {
                ret.append(separator);
            }

            ret.append(tmp[x]);
        }

        return ret.toString();
    }

    public static String implode(Collection<String> toImplode)
    {
        return implode(toImplode, ", ");
    }

    private CollectionUtils()
    {
    }
}
