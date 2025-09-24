package org.lazywizard.lazylib.combat.entities;

import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.ShipEngineControllerAPI.ShipEngineAPI;
import com.fs.starfarer.api.combat.WeaponAPI;
import org.jetbrains.annotations.Nullable;
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
    protected final SimpleEntityType type;
    // Variables for Vector2f-based variant
    protected Vector2f location = null;
    // Variables for WeaponAPI-based variant
    protected WeaponAPI weapon = null;
    // Variables for ShipEngineAPI-based variant
    protected ShipEngineAPI engine = null;

    /**
     * @since 1.7
     */
    public enum SimpleEntityType
    {
        VECTOR,
        WEAPON,
        ENGINE
    }

    /**
     * Creates a {@link CombatEntityAPI} that stays in a single, predefined
     * location. This version is a simple wrapper around a {@link Vector2f}
     * and costs virtually nothing.
     *
     * @param location The {@link Vector2f} that getLocation() should return.
     *
     * @since 1.4
     */
    public SimpleEntity(Vector2f location)
    {
        this.location = new Vector2f(location);
        type = SimpleEntityType.VECTOR;
    }

    /**
     * Creates a {@code CombatEntityAPI} that mimics the location of a
     * {@link com.fs.starfarer.api.combat.WeaponAPI}.
     *
     * @param weapon The {@link WeaponAPI} whose location getLocation() should
     *               return.
     *
     * @since 1.7
     */
    public SimpleEntity(WeaponAPI weapon)
    {
        this.weapon = weapon;
        type = SimpleEntityType.WEAPON;
    }

    /**
     * Creates a {@code CombatEntityAPI} that mimics the location of a
     * {@link com.fs.starfarer.api.combat.ShipEngineControllerAPI.ShipEngineAPI}.
     *
     * @param engine The {@link ShipEngineAPI} whose location getLocation()
     *               should return.
     *
     * @since 1.9b
     */
    public SimpleEntity(ShipEngineAPI engine)
    {
        this.engine = engine;
        type = SimpleEntityType.ENGINE;
    }

    /**
     * Returns the location this {@link SimpleEntity} is mimicking.
     *
     * @return The {@link Vector2f} passed in at creation or the result of
     *         getLocation() on the followed {@link Object}, depending on which
     *         constructor was used. Can return {@code null} if the object it
     *         is following is no longer on the battle map.
     *
     * @since 1.4
     */
    @Override
    public Vector2f getLocation()
    {
        return switch (type)
        {
            // Vector2f-based constructor
            case VECTOR -> location;
            // WeaponAPI-based constructor
            case WEAPON -> weapon.getLocation();
            // ShipEngineAPI-based constructor
            case ENGINE -> engine.getLocation();
        };
    }

    /**
     * Returns the {@link WeaponAPI} this entity is attached to, if any.
     *
     * @return The {@link WeaponAPI} passed into the constructor, or
     *         {@code null} if another constructor was used.
     *
     * @since 1.7
     */
    @Nullable
    public WeaponAPI getWeapon()
    {
        return weapon;
    }

    /**
     * Returns the {@link ShipEngineAPI} this entity is attached to, if any.
     *
     * @return The {@link ShipEngineAPI} passed into the constructor, or
     *         {@code null} if another constructor was used.
     *
     * @since 1.9b
     */
    @Nullable
    public ShipEngineAPI getEngine()
    {
        return engine;
    }

    /**
     * Returns the {@link SimpleEntityType} corresponding to the constructor
     * used to create this object.
     *
     * @return The type of constructor used to create this entity.
     *
     * @since 1.7
     */
    public SimpleEntityType getType()
    {
        return type;
    }
}
