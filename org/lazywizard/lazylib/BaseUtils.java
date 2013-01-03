package org.lazywizard.lazylib;

import java.util.Collection;

public class BaseUtils
{
    public static String implode(Collection<String> toImplode, String separator)
    {
        StringBuilder ret = new StringBuilder();
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
}
