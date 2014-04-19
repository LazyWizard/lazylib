package org.lazywizard.lazylib.campaign.orbits;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.OrbitAPI;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import org.lazywizard.lazylib.MathUtils;

/**
 * Represents an elliptical orbit path.
 *
 * @author LazyWizard
 * @since 1.9
 */
public class EllipticalOrbit implements OrbitAPI
{
    private final SectorEntityToken focus;
    private final float orbitAngle, orbitWidth, orbitHeight, orbitSpeed;
    private SectorEntityToken entity;
    private float currentAngle;

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
     */
    public EllipticalOrbit(SectorEntityToken focus, float startAngle,
            float orbitWidth, float orbitHeight, float orbitAngle, float daysPerOrbit)
    {
        this.focus = focus;
        this.orbitWidth = orbitWidth;
        this.orbitHeight = orbitHeight;
        this.orbitAngle = (float) Math.toRadians(orbitAngle);
        this.orbitSpeed = 360f / daysPerOrbit;
        currentAngle = startAngle;
        //runcode StarSystemAPI system = (StarSystemAPI) Global.getSector().getCurrentLocation(); system.getEntityByName("Orbital Station").setOrbit(new org.lazywizard.lazylib.campaign.orbits.EllipticalOrbit(system.getEntityByName("Corvus II"), 90f, 400f, 900f, 45f, 1f));
    }

    /**
     * Gets the object we are orbiting.
     *
     * @return The {@link SectorEntityToken} we are orbiting around.
     */
    @Override
    public SectorEntityToken getFocus()
    {
        return focus;
    }

    /**
     * Explicitly sets where along our orbital path we should be.
     * <p>
     * @param angle The angle (in degrees) along the orbital path we should be
     *              moved to.
     */
    public void setAngle(float angle)
    {
        currentAngle = angle;

        // Don't bother with these calculations if there's no object to move!
        if (entity == null)
        {
            return;
        }

        entity.getLocation().set(MathUtils.getPointOnEllipse(focus.getLocation(),
                orbitWidth, orbitHeight, orbitAngle, angle));
    }

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

    @Override
    public void setEntity(SectorEntityToken entity)
    {
        this.entity = entity;
        setAngle(currentAngle);
    }
}
