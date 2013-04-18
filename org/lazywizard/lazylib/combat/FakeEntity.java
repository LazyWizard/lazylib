package org.lazywizard.lazylib.combat;

import com.fs.starfarer.api.combat.BoundsAPI;
import com.fs.starfarer.api.combat.CollisionClass;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.ShieldAPI;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import org.lazywizard.lazylib.LazyLib;
import org.lwjgl.util.vector.Vector2f;

public class FakeEntity implements CombatEntityAPI
{
    private static final Map<Class, Method> methodCache = new HashMap();
    private Object toFollow;
    private Method getLocation;
    private Vector2f location = null;

    private FakeEntity()
    {
    }

    /**
     * Creates a {@code CombatEntityAPI} that mimics the location of an object such as a {@link com.fs.starfarer.api.combat.WeaponAPI}.
     *
     * @param toFollow The {@link Object} to mimic the location of. This object MUST have the method getLocation(), which MUST return a {@link Vector2f}!
     */
    public FakeEntity(Object toFollow)
    {
        this.toFollow = toFollow;
        Class tmp = toFollow.getClass();

        if (methodCache.containsKey(tmp))
        {
            getLocation = methodCache.get(tmp);
        }
        else
        {
            try
            {
                getLocation = toFollow.getClass().getMethod("getLocation", null);

                if (getLocation.getReturnType() != Vector2f.class)
                {
                    throw new RuntimeException("Class "
                            + toFollow.getClass().getSimpleName()
                            + "'s getLocation() does not return a Vector2f!");
                }
            }
            catch (NoSuchMethodException ex)
            {
                throw new RuntimeException("Class "
                        + toFollow.getClass().getSimpleName()
                        + " does not implement getLocation()!");
            }

            methodCache.put(tmp, getLocation);

            if (LazyLib.isDevBuild())
            {
                System.out.println("FakeEntity cached: "
                        + tmp.getCanonicalName() + " | "
                        + getLocation.toGenericString() + ".");
            }
        }
    }

    /**
     * Creates a {@link CombatEntityAPI} that stays in a single, predefined location.
     *
     * @param location
     */
    public FakeEntity(Vector2f location)
    {
        this.location = location;
    }

    @Override
    public Vector2f getLocation()
    {
        if (location != null)
        {
            return location;
        }

        try
        {
            return (Vector2f) getLocation.invoke(toFollow);
        }
        catch (Exception ex)
        {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Vector2f getVelocity()
    {
        return new Vector2f(0, 0);
    }

    @Override
    public float getFacing()
    {
        return 0;
    }

    @Override
    public void setFacing(float facing)
    {
    }

    @Override
    public float getAngularVelocity()
    {
        return 0f;
    }

    @Override
    public void setAngularVelocity(float angVel)
    {
    }

    @Override
    public int getOwner()
    {
        return 100;
    }

    @Override
    public void setOwner(int owner)
    {
    }

    @Override
    public float getCollisionRadius()
    {
        return 0f;
    }

    @Override
    public CollisionClass getCollisionClass()
    {
        return CollisionClass.NONE;
    }

    @Override
    public void setCollisionClass(CollisionClass collisionClass)
    {
    }

    @Override
    public float getMass()
    {
        return 0f;
    }

    @Override
    public void setMass(float mass)
    {
    }

    @Override
    public BoundsAPI getExactBounds()
    {
        return null;
    }

    @Override
    public ShieldAPI getShield()
    {
        return null;
    }

    @Override
    public float getHullLevel()
    {
        return 1f;
    }

    @Override
    public float getHitpoints()
    {
        return 1f;
    }

    @Override
    public float getMaxHitpoints()
    {
        return 1f;
    }
}
