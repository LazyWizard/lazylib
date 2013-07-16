package org.lazywizard.lazylib.combat.entities;

import com.fs.starfarer.api.combat.CombatEntityAPI;
import org.lazywizard.lazylib.MathUtils;
import org.lwjgl.util.vector.Vector2f;

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
     * @param location The location relative to {@code anchor} to track.
     * @param anchor The {@link CombatEntityAPI} to follow and rotate with.
     * @since 1.5
     */
    public AnchoredEntity(Vector2f location, CombatEntityAPI anchor)
    {
        relativeDistance = MathUtils.getDistance(anchor.getLocation(), location);
        relativeAngle = MathUtils.clampAngle(MathUtils.getAngle(
                anchor.getLocation(), location) - anchor.getFacing());
        this.anchor = anchor;
    }

    @Override
    public Vector2f getLocation()
    {
        return MathUtils.getPointOnCircumference(anchor.getLocation(),
                relativeDistance, relativeAngle + anchor.getFacing());
    }

    @Override
    public Vector2f getVelocity()
    {
        return anchor.getVelocity();
    }

    /**
     * Returns the {@link CombatEntityAPI} this entity is anchored to.
     *
     * @return The {@link CombatEntityAPI} this entity is anchored to.
     * @since 1.5
     */
    public CombatEntityAPI getAnchor()
    {
        return anchor;
    }
}
