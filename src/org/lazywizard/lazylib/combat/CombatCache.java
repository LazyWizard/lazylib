package org.lazywizard.lazylib.combat;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.ShipAPI;

import java.util.*;

class CombatCache
{
    private static final Map<Integer, Set<ShipAPI>> visCache = new HashMap<>();
    private static int lastNumShips = 0;
    private static float lastTime = 0f;

    static
    {
        // If support for more than two sides is ever added, just
        // add the side's owner number to this list
        visCache.put(0, Collections.newSetFromMap(new WeakHashMap<>()));
        visCache.put(1, Collections.newSetFromMap(new WeakHashMap<>()));
    }

    static List<ShipAPI> getCachedVisibleEnemies(int side)
    {
        if (!visCache.containsKey(side))
        {
            return Collections.emptyList();
        }

        final int numShips = Global.getCombatEngine().getShips().size();
        final float time = Global.getCombatEngine().getTotalElapsedTime(true);
        if ((numShips != lastNumShips) || (time != lastTime))
        {
            cacheMapVisible();
            lastNumShips = numShips;
            lastTime = time;
        }

        return new ArrayList<>(visCache.get(side));
    }

    private static void cacheMapVisible()
    {
        for (Map.Entry<Integer, Set<ShipAPI>> entry : visCache.entrySet())
        {
            final int owner = entry.getKey();
            final Set<ShipAPI> visible = entry.getValue();
            visible.clear();
            for (ShipAPI tmp : Global.getCombatEngine().getShips())
            {
                if (tmp.getOwner() != owner && !tmp.isHulk() && !tmp.isShuttlePod()
                        && CombatUtils.isVisibleToSide(tmp, owner))
                {
                    visible.add(tmp);
                }
            }
        }
    }

    private CombatCache()
    {
    }
}
