package org.lazywizard.lazylib.campaign.orbits;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.OrbitAPI;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import org.lwjgl.util.vector.Vector2f;

/**
 * Keplerian orbit simulation.
 *
 * @author Liral
 * @since 2.8
 */
public class KeplerOrbit implements OrbitAPI {

    public static final float MINIMUM_ECCENTRICITY = 0.001f, MAXIMUM_ECCENTRICITY = 0.999f,
            MINIMUM_AXIS = 100, MAXIMUM_AXIS = 1000000,
            MINIMUM_PERIOD = 10, MAXIMUM_PERIOD = 1000000;

    private static final float MAXIMUM_ERROR = 0.0001f, ECCENTRICITY_CUTOFF = 0.8f,
            TWO_PI = 2f * (float) Math.PI;
    private static final int MAXIMUM_ITERATIONS = 100;

    private final SectorEntityToken focus;
    private final boolean clockwise;
    private final int direction;
    private final float semiMinorAxis, semiMajorAxis,
            angle,
            period,
            eccentricity,
            pericenterTime,
            averageAngularSweepRate;

    private SectorEntityToken entity;
    private float eccentricAnomaly, time;

    /**
     * Elliptical <a href="https://en.wikipedia.org/wiki/Kepler_orbit">Kepler Orbit</a>
     * of one body around another.
     * <p></p>
     * Approximates the orbit of a body to a <a href="https://en.wikipedia.org/wiki/Patched_conic_approximation">patched conic orbit</a>
     * of given <a href="https://en.wikipedia.org/wiki/Orbital_elements#Keplerian">orbital
     * elements</a> and thereafter advances the position of the body by
     * calculating its <a href="https://en.wikipedia.org/wiki/Eccentric_anomaly">eccentric anomaly</a>
     * on the patched conic orbit with an iterative <a href="https://en.wikipedia.org/wiki/Kepler%27s_equation#Numerical_approximation_of_inverse_problem">Newton-Raphson</a>
     * solution to <a href="https://en.wikipedia.org/wiki/Kepler%27s_equation">Kepler's Equation</a>.
     * <p></p>
     * <list>
     * Sets the eccentric anomaly to the <a href="https://en.wikipedia.org/wiki/Mean_anomaly">mean anomaly</a>
     * whenever the Newton-Raphson method yields no solution.
     * <p></p>

     * Prevents absurdity and ensures <a href="https://en.wikipedia.org/wiki/Numerical_stability">numerical stability</a>
     * by adjusting or clamping impossible or extreme orbital elements:
     * <list>
     * <li>Sets each axis to its absolute value, clamped to
     * [{@value #MINIMUM_AXIS}, {@value #MAXIMUM_AXIS}], and respectively sets
     * the semi-minor and semi-major axis to the lesser and greater of the two
     * resulting values.</li>
     * <li>Slightly shifts the resulting semi-major axis should it entail an
     * orbit of such <a href="https://en.wikipedia.org/wiki/Orbital_eccentricity">orbital eccentricity</a>
     * (eccentricity = 1 - semiMinorAxis/semiMajorAxis <
     * {@value #MAXIMUM_ECCENTRICITY}) or so nearly circular (eccentricity <
     * {@value #MINIMUM_ECCENTRICITY})) that the Newton-Raphson method
     * would be numerically-unstable.</li>
     * <li>Sets the angle to its absolute value.</li>
     * <li>Sets the period to its absolute value, clamped to
     * [{@value #MINIMUM_PERIOD}, {@value #MAXIMUM_PERIOD}].</li>
     * </list>
     *
     * @param focus {@link SectorEntityToken} whereabout the body orbits
     * @param semiMajorAxis {@code float} length of the <a href="https://en.wikipedia.org/wiki/Semi-major_and_semi-minor_axes">semi-minor axis</a>,
     *                      which is the distance from the {@code focus}
     *                      to the orbital point furthest from it
     * @param semiMinorAxis {@code float} length of the <a href="https://en.wikipedia.org/wiki/Semi-major_and_semi-minor_axes">semi-minor axis</a>,
     *                      which is the distance from the {@code focus}
     *                      to either of the two points where a line
     *                      perpendicular to the {@code semiMajorAxis}
     *                      intersects the orbit
     * @param angle {@code float} degrees counterclockwise from horizontally
     *              right to the 'first' semi-major axis
     * @param period {@code float} duration of one orbit in days
     * @param clockwise {@code boolean} whether the orbit is clockwise
     *                  ({@code true}) or counterclockwise ({@code false})
     */
    public KeplerOrbit(final SectorEntityToken focus,
                       float semiMajorAxis,
                       float semiMinorAxis,
                       final float angle,
                       final float period,
                       final boolean clockwise)
    {
        this.focus = focus;
        semiMajorAxis = Math.abs(semiMajorAxis);
        semiMinorAxis = Math.abs(semiMinorAxis);
        float longerAxis = Math.max(semiMajorAxis, semiMinorAxis);
        float shorterAxis = Math.min(semiMajorAxis, semiMinorAxis);
        semiMajorAxis = Math.min(Math.max(longerAxis, MINIMUM_AXIS), MAXIMUM_AXIS);
        semiMinorAxis = Math.min(Math.max(shorterAxis, MINIMUM_AXIS), MAXIMUM_AXIS);
        final float e = 1 - semiMinorAxis / semiMajorAxis;
        if (e < MINIMUM_ECCENTRICITY) semiMajorAxis = semiMinorAxis * (1 + MINIMUM_ECCENTRICITY);
        else if (e > MAXIMUM_ECCENTRICITY)
            semiMajorAxis = semiMinorAxis / (1 - MAXIMUM_ECCENTRICITY);
        this.semiMinorAxis = semiMajorAxis; this.semiMajorAxis = semiMajorAxis;

        this.angle = Math.abs(angle);
        this.period = Math.min(Math.max(Math.abs(period), MINIMUM_PERIOD), MAXIMUM_PERIOD);
        this.clockwise = clockwise;

        time = 0;
        eccentricity = 1 - semiMinorAxis / semiMajorAxis;
        pericenterTime = period * angle % 360;
        averageAngularSweepRate = TWO_PI / period;
        direction = clockwise ? 1 : -1;

        eccentricAnomaly = getInitialEccentricAnomalyApproximation();
    }

