package org.lazywizard.lazylib.combat;

import com.fs.starfarer.api.combat.CombatEntityAPI;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import org.lazywizard.lazylib.LazyLib;
import org.lwjgl.util.vector.Vector2f;

/**
 * A bare-bones implementation of {@link CombatEntityAPI}, mostly useful for EMP arcs.
 *
 * @author LazyWizard
 * @since 1.4
 */
public class FakeEntity extends FakeEntityBase
{
    private static final Map<Class, Method> methodCache = new HashMap();
    private Object toFollow;
    private Method getLocation;
    private Vector2f location = null;

    private FakeEntity()
    {
    }

    /**
     * Creates a {@code CombatEntityAPI} that mimics the location of an object
     * such as a {@link com.fs.starfarer.api.combat.WeaponAPI}.
     *
     * @param toFollow The {@link Object} to mimic the location of. This object
     * MUST have the method getLocation(), which MUST return a {@link Vector2f}!
     * @since 1.4
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
     * @since 1.4
     */
    public FakeEntity(Vector2f location)
    {
        this.location = location;
    }

    /**
     * Returns the location this {@link FakeEntity} is mimicking.
     *
     * @return The {@link Vector2f} passed in at creation, or the result of
     * getLocation() on the followed {@link Object}, depending on which constructor was used.
     * @since 1.4
     */
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
}
