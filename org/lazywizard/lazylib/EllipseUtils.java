package org.lazywizard.lazylib;

import org.lwjgl.util.vector.Vector2f;

/**
 * Contains methods for dealing with ellipses. Since these methods are very
 * rarely used, they have been moved into their own class to reduce clutter in
 * the main library classes.
 *
 * @author LazyWizard
 * @since 1.9
 */
public class EllipseUtils
{
    // TODO: Javadoc this
    public static Vector2f getPointOnEllipse(Vector2f ellipseCenter,
            float ellipseWidth, float ellipseHeight, float ellipseAngleOffset, float angle)
    {
        ellipseAngleOffset = (float) Math.toRadians(ellipseAngleOffset);
        angle = (float) Math.toRadians(angle);
        float sin = (float) FastTrig.sin(angle),
                cos = (float) FastTrig.cos(angle);

        // Get point on unrotated ellipse around origin (0, 0)
        final float x = ellipseWidth * cos;
        final float y = ellipseHeight * sin;

        // Rotate point to match ellipses rotation
        sin = (float) FastTrig.sin(ellipseAngleOffset);
        cos = (float) FastTrig.cos(ellipseAngleOffset);
        return new Vector2f((x * cos) - (y * sin) + ellipseCenter.x,
                (x * sin) + (y * cos) + ellipseCenter.y);
    }

    // TODO: Javadoc this
    public static Vector2f getRandomPointOnEllipse(Vector2f ellipseCenter,
            float ellipseWidth, float ellipseHeight, float ellipseAngleOffset)
    {
        return getPointOnEllipse(ellipseCenter, ellipseWidth, ellipseHeight,
                ellipseAngleOffset, MathUtils.getRandom().nextFloat() * 360f);
    }

    // TODO: implement and make sure it's uniformly distributed
    private static void getRandomPointInEllipse(Vector2f ellipseCenter,
            float ellipseWidth, float ellipseHeight, float ellipseAngleOffset)
    {
    }

    // Also returns true if the point is ON the ellipse
    // TODO: Javadoc this
    public static boolean isPointWithinEllipse(Vector2f point, Vector2f ellipseCenter,
            float ellipseWidth, float ellipseHeight, float ellipseAngleOffset)
    {
        // Move relative to 0, 0 and rotate to match ellipse offset
        Vector2f origin = Vector2f.sub(point, ellipseCenter, null);
        VectorUtils.rotate(origin, -ellipseAngleOffset, origin);

        final float x = (origin.x * origin.x) / (ellipseWidth * ellipseWidth),
                y = (origin.y * origin.y) / (ellipseHeight * ellipseHeight);
        return ((x + y) <= 1.0001f);
    }

    private EllipseUtils()
    {
    }
}
