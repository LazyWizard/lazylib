package org.lazywizard.lazylib;

import com.fs.starfarer.api.combat.BoundsAPI;
import com.fs.starfarer.api.combat.BoundsAPI.SegmentAPI;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import java.util.ArrayList;
import java.util.List;
import org.lwjgl.util.vector.Vector2f;

/**
 * Contains methods for working with bounds and collision detection.
 *
 * @author LazyWizard
 */
public class CollisionUtils
{
    /**
     * Finds the part of the ship that would be intersected by a given path.
     *
     * @param target The CombatEntityAPI to check collision with.
     * @param lineStart The start of the line to test collision with.
     * @param lineEnd The end of the line to test collision with.
     * @return The {@link Vector2f} of the point the line would hit at, or null if it doesn't hit.
     */
    public static Vector2f getCollisionPoint(Vector2f lineStart, Vector2f lineEnd, CombatEntityAPI target)
    {
        BoundsAPI bounds = target.getExactBounds();

        // Entities that lack bounds will use the collision circle instead
        if (bounds == null)
        {
            if (getCollides(lineStart, lineEnd,
                    target.getLocation(), target.getCollisionRadius()))
            {
                return target.getLocation();
            }

            return null;
        }

        Vector2f closestIntersection = null;

        // Convert all segments to lines, do collision checks to find closest hit
        for (SegmentAPI tmp : bounds.getSegments())
        {
            Vector2f intersection =
                    getCollisionPoint(tmp.getP1(), tmp.getP2(), lineStart, lineEnd);
            // Collision = true
            if (intersection != null)
            {
                if (closestIntersection == null)
                {
                    closestIntersection = new Vector2f(intersection);
                }
                else if (MathUtils.getDistanceSquared(lineStart, intersection)
                        > MathUtils.getDistanceSquared(lineStart, closestIntersection))
                {
                    closestIntersection.set(intersection);
                }
            }
        }

        // Null if no segment was hit
        return closestIntersection;
    }

    /**
     * Finds the point of intersection between two lines.
     *
     * @param start1 The start of the first line to test collision with.
     * @param end1 The end of the first line to test collision with.
     * @param start2 The start of the second line to test collision with.
     * @param end2 The end of the second line to test collision with.
     * @return The {@link Vector2f} that the two lines intersect at, null if they don't collide.
     */
    public static Vector2f getCollisionPoint(Vector2f start1, Vector2f end1, Vector2f start2, Vector2f end2)
    {
        float denom = ((end1.x - start1.x) * (end2.y - start2.y))
                - ((end1.y - start1.y) * (end2.x - start2.x));

        //  AB & CD are parallel
        if (denom == 0)
        {
            return null;
        }

        float numer = ((start1.y - start2.y) * (end2.x - start2.x))
                - ((start1.x - start2.x) * (end2.y - start2.y));
        float r = numer / denom;
        float numer2 = ((start1.y - start2.y) * (end1.x - start1.x))
                - ((start1.x - start2.x) * (end1.y - start1.y));
        float s = numer2 / denom;

        if ((r < 0 || r > 1) || (s < 0 || s > 1))
        {
            return null;
        }

        // Find intersection point
        Vector2f result = new Vector2f();
        result.x = start1.x + (r * (end1.x - start1.x));
        result.y = start1.y + (r * (end1.y - start1.y));

        return result;
    }

    /**
     * Check if a line connects with a circle.
     *
     * @param lineStart The start point of the line to test.
     * @param lineEnd The end point of the line to test.
     * @param center The center point of the circle.
     * @param radius The radius of the circle.
     * @return {@code true} if the line collides with the circle, {@code false} otherwise.
     */
    public static boolean getCollides(Vector2f lineStart, Vector2f lineEnd,
            Vector2f center, float radius)
    {
        if ((new Line(lineStart, lineEnd)).ptSegDist(center.x, center.y)
                <= radius)
        {
            return true;
        }

        return false;
    }

    /**
     * Checks if a point is inside of the bounds of a {@link CombatEntityAPI}.
     *
     * @param point The {@link Vector2f} to check.
     * @param entity The {@link CombatEntityAPI} whose {@link BoundsAPI} we are checking against.
     * @return {@code true} if {@link point} is within the bounds of {@code entity}, {@code false} otherwise.
     */
    public static boolean isPointWithinBounds(Vector2f point, CombatEntityAPI entity)
    {
        if (entity.getExactBounds() == null)
        {
            return MathUtils.isPointWithinCircle(point, entity.getLocation(),
                    entity.getCollisionRadius());
        }
        BoundsAPI bounds = entity.getExactBounds();
        bounds.update(entity.getLocation(), entity.getFacing());
        //Polygon poly = Convert.boundsToPolygon(bounds);
        //return poly.contains(point.x, point.y);

        // TODO: Test this thoroughly!
        List<SegmentAPI> segments = bounds.getSegments();
        List<Vector2f> points = new ArrayList();
        for (int x = 0; x < segments.size(); x++)
        {
            points.add(segments.get(x).getP1());

            if (x == (segments.size() - 1))
            {
                points.add(segments.get(x).getP2());
            }
        }

        int i, j;
        boolean result = false;
        for (i = 0, j = points.size() - 1; i < points.size(); j = i++)
        {
            if ((points.get(i).y > point.y) != (points.get(j).y > point.y)
                    && (point.x < (points.get(j).x - points.get(i).x)
                    * (point.y - points.get(i).y)
                    / (points.get(j).y - points.get(i).y) + points.get(i).x))
            {
                result = !result;
            }
        }
        return result;
    }

    private CollisionUtils()
    {
    }
}
