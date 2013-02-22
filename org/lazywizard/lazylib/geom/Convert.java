package org.lazywizard.lazylib.geom;

import com.fs.starfarer.api.combat.BoundsAPI;
import com.fs.starfarer.api.combat.BoundsAPI.SegmentAPI;
import com.fs.starfarer.api.combat.CombatEntityAPI;

/**
 * Contains methods to convert between Starsector/Slick2D geometry types.
 * @author LazyWizard
 */
public final class Convert
{
    public static Line segmentToLine(SegmentAPI segment)
    {
        return new Line(segment.getP1().x, segment.getP1().y,
                segment.getP2().x, segment.getP2().y);
    }

    // TODO
    /*public static Line vectorToLine(Vector2f vector)
     {
     Vector2f tmp = //

     return new Line(vector.x, vector.y, tmp.x, tmp.y);
     }*/
    public static Shape entityToShape(CombatEntityAPI entity)
    {
        if (entity.getExactBounds() == null)
        {
            return new Circle(entity.getLocation().x, entity.getLocation().y,
                    entity.getCollisionRadius());
        }

        return boundsToPolygon(entity.getExactBounds());
    }

    public static Polygon boundsToPolygon(BoundsAPI bounds)
    {
        float[] points = new float[bounds.getSegments().size() * 4];
        int cur = 0;

        for (SegmentAPI seg : bounds.getSegments())
        {
            points[cur] = seg.getP1().x;
            points[cur + 1] = seg.getP1().y;
            points[cur + 2] = seg.getP2().x;
            points[cur + 3] = seg.getP2().y;
            cur += 4;
        }

        return new Polygon(points);
    }

    private Convert()
    {
    }
}