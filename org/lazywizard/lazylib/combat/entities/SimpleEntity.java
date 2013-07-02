package org.lazywizard.lazylib.combat.entities;

import com.fs.starfarer.api.combat.CombatEntityAPI;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import org.lazywizard.lazylib.LazyLib;
import org.lazywizard.lazylib.MathUtils;
import org.lwjgl.util.vector.Vector2f;

/**
 * A bare-bones implementation of {@link CombatEntityAPI}, mostly useful for EMP arcs.
 *
 * @author LazyWizard
 * @since 1.4
 */
public class SimpleEntity extends SimpleEntityBase
{
    // Cache for all reflection-based variants (reduces overhead on creation)
    private static final Map<Class, Method> methodCache = new HashMap();
    // Variables for anchor-based variant
    private CombatEntityAPI anchor;
    private float relativeDistance, relativeAngle;
    // Variables for reflection-based variant
    private Object toFollow;
    private Method getLocation;
    // Variables for Vector2f-based variant
    private Vector2f location = null;

    private SimpleEntity()
    {
    }

    /**
     * Creates a {@code CombatEntityAPI} that follows and rotates another
     * anchoring {@code CombatEntityAPI}.
     *
     * @param location The location relative to {@code anchor} to track.
     * @param anchor The {@link CombatEntityAPI} to follow and rotate with.
     * @since 1.5
     */
    public SimpleEntity(Vector2f location, CombatEntityAPI anchor)
    {
        relativeDistance = MathUtils.getDistance(anchor.getLocation(), location);
        relativeAngle = MathUtils.clampAngle(MathUtils.getAngle(
                anchor.getLocation(), location) - anchor.getFacing());
        this.anchor = anchor;
    }

    /**
     * Creates a {@code CombatEntityAPI} that mimics the location of an object
     * such as a {@link com.fs.starfarer.api.combat.WeaponAPI}.
     *
     * @param toFollow The {@link Object} to mimic the location of. This object
     * MUST have the method getLocation(), which MUST return a {@link Vector2f}!
     * @since 1.4
     */
    public SimpleEntity(Object toFollow)
    {
        this.toFollow = toFollow;
        Class tmp = toFollow.getClass();

        // Check if the method has already been found for this object type
        if (methodCache.containsKey(tmp))
        {
            getLocation = methodCache.get(tmp);
        }
        // If not, we've got some reflection-heavy work ahead of us!
        else
        {
            // Check if this object has a getLocation() method
            try
            {
                getLocation = toFollow.getClass().getMethod("getLocation",
                        (Class<?>[]) null);

                // Check if getLocation() returns a Vector2f
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

            // Cache this method so we only have to do the lookup once a session
            methodCache.put(tmp, getLocation);

            if (LazyLib.isDevBuild())
            {
                System.out.println("SimpleEntity cached: "
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
    public SimpleEntity(Vector2f location)
    {
        this.location = location;
    }

    /**
     * Returns the location this {@link SimpleEntity} is mimicking.
     *
     * @return The {@link Vector2f} passed in at creation, a location near this
     * entity's anchor, or the result of getLocation() on the followed
     * {@link Object}, depending on which constructor was used.
     * @since 1.4
     */
    @Override
    public Vector2f getLocation()
    {
        // Anchor-based constructor
        if (anchor != null)
        {
            return MathUtils.getPointOnCircumference(anchor.getLocation(),
                    relativeDistance, relativeAngle + anchor.getFacing());
        }

        // Vector2f-based constructor
        if (location != null)
        {
            return location;
        }

        // Reflection-based constructor
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
