package org.lazywizard.lazylib.combat;

import com.fs.starfarer.api.combat.BoundsAPI;
import com.fs.starfarer.api.combat.CollisionClass;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.ShieldAPI;
import org.lwjgl.util.vector.Vector2f;

/**
 * Just a dump for the unimportant {@link CombatEntityAPI} methods to avoid
 * clutter in {@link FakeEntity}.
 *
 * @author LazyWizard
 * @since 1.4
 */
abstract class FakeEntityBase implements CombatEntityAPI
{
    /**
     * NONFUNCTIONAL/PLACEHOLDER
     */
    @Override
    public Vector2f getVelocity()
    {
        return new Vector2f(0, 0);
    }

    /**
     * NONFUNCTIONAL/PLACEHOLDER
     */
    @Override
    public float getFacing()
    {
        return 0;
    }

    /**
     * NONFUNCTIONAL/PLACEHOLDER
     */
    @Override
    public void setFacing(float facing)
    {
    }

    /**
     * NONFUNCTIONAL/PLACEHOLDER
     */
    @Override
    public float getAngularVelocity()
    {
        return 0f;
    }

    /**
     * NONFUNCTIONAL/PLACEHOLDER
     */
    @Override
    public void setAngularVelocity(float angVel)
    {
    }

    /**
     * NONFUNCTIONAL/PLACEHOLDER
     */
    @Override
    public int getOwner()
    {
        return 100;
    }

    /**
     * NONFUNCTIONAL/PLACEHOLDER
     */
    @Override
    public void setOwner(int owner)
    {
    }

    /**
     * NONFUNCTIONAL/PLACEHOLDER
     */
    @Override
    public float getCollisionRadius()
    {
        return 0f;
    }

    /**
     * NONFUNCTIONAL/PLACEHOLDER
     */
    @Override
    public CollisionClass getCollisionClass()
    {
        return CollisionClass.NONE;
    }

    /**
     * NONFUNCTIONAL/PLACEHOLDER
     */
    @Override
    public void setCollisionClass(CollisionClass collisionClass)
    {
    }

    /**
     * NONFUNCTIONAL/PLACEHOLDER
     */
    @Override
    public float getMass()
    {
        return 0f;
    }

    /**
     * NONFUNCTIONAL/PLACEHOLDER
     */
    @Override
    public void setMass(float mass)
    {
    }

    /**
     * NONFUNCTIONAL/PLACEHOLDER
     */
    @Override
    public BoundsAPI getExactBounds()
    {
        return null;
    }

    /**
     * NONFUNCTIONAL/PLACEHOLDER
     */
    @Override
    public ShieldAPI getShield()
    {
        return null;
    }

    /**
     * NONFUNCTIONAL/PLACEHOLDER
     */
    @Override
    public float getHullLevel()
    {
        return 1f;
    }

    /**
     * NONFUNCTIONAL/PLACEHOLDER
     */
    @Override
    public float getHitpoints()
    {
        return 1f;
    }

    /**
     * NONFUNCTIONAL/PLACEHOLDER
     */
    @Override
    public float getMaxHitpoints()
    {
        return 1f;
    }

    @Override
    public abstract Vector2f getLocation();
}
