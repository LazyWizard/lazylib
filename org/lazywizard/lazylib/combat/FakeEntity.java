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

/**
 * A bare-bones implementation of {@link CombatEntityAPI}, mostly useful for EMP arcs.
 *
 * @author LazyWizard
 */
public class FakeEntity implements CombatEntityAPI
{
    private static final Map<Class, Method> methodCache = new HashMap();
    private Object toFollow;
    private Method getLocation;
    private Vector2f location = null;

    /*public static void main(String[] args)
    {
        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
        int numTests = 10000000;
        long startTime, endTime;
        Vector2f vec1;
        FakeEntity fe1, fe2;
        double totalTime, total1, total2, total3;
        Object obj = new Object()
        {
            private Vector2f vec2 = new Vector2f(5f, 25f);

            public Vector2f getLocation()
            {
                return vec2;
            }
        };

        java.util.DecimalFormat format = new java.util.DecimalFormat("###,##0.00000000000");
        System.out.println("Running "
                + DecimalFormat.getNumberInstance().format(numTests)
                + " tests at max priority:");

        startTime = System.nanoTime();
        vec1 = new Vector2f(5f, 25f);
        fe1 = new FakeEntity(vec1);
        for (int x = 0; x < numTests; x++)
        {
            fe1.getLocation();
        }
        endTime = System.nanoTime();
        totalTime = (double) (endTime - startTime) / 1000000000d;
        total1 = totalTime;
        System.out.println("Time taken (Vector2f):           "
                + format.format(totalTime) + " seconds (" + format.format(
                totalTime / (double) numTests) + " avg).");

        startTime = System.nanoTime();
        fe2 = new FakeEntity(obj);
        for (int x = 0; x < numTests; x++)
        {
            fe2.getLocation();
        }
        endTime = System.nanoTime();
        totalTime = (double) (endTime - startTime) / 1000000000d;
        total2 = totalTime;
        System.out.println("Time taken (FakeEntity cached):  "
                + format.format(totalTime) + " seconds (" + format.format(
                totalTime / (double) numTests) + " avg).");

        startTime = System.nanoTime();
        for (int x = 0; x < numTests; x++)
        {
            try
            {
                fe2.getClass().getMethod("getLocation", (Class<?>[]) null).invoke(fe2);
            }
            catch (Exception ex)
            {
                throw new RuntimeException(ex);
            }
        }
        endTime = System.nanoTime();
        totalTime = (double) (endTime - startTime) / 1000000000d;
        total3 = totalTime;
        System.out.println("Time taken (FakeEntity nocache): "
                + format.format(totalTime) + " seconds (" + format.format(
                totalTime / (double) numTests) + " avg).");

        System.out.println("\nFinal report:\nFakeEntity with cache is "
                + (int) (total2 / total1) + " times slower than Vector2f.\n"
                + "FakeEntity without cache is " + (int) ((total3 / total1) + .5)
                + " times slower than Vector2f.\nCached FakeEntity is "
                + (int) ((total3 / total2) + .5) + " times faster than uncached.");
    }*/

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
                getLocation = toFollow.getClass().getMethod("getLocation",
                        (Class<?>[]) null);

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
