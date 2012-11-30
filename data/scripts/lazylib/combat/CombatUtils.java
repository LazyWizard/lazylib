package data.scripts.lazylib.combat;

import com.fs.starfarer.api.combat.BoundsAPI.SegmentAPI;
import data.scripts.lazylib.Line;

public class CombatUtils
{
    public static enum DefenseType
    {
        HULL,
        ARMOR,
        SHIELD,
        PHASE,
        MISS
    }

    public static Line convertSegmentToLine(SegmentAPI segment)
    {
        return new Line(segment.getP1(), segment.getP2());
    }
}
