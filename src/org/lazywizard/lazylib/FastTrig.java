package org.lazywizard.lazylib;

/**
 * Math utility class that trades accuracy for speed, often returning several times faster than {@link Math}'s
 * equivalent functions.
 *
 * @author Various (see javadoc of individual methods for attributions)
 * @since 1.0
 */
public class FastTrig
{
    /**
     * Fast Trig functions for x86.
     * This forces the trig function to stay within the safe area on the x86
     * processor (-45 degrees to +45 degrees)
     * The results may be very slightly off from what the Math and StrictMath
     * trig functions give due to
     * rounding in the angle reduction but it will be very very close.
     * <p>
     * Originally written by JeffK, and taken from the <a href="http://slick.ninjacave.com/">Slick2D</a> game library.
     *
     * @param radians The original angle
     *
     * @return The reduced Sin angle
     *
     * @author JeffK (taken from the <a href="http://slick.ninjacave.com/">Slick2D</a> game library)
     * @since 1.0
     */
    private static double reduceSinAngle(double radians)
    {
        radians %= Math.PI * 2.0; // put us in -2PI to +2PI space
        if (Math.abs(radians) > Math.PI)
        { // put us in -PI to +PI space
            radians -= (Math.PI * 2.0);
        }
        if (Math.abs(radians) > Math.PI / 2)
        {// put us in -PI/2 to +PI/2 space
            radians = Math.PI - radians;
        }

        return radians;
    }

    /**
     * Get the sine of an angle.
     * <p>
     * Originally written by JeffK, and taken from the <a href="http://slick.ninjacave.com/">Slick2D</a> game library.
     *
     * @param radians The angle, in radians.
     *
     * @return The sine of {@code radians}.
     *
     * @author JeffK (taken from the <a href="http://slick.ninjacave.com/">Slick2D</a> game library)
     * @since 1.0
     */
    public static double sin(double radians)
    {
        radians = reduceSinAngle(radians); // limits angle to between -PI/2 and +PI/2
        if (Math.abs(radians) <= Math.PI / 4)
        {
            return Math.sin(radians);
        }
        else
        {
            return Math.cos(Math.PI / 2 - radians);
        }
    }

    /**
     * Get the cosine of an angle.
     * <p>
     * Originally written by JeffK, and taken from the <a href="http://slick.ninjacave.com/">Slick2D</a> game library.
     *
     * @param radians The angle, in radians.
     *
     * @return The cosine of {@code radians}.
     *
     * @author JeffK (taken from the <a href="http://slick.ninjacave.com/">Slick2D</a> game library)
     * @since 1.0
     */
    public static double cos(double radians)
    {
        return sin(radians + Math.PI / 2);
    }

    /**
     * Returns the arc tangent of a value. Accurate to within 0.005 radians, or ~0.29 degrees.
     * <p>
     * Originally written by Nic Taylor, and taken from <a href="https://www.dsprelated.com/showarticle/1052.php">this
     * page</a>.
     *
     * @param z The value to calculate the arc tangent of.
     *
     * @return The arc tangent of {@code z}, in radians.
     *
     * @author Nic Taylor (taken from <a href="https://www.dsprelated.com/showarticle/1052.php">this page</a>
     * @since 2.3
     */
    public static double atan(double z)
    {
        return (0.97239411 + -0.19194795 * z * z) * z;
    }

    /**
     * Returns the angle theta from the conversion of rectangular coordinates (x, y) to polar coordinates (r, theta).
     * Accurate to within 0.005 radians, or ~0.29 degrees.
     * <p>
     * Originally written by Nic Taylor, further modified by imuli, and taken from <a
     * href="https://www.dsprelated.com/showarticle/1052.php">this page</a>.
     *
     * @param y The ordinate coordinate.
     * @param x The abscissa coordinate.
     *
     * @return The theta component of the point (r, theta) in polar coordinates that corresponds to the point (x, y) in
     *         Cartesian coordinates.
     *
     * @author Nic Taylor and imuli (taken from <a href="https://www.dsprelated.com/showarticle/1052.php">this
     *         page</a>
     * @since 2.3
     */
    public static double atan2(double y, double x)
    {
        final double ay = Math.abs(y), ax = Math.abs(x);
        final boolean invert = ay > ax;
        final double z = invert ? ax / ay : ay / ax;    // [0,1]
        double th = atan(z);                            // [0,π/4]
        if (invert) th = Math.PI / 2f - th;             // [0,π/2]
        if (x < 0) th = Math.PI - th;                   // [0,π]
        return Math.copySign(th, y);                    // [-π,π]
    }

    private FastTrig()
    {
    }
}
