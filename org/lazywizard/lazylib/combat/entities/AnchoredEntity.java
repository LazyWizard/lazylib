package org.lazywizard.lazylib.combat.entities;

import com.fs.starfarer.api.combat.CombatEntityAPI;
import org.lazywizard.lazylib.MathUtils;
import org.lwjgl.util.vector.Vector2f;

/**
 * An implementation of {@code CombatEntityAPI} that follows and rotates
 * with another anchoring {@code CombatEntityAPI}.
 *
 * @author LazyWizard
 * @since 1.5
 */
public class AnchoredEntity extends EntityBase
{
    private CombatEntityAPI anchor;
    private float relativeDistance, relativeAngle;

    private AnchoredEntity()
    {
    }

    /**
     * Creates a {@code CombatEntityAPI} that follows and rotates with another
     * anchoring {@code CombatEntityAPI}.
     *
     * @param anchor   The {@link CombatEntityAPI} to follow and rotate with.
     * @param location The location relative to {@code anchor} to track.
     * <p>
     * @since 1.5
     */
    public AnchoredEntity(CombatEntityAPI anchor, Vector2f location)
    {
        relativeDistance = MathUtils.getDistance(anchor.getLocation(), location);
        relativeAngle = MathUtils.clampAngle(MathUtils.getAngle(
                anchor.getLocation(), location) - anchor.getFacing());
        this.anchor = anchor;
    }

    /**
     * Returns the location of this entity, calculated to be relative to
     * its anchor.
     *
     * @return The location of this entity.
     * <p>
     * @since 1.5
     */
    @Override
    public Vector2f getLocation()
    {
        return MathUtils.getPointOnCircumference(anchor.getLocation(),
                relativeDistance, relativeAngle + anchor.getFacing());
    }

    /**
     * Returns this entity's velocity.
     *
     * @return The velocity of this entity (will be the same as its anchor).
     * <p>
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
     * <p>
     * @since 1.5
     */
    public CombatEntityAPI getAnchor()
    {
        return anchor;
    }
}
