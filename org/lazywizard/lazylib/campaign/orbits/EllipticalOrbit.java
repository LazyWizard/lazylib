package org.lazywizard.lazylib.campaign.orbits;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.OrbitAPI;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import org.lazywizard.lazylib.FastTrig;
import org.lazywizard.lazylib.MathUtils;

/**
 * Represents an elliptical orbit path.
 *
 * @author LazyWizard
 * @since 1.9
 */
public class EllipticalOrbit implements OrbitAPI
{
    protected final SectorEntityToken focus;
    protected final float orbitAngle, orbitWidth, orbitHeight, orbitSpeed,
            offsetSin, offsetCos;
    protected SectorEntityToken entity;
    protected float currentAngle;

    /**
     * Creates an elliptical orbit around a focus object.
     *
     * @param focus        What to orbit around.
     * @param startAngle   The angle (in degrees) that the orbit will begin at.
     *                     0 degrees = right - this is not relative to
     *                     {@code orbitAngle}.
     * @param orbitWidth   The width of the ellipse that makes up the orbital
     *                     path.
     * @param orbitHeight  The height of the ellipse that makes up the orbital
     *                     path.
     * @param orbitAngle   The angular offset of the ellipse that makes up the
     *                     orbital path.
     * @param daysPerOrbit How long it should take for us to make one orbit
     *                     around {@code focus}.
     * <p>
     * @since 1.9
     */
    public EllipticalOrbit(SectorEntityToken focus, float startAngle,
            float orbitWidth, float orbitHeight, float orbitAngle, float daysPerOrbit)
    {
        this.focus = focus;
        this.orbitWidth = orbitWidth;
        this.orbitHeight = orbitHeight;
        this.orbitAngle = orbitAngle;
        this.orbitSpeed = 360f / daysPerOrbit;
        double rad = Math.toRadians(orbitAngle);
        offsetSin = (float) Math.sin(rad);
        offsetCos = (float) Math.cos(rad);
        setAngle(startAngle);
        //runcode StarSystemAPI system = (StarSystemAPI) Global.getSector().getCurrentLocation(); system.getEntityByName("Orbital Station").setOrbit(new org.lazywizard.lazylib.campaign.orbits.EllipticalOrbit(system.getEntityByName("Corvus II"), 90f, 400f, 900f, 45f, 1f));
    }

    /**
     * Returns the current angle along the orbital path of the orbiting entity.
     * <p>
     * @return The angle of the current position along the elliptical path.
     * <p>
     * @since 1.9
     */
    public float getAngle()
    {
        return currentAngle;
    }

    /**
     * Returns the width of the ellipsis used as a path.
     * <p>
     * @return The width of the ellipsis in su.
     * <p>
     * @since 1.9
     */
    public float getOrbitWidth()
    {
        return orbitWidth;
    }

    /**
     * Returns the height of the ellipsis used as a path.
     * <p>
     * @return The height of the ellipsis in su.
     * <p>
     * @since 1.9
     */
    public float getOrbitHeight()
    {
        return orbitHeight;
    }

    /**
     * Returns the offset angle of the ellipsis used as a path.
     * <p>
     * @return The offset of the ellipsis used as a path, in degrees.
     * <p>
     * @since 1.9
     */
    public float getOrbitAngle()
    {
        return orbitAngle;
    }

    /**
     * Explicitly sets where along our orbital path we should be.
     * <p>
     * @param angle The angle (in degrees) along the orbital path we should be
     *              moved to.
     * <p>
     * @since 1.9
     */
    public void setAngle(float angle)
    {
        currentAngle = angle;

        // Don't bother with these calculations if there's no object to move!
        if (entity == null)
        {
            return;
        }

        angle = (float) Math.toRadians(angle);

        // Get point on unrotated ellipse around origin (0, 0)
        final float x = orbitWidth * (float) FastTrig.cos(angle);
        final float y = orbitHeight * (float) FastTrig.sin(angle);

        // Rotate point to match ellipses rotation and translate back to center
        entity.getLocation().set((x * offsetCos) - (y * offsetSin) + focus.getLocation().x,
                (x * offsetSin) + (y * offsetCos) + focus.getLocation().y);
    }

    /**
     * Returns the object we are orbiting.
     *
     * @return The {@link SectorEntityToken} we are orbiting around.
     */
    @Override
    public SectorEntityToken getFocus()
    {
        return focus;
    }

    /**
     * Called by Starsector itself - you can ignore this.
     */
    @Override
    public void setEntity(SectorEntityToken entity)
    {
        this.entity = entity;
        setAngle(currentAngle);
    }

    /**
     * Called by Starsector itself - you can ignore this.
     */
    @Override
    public void advance(float amount)
    {
        if (entity == null)
        {
            return;
        }

        // Advance rotation
        setAngle(MathUtils.clampAngle(currentAngle + (orbitSpeed
                * Global.getSector().getClock().convertToDays(amount))));
    }
}
