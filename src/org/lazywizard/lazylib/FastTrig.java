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
        if (Math.abs(radians) > Math.PI / 2.0)
        {// put us in -PI/2 to +PI/2 space
            radians = Math.PI - radians;
        }

        return radians;
    }

    private static double sinQuartic(double x) // inline
    {
        return Math.fma(x, Math.fma(x, Math.fma(x,
                Math.fma(x, 0.028713815256377853d, -0.20358090709887086d),
                0.019965253315485060d), 0.99615005303669568d), 0.00012052567744297745d);
    }

    private static double cosQuartic(double x) // inline
    {
        return Math.fma(x, Math.fma(x, Math.fma(x,
                Math.fma(x, 0.028713815256378741d, 0.023166684966924631d),
                -0.51429617377432668d), 0.0029202661377637799d), 0.99990758164524929d);
    }

    public static double sin(double radians)
    {
        // in 268435456 uniform samples under [0.0, 0.5pi]:
        //     error = [-0.0001184373484354d, 0.0001205256774430d], delta ~ 0.0001205

        // in 268435456 uniform samples under [-2pi, 2pi]:
        //     error = [-0.0001205256774430d, 0.0001205256774430d], delta ~ 0.0001205

        // in 268435456 uniform samples under [-131072, 131072]:
        //     error = [-0.0001205256328852d, 0.0001205256329412d], delta ~ 0.0001205
        if (Math.abs(radians) > 1e15d) return Math.sin(radians);
        final double n = Math.rint(radians * (1.0d / Math.PI)),
                r = Math.fma(n, -1.2246467991473532E-16d, Math.fma(n, -Math.PI, radians)),
                sinR = Math.copySign(sinQuartic(Math.abs(r)), r);
        return ((long) n & 1L) > 0L ? -sinR : sinR;
    }

    public static double cos(double radians)
    {
        // in 268435456 uniform samples under [0.0, 0.5pi]:
        //     error = [-0.0001184373484355d, 0.0001205256774428d], delta ~ 0.0001205

        // in 268435456 uniform samples under [-2pi, 2pi]:
        //     error = [-0.0001205256549143d, 0.0001205256098569d], delta ~ 0.0001205

        // in 268435456 uniform samples under [-131072, 131072]:
        //     error = [-0.0001205256212465d, 0.0001205255977449d], delta ~ 0.0001205
        if (Math.abs(radians) > 1e15d) return Math.cos(radians);
        final double n = Math.rint(radians * (1.0d / Math.PI)),
                r = Math.fma(n, -1.2246467991473532E-16d, Math.fma(n, -Math.PI, radians)),
                cosR = cosQuartic(Math.abs(r));
        return ((long) n & 1L) > 0L ? -cosR : cosR;
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
    public static double sin_Slick2D(double radians)
    {
        radians = reduceSinAngle(radians); // limits angle to between -PI/2 and +PI/2
        if (Math.abs(radians) <= Math.PI / 4.0)
        {
            return Math.sin(radians);
        }
        else
        {
            return Math.cos(Math.PI / 2.0 - radians);
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
    public static double cos_Slick2D(double radians)
    {
        return sin_Slick2D(radians + Math.PI / 2.0);
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
        // Fix supplied by Genir on the Discord, https://discord.com/channels/187635036525166592/310517733458706442/1253123181275775017
        if (Math.abs(z) > 1.0)
        {
            return Math.signum(z) * Math.PI / 2f - FastTrig.atan(1f / z);
        }

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
        if (invert) th = Math.PI / 2.0 - th;            // [0,π/2]
        if (x < 0.0) th = Math.PI - th;                 // [0,π]
        return Math.copySign(th, y);                    // [-π,π]
    }

    private FastTrig()
    {
    }
}
