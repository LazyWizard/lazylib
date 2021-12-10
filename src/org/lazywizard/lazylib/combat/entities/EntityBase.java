package org.lazywizard.lazylib.combat.entities;

import com.fs.starfarer.api.combat.BoundsAPI;
import com.fs.starfarer.api.combat.CollisionClass;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.ShieldAPI;
import org.lwjgl.util.vector.Vector2f;

import java.util.Map;

/**
 * Contains basic implementations of {@link CombatEntityAPI}'s methods to avoid
 * clutter in {@link SimpleEntity} and {@link AnchoredEntity}.
 *
 * @author LazyWizard
 * @since 1.4
 */
class EntityBase implements CombatEntityAPI
{
    /**
     * <small><i>NONFUNCTIONAL/PLACEHOLDER</i></small>
     */
    @Override
    public Vector2f getLocation()
    {
        return new Vector2f(0f, 0f);
    }

    /**
     * <small><i>NONFUNCTIONAL/PLACEHOLDER</i></small>
     */
    @Override
    public Vector2f getVelocity()
    {
        return new Vector2f(0f, 0f);
    }

    /**
     * <small><i>NONFUNCTIONAL/PLACEHOLDER</i></small>
     */
    @Override
    public float getFacing()
    {
        return 0;
    }

    /**
     * <small><i>NONFUNCTIONAL/PLACEHOLDER</i></small>
     */
    @Override
    public void setFacing(float facing)
    {
    }

    /**
     * <small><i>NONFUNCTIONAL/PLACEHOLDER</i></small>
     */
    @Override
    public float getAngularVelocity()
    {
        return 0f;
    }

    /**
     * <small><i>NONFUNCTIONAL/PLACEHOLDER</i></small>
     */
    @Override
    public void setAngularVelocity(float angVel)
    {
    }

    /**
     * <small><i>NONFUNCTIONAL/PLACEHOLDER</i></small>
     */
    @Override
    public int getOwner()
    {
        return 100;
    }

    /**
     * <small><i>NONFUNCTIONAL/PLACEHOLDER</i></small>
     */
    @Override
    public void setOwner(int owner)
    {
    }

    /**
     * <small><i>NONFUNCTIONAL/PLACEHOLDER</i></small>
     */
    @Override
    public float getCollisionRadius()
    {
        return 0f;
    }

    /**
     * <small><i>NONFUNCTIONAL/PLACEHOLDER</i></small>
     */
    @Override
    public CollisionClass getCollisionClass()
    {
        return CollisionClass.NONE;
    }

    /**
     * <small><i>NONFUNCTIONAL/PLACEHOLDER</i></small>
     */
    @Override
    public void setCollisionClass(CollisionClass collisionClass)
    {
    }

    /**
     * <small><i>NONFUNCTIONAL/PLACEHOLDER</i></small>
     */
    @Override
    public float getMass()
    {
        return 0f;
    }

    /**
     * <small><i>NONFUNCTIONAL/PLACEHOLDER</i></small>
     */
    @Override
    public void setMass(float mass)
    {
    }

    /**
     * <small><i>NONFUNCTIONAL/PLACEHOLDER</i></small>
     */
    @Override
    public BoundsAPI getExactBounds()
    {
        return null;
    }

    /**
     * <small><i>NONFUNCTIONAL/PLACEHOLDER</i></small>
     */
    @Override
    public ShieldAPI getShield()
    {
        return null;
    }

    /**
     * <small><i>NONFUNCTIONAL/PLACEHOLDER</i></small>
     */
    @Override
    public float getHullLevel()
    {
        return 1f;
    }

    /**
     * <small><i>NONFUNCTIONAL/PLACEHOLDER</i></small>
     */
    @Override
    public float getHitpoints()
    {
        return 1f;
    }

    /**
     * <small><i>NONFUNCTIONAL/PLACEHOLDER</i></small>
     */
    @Override
    public float getMaxHitpoints()
    {
        return 1f;
    }

    /**
     * <small><i>NONFUNCTIONAL/PLACEHOLDER</i></small>
     */
    @Override
    public void setCollisionRadius(float arg0)
    {
    }

    /**
     * <small><i>NONFUNCTIONAL/PLACEHOLDER</i></small>
     */
    @Override
    public Object getAI()
    {
        return null;
    }

    /**
     * <small><i>NONFUNCTIONAL/PLACEHOLDER</i></small>
     */
    @Override
    public boolean isExpired()
    {
        return false;
    }

    /**
     * <small><i>NONFUNCTIONAL/PLACEHOLDER</i></small>
     */
    @Override
    public void setCustomData(String s, Object o)
    {

    }

    /**
     * <small><i>NONFUNCTIONAL/PLACEHOLDER</i></small>
     */
    @Override
    public void removeCustomData(String s)
    {
    }

    /**
     * <small><i>NONFUNCTIONAL/PLACEHOLDER</i></small>
     */
    @Override
    public Map<String, Object> getCustomData()
    {
        return null;
    }

    /**
     * <small><i>NONFUNCTIONAL/PLACEHOLDER</i></small>
     */
    @Override
    public void setHitpoints(float hitpoints)
    {
    }
}
