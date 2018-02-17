package org.lazywizard.lazylib.combat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.ShipAPI;

class CombatCache
{
    private static final Map<Integer, Set<ShipAPI>> visCache = new HashMap<>();
    private static int lastNumShips = 0;
    private static float lastTime = 0f;

    static
    {
        // If support for more than two sides is ever added, just
        // add the side's owner number to this list
        visCache.put(0, Collections.newSetFromMap(
                new WeakHashMap<ShipAPI, Boolean>()));
        visCache.put(1, Collections.newSetFromMap(
                new WeakHashMap<ShipAPI, Boolean>()));
    }

    static List<ShipAPI> getCachedVisibleEnemies(int side)
    {
        if (!visCache.containsKey(side))
        {
            return Collections.emptyList();
        }

        int numShips = Global.getCombatEngine().getShips().size();
        float time = Global.getCombatEngine().getTotalElapsedTime(true);
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
        List<ShipAPI> ships = Global.getCombatEngine().getShips();

        for (Map.Entry<Integer, Set<ShipAPI>> entry : visCache.entrySet())
        {
            int owner = entry.getKey();
            Set<ShipAPI> visible = entry.getValue();
            visible.clear();
            for (ShipAPI tmp : ships)
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
