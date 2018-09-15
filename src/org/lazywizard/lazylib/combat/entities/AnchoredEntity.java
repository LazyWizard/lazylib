package org.lazywizard.lazylib.combat.entities;

import com.fs.starfarer.api.combat.CombatEntityAPI;
import org.lazywizard.lazylib.MathUtils;
import org.lazywizard.lazylib.VectorUtils;
import org.lwjgl.util.vector.Vector2f;

/**
 * An implementation of {@link CombatEntityAPI} that follows and rotates
 * with another anchoring {@link CombatEntityAPI}.
 *
 * @author LazyWizard
 * @since 1.5
 */
public class AnchoredEntity extends EntityBase
{
    protected CombatEntityAPI anchor;
    protected float relativeDistance, relativeAngle;

    /**
     * Creates a {@code CombatEntityAPI} that follows and rotates with another
     * anchoring {@code CombatEntityAPI}.
     *
     * @param anchor   The {@link CombatEntityAPI} to follow and rotate with.
     * @param location The location relative to {@code anchor} to track.
     *
     * @since 1.5
     */
    public AnchoredEntity(CombatEntityAPI anchor, Vector2f location)
    {
        reanchor(anchor, location);
    }

    /**
     * Reorient this entity around a new anchor.
     *
     * @param newAnchor   The {@link CombatEntityAPI} to follow and rotate with.
     * @param newLocation The location relative to {@code anchor} to track.
     *
     * @since 1.8
     */
    public void reanchor(CombatEntityAPI newAnchor, Vector2f newLocation)
    {
        relativeDistance = MathUtils.getDistance(newAnchor.getLocation(), newLocation);
        relativeAngle = MathUtils.clampAngle(VectorUtils.getAngle(
                newAnchor.getLocation(), newLocation) - newAnchor.getFacing());
        anchor = newAnchor;
    }

    /**
     * Returns the location of this entity, calculated to be relative to
     * its anchor.
     *
     * @return The location of this entity.
     *
     * @since 1.5
     */
    @Override
    public Vector2f getLocation()
    {
        if (relativeDistance == 0f)
        {
            return new Vector2f(anchor.getLocation());
        }

        return MathUtils.getPointOnCircumference(anchor.getLocation(),
                relativeDistance, relativeAngle + anchor.getFacing());
    }

    /**
     * Returns the velocity of this entity's anchor (and by definition this entity's velocity as well).
     *
     * @return The velocity of this entity's anchor (a direct reference, so changes will propagate!).
     *
     * @since 1.5
     */
    @Override
    public Vector2f getVelocity()
    {
        return anchor.getVelocity();
    }

    /**
     * Returns the {@link CombatEntityAPI} this entity is anchored to.
     *
     * @return The {@link CombatEntityAPI} this entity is anchored to.
     *
     * @since 1.5
     */
    public CombatEntityAPI getAnchor()
    {
        return anchor;
    }
}
