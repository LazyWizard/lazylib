/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lazywizard.lazylib.campaign.orbits;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.OrbitAPI;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import org.lazywizard.lazylib.FastTrig;
import org.lazywizard.lazylib.MathUtils;
import org.lwjgl.util.vector.Vector2f;

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

        angle = (float) Math.toRadians(angle);
        float sin = (float) FastTrig.sin(angle),
                cos = (float) FastTrig.cos(angle);

        // Get point on unrotated ellipse around origin (0, 0)
        final float x = orbitWidth * cos;
        final float y = orbitHeight * sin;

        // Rotate to match actual rotated elliptical path
        sin = (float) FastTrig.sin(orbitAngle);
        cos = (float) FastTrig.cos(orbitAngle);
        Vector2f newLoc = new Vector2f((x * cos) - (y * sin),
                (x * sin) + (y * cos));

        // Translate from origin to final location
        Vector2f.add(newLoc, focus.getLocation(), newLoc);
        entity.getLocation().set(newLoc);
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