    private float getMeanAnomaly() {
        return averageAngularSweepRate * (time - pericenterTime);
    }

    /**
     * @return {@code float} <a href="https://en.wikipedia.org/wiki/Mean_anomaly">mean anomaly</a>
     *         of the orbit if its <a href="https://en.wikipedia.org/wiki/Orbital_eccentricity">orbital eccentricity</a>
     *         is below {@value #ECCENTRICITY_CUTOFF}, or else {@value Math#PI}
     */
    private float getInitialEccentricAnomalyApproximation() {
        return (eccentricity < ECCENTRICITY_CUTOFF) ? getMeanAnomaly() : (float) Math.PI;
    }

    /**
     * @param eccentricAnomaly {@code float}
     * @return {@code float} eccentric anomaly, iteratively updated from
     *         its previous {@code float} value
     */
    private float f(final float eccentricAnomaly) {
        return eccentricAnomaly
                - eccentricity * (float) Math.sin(eccentricAnomaly)
                - getMeanAnomaly();
    }

    /**
     * @param eccentricAnomaly {@code float}
     * @return {@code float} eccentric anomaly derivative, iteratively updated
     *         from its previous {@code float} value
     */
    private float df(final float eccentricAnomaly) {
        return 1 - eccentricity * (float) Math.cos(eccentricAnomaly);
    }

    /**
     * @param eccentricAnomaly {@code float}
     * @return {@code float} Newton-Raphson approximation of the eccentric
     *         anomaly, iteratively updated from its previous value
     */
    private float getNextEccentricAnomalyApproximation(final float eccentricAnomaly) {
        return eccentricAnomaly - f(eccentricAnomaly) / df(eccentricAnomaly);
    }

    /**
     * @return {@code float} <a href="https://en.wikipedia.org/wiki/Eccentric_anomaly">eccentric anomaly</a>
     * of the orbiting body on the patched conic orbit calculated by an
     * iterative <a href="https://en.wikipedia.org/wiki/Kepler%27s_equation#Numerical_approximation_of_inverse_problem">Newton-Raphson</a>
     * solution to <a href="https://en.wikipedia.org/wiki/Kepler%27s_equation">Kepler's Equation</a>,
     * or the <a href="https://en.wikipedia.org/wiki/Mean_anomaly">mean anomaly</a>
     * should {@value MAXIMUM_ITERATIONS} iterations of the Newton-Raphson
     * method yield no solution of error less than {@value #MAXIMUM_ERROR}.
     */
    private float getUpdatedEccentricAnomaly() {
        float iteration = 0,
                eccentricAnomaly = getInitialEccentricAnomalyApproximation(),
                error = f(eccentricAnomaly);
        while (iteration++ < MAXIMUM_ITERATIONS && error > MAXIMUM_ERROR) {
            eccentricAnomaly = getNextEccentricAnomalyApproximation(eccentricAnomaly);
            error = f(eccentricAnomaly);
        } return direction * eccentricAnomaly;
    }

    /**
     * @return {@code float} position along the x-axis of the orbiting body,
     *         calculated from its eccentric anomaly
     */
    private float getX() {
        return semiMajorAxis * ((float) Math.cos(eccentricAnomaly) - eccentricity);
    }

    /**
     * @return {@code float} position along the y-axis of the orbiting body,
     *         calculated from its eccentric anomaly
     */
    private float getY() {
        return semiMinorAxis * (float) Math.sin(eccentricAnomaly);
    }

    /**
     * @return {@link SectorEntityToken} orbited
     */
    @Override
    public SectorEntityToken getFocus() { return focus; }

    /**
     * Called by Starsector itself - you can ignore this.
     */
    @Override
    public void setEntity(final SectorEntityToken entity) { this.entity = entity; }

    /**
     * Called by Starsector itself - you can ignore this.
     */
    @Override
    public void advance(final float amount) {
        if (entity != null) {
            time += Global.getSector().getClock().convertToDays(amount);
            updateLocation();
        }
    }

    @Override
    public OrbitAPI makeCopy() {
        return new KeplerOrbit(focus, semiMajorAxis, semiMinorAxis, angle, period, clockwise);
    }

    @Override
    public Vector2f computeCurrentLocation() { return new Vector2f(getX(), getY()); }

    @Override
    public float getOrbitalPeriod() { return period; }

    @Override
    public void updateLocation() {
        try { eccentricAnomaly = getUpdatedEccentricAnomaly(); } catch (Throwable ignored) {
            try { eccentricAnomaly = getMeanAnomaly(); } catch (Throwable ignored1) {}
        } try { entity.getLocation().set(computeCurrentLocation()); } catch (Throwable ignored) {}
    }
}
