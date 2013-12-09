package org.lazywizard.lazylib.combat.entities;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.WeaponAPI;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Level;
import org.lwjgl.util.vector.Vector2f;

/**
 * A bare-bones implementation of {@link CombatEntityAPI}, mostly useful for
 * decorative EMP arc targeting. These entities do not actually exist on the
 * battle map, so most other methods that take a {@link CombatEntityAPI} will
 * fail if you use one of these as a parameter.
 *
 * @author LazyWizard
 * @since 1.4
 */
public class SimpleEntity extends EntityBase
{
    // Cache for all reflection-based variants (reduces overhead on creation)
    private static final Map<Class, Method> methodCache = new HashMap<Class, Method>();
    private final SimpleEntityType type;
    // Variables for Vector2f-based variant
    private Vector2f location = null;
    // Variables for WeaponAPI-based variant
    private WeaponAPI weapon = null;
    // Variables for reflection-based variant
    private Object toFollow = null;
    private Method getLocation = null;

    /**
     * @since 1.7
     */
    public static enum SimpleEntityType
    {
        VECTOR,
        WEAPON,
        REFLECTION
    }

    private SimpleEntity()
    {
        type = null;
    }

    /**
     * Creates a {@link CombatEntityAPI} that stays in a single, predefined
     * location. This version is a simple wrapper around a {@link Vector2f}
     * and costs virtually nothing.
     *
     * @param location The {@link Vector2f} that getLocation() should return.
     * <p>
     * @since 1.4
     */
    public SimpleEntity(Vector2f location)
    {
        this.location = location;
        type = SimpleEntityType.VECTOR;
    }

    /**
     * Creates a {@code CombatEntityAPI} that mimics the location of a
     * {@link com.fs.starfarer.api.combat.WeaponAPI}.
     * <p>
     * @param weapon The {@link WeaponAPI} whose location getLocation() should
     *               return.
     * <p>
     * @since 1.7
     */
    public SimpleEntity(WeaponAPI weapon)
    {
        this.weapon = weapon;
        type = SimpleEntityType.WEAPON;
    }

    /**
     * Creates a {@code CombatEntityAPI} that mimics the location of another
     * object that contains a getLocation() method.
     *
     * @param toFollow The {@link Object} to mimic the location of. This object
     *                 MUST have the method getLocation(), which MUST return a
     *                 {@link Vector2f}!
     * <p>
     * @since 1.4
     */
    public SimpleEntity(Object toFollow)
    {
        this.toFollow = toFollow;
        Class tmp = toFollow.getClass();
        type = SimpleEntityType.REFLECTION;

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
            Global.getLogger(SimpleEntity.class).log(Level.DEBUG,
                    "SimpleEntity cached: "
                    + tmp.getCanonicalName() + " | "
                    + getLocation.toGenericString() + ".");
        }
    }

    /**
     * Returns the location this {@link SimpleEntity} is mimicking.
     *
     * @return The {@link Vector2f} passed in at creation or the result of
     *         getLocation() on the followed {@link Object}, depending on which
     *         constructor was used. Can return {@code null} if the object it
     *         is following is no longer on the battle map.
     * <p>
     * @since 1.4
     */
    @Override
    public Vector2f getLocation()
    {
        switch (type)
        {
            // Vector2f-based constructor
            case VECTOR:
            {
                return location;
            }
            // WeaponAPI-based constructor
            case WEAPON:
            {
                return weapon.getLocation();
            }
            // Reflection-based constructor (any other Object passed in)
            case REFLECTION:
            {
                try
                {
                    return (Vector2f) getLocation.invoke(toFollow);
                }
                catch (Exception ex)
                {
                    throw new RuntimeException(ex);
                }
            }
            default:
                return null;
        }
    }

    /**
     * Returns the {@link WeaponAPI} this entity is attached to, if any.
     *
     * @return The {@link WeaponAPI} passed into the constructor, or
     *         {@code null} if another constructor was used.
     * @since 1.7
     */
    public WeaponAPI getWeapon()
    {
        return weapon;
    }

    /**
     * Returns the {@link SimpleEntityType} corresponding to the constructor
     * used to create this object.
     *
     * @return The type of constructor used to create this entity.
     * <p>
     * @since 1.7
     */
    public SimpleEntityType getType()
    {
        return type;
    }
}
