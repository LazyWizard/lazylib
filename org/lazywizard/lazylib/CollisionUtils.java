package org.lazywizard.lazylib;

import com.fs.starfarer.api.combat.BoundsAPI;
import com.fs.starfarer.api.combat.BoundsAPI.SegmentAPI;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;
import org.lwjgl.util.vector.Vector2f;

/**
 * Contains methods for line intersection, bounds and collision detection tests.
 *
 * @author LazyWizard
 * @since 1.0
 */
public class CollisionUtils
{
    /**
     * Finds the part of the ship that would be intersected by a given path.
     *
     * @param target    The CombatEntityAPI to check collision with.
     * @param lineStart The start of the line to test collision with.
     * @param lineEnd   The end of the line to test collision with.
     * <p>
     * @return The {@link Vector2f} of the point the line would hit at,
     *         or {@code null} if it doesn't hit.
     * <p>
     * @since 1.0
     */
    public static Vector2f getCollisionPoint(Vector2f lineStart,
            Vector2f lineEnd, CombatEntityAPI target)
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
            Vector2f intersection
                    = getCollisionPoint(lineStart, lineEnd, tmp.getP1(), tmp.getP2());
            // Collision = true
            if (intersection != null)
            {
                if (closestIntersection == null)
                {
                    closestIntersection = new Vector2f(intersection);
                }
                else if (MathUtils.getDistanceSquared(lineStart, intersection)
                        < MathUtils.getDistanceSquared(lineStart, closestIntersection))
                {
                    closestIntersection.set(intersection);
                }
            }
        }

        // Null if no segment was hit
        return closestIntersection;
    }

    /**
     * Finds the point of intersection between two lines. Accurate to within
     * 1/3 su.
     *
     * @param start1 The start of the first line to test collision with.
     * @param end1   The end of the first line to test collision with.
     * @param start2 The start of the second line to test collision with.
     * @param end2   The end of the second line to test collision with.
     * <p>
     * @return The {@link Vector2f} that the two lines intersect at,
     *         {@code null} if they don't collide.
     * <p>
     * @since 1.0
     */
    public static Vector2f getCollisionPoint(Vector2f start1, Vector2f end1,
            Vector2f start2, Vector2f end2)
    {
        if (Line2D.Float.ptSegDistSq(start2.x, start2.y, end2.x, end2.y,
                end1.x, end1.y) <= 0.11111f)
        {
            return end1;
        }

        if (Line2D.Float.ptSegDistSq(start2.x, start2.y, end2.x, end2.y,
                start1.x, start1.y) <= 0.11111f)
        {
            return start1;
        }

        float denom = ((end1.x - start1.x) * (end2.y - start2.y))
                - ((end1.y - start1.y) * (end2.x - start2.x));

        //  AB & CD are parallel
        if (MathUtils.equals(0f, denom))
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
     * Checks if a line connects with a circle.
     *
     * @param lineStart    The start point of the line to test.
     * @param lineEnd      The end point of the line to test.
     * @param circleCenter The center point of the circle.
     * @param circleRadius The radius of the circle.
     * <p>
     * @return {@code true} if the line collides with the circle,
     *         {@code false} otherwise.
     * <p>
     * @since 1.0
     */
    public static boolean getCollides(Vector2f lineStart, Vector2f lineEnd,
            Vector2f circleCenter, float circleRadius)
    {
        // Check if distance between line and center is within radius
        return Line2D.ptSegDistSq(lineStart.x, lineStart.y, lineEnd.x,
                lineEnd.y, circleCenter.x, circleCenter.y)
                <= (circleRadius * circleRadius);
    }

    /**
     * Checks if a point is inside the collision circle of a
     * {@link CombatEntityAPI}.
     *
     * @param point  The {@link Vector2f} to check.
     * @param entity The {@link CombatEntityAPI} whose {@link BoundsAPI} we
     *               are checking against.
     * <p>
     * @return {@code true} if {@code point} is within the collision circle
     *         of {@code entity}, {@code false} otherwise.
     * <p>
     * @since 1.4
     */
    public static boolean isPointWithinCollisionCircle(Vector2f point,
            CombatEntityAPI entity)
    {
        return MathUtils.isPointWithinCircle(point, entity.getLocation(),
                entity.getCollisionRadius());
    }

    /**
     * Check if a point is along a {@link SegmentAPI}. Accurate to within 1/3
     * su, use {@link MathUtils#isPointOnLine(Vector2f, Vector2f, Vector2f)}
     * if you need higher precision.
     *
     * @param point   The point to check.
     * @param segment The {@link SegmentAPI} to check for collision with.
     * <p>
     * @return {@code true} if the point is along the line, {@code false}
     *         otherwise.
     * <p>
     * @since 1.6b
     */
    public static boolean isPointOnSegment(Vector2f point, SegmentAPI segment)
    {
        return (Line2D.Float.ptSegDistSq(segment.getP1().x, segment.getP1().y,
                segment.getP2().x, segment.getP2().y, point.x, point.y) <= 0.11111f);
    }

    /**
     * Checks if a point is inside or on the bounds of a
     * {@link CombatEntityAPI}.
     *
     * @param point  The {@link Vector2f} to check.
     * @param entity The {@link CombatEntityAPI} whose {@link BoundsAPI} we
     *               are checking against.
     * <p>
     * @return {@code true} if {@code point} is within or on the bounds of
     *         {@code entity}, {@code false} otherwise.
     * <p>
     * @since 1.0
     */
    public static boolean isPointWithinBounds(Vector2f point, CombatEntityAPI entity)
    {
        // If the entity lacks bounds, use the collision circle instead
        if (entity.getExactBounds() == null)
        {
            return isPointWithinCollisionCircle(point, entity);
        }

        // Check if it's even possible there's a collision
        if (!isPointWithinCollisionCircle(point, entity))
        {
            return false;
        }

        // Grab the ship bounds and update them to reflect the ship's position
        BoundsAPI bounds = entity.getExactBounds();
        bounds.update(entity.getLocation(), entity.getFacing());

        // Transform the bounds into a series of points
        List<SegmentAPI> segments = bounds.getSegments();
        List<Vector2f> points = new ArrayList<>(segments.size() + 1);
        SegmentAPI seg;
        for (int x = 0; x < segments.size(); x++)
        {
            seg = segments.get(x);

            // Use this opportunity to test if the point is exactly on the bounds
            if (CollisionUtils.isPointOnSegment(point, seg))
            {
                return true;
            }

            points.add(seg.getP1());
            // Make sure to add the final point
            if (x == (segments.size() - 1))
            {
                points.add(seg.getP2());
            }
        }

        // Check if the point is inside the bounds polygon
        // This code uses the extremely efficient PNPOLY solution taken from:
        // http://www.ecse.rpi.edu/Homepages/wrf/Research/Short_Notes/pnpoly.html
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
