package org.lazywizard.lazylib.campaign;

import com.fs.starfarer.api.campaign.CargoAPI;
import com.fs.starfarer.api.campaign.CargoStackAPI;

/**
 * Contains methods for working with cargo and item stacks.
 * @author LazyWizard
 */
public class CargoUtils
{
    /**
     * Moves an entire {@link CargoStackAPI} from its current location to another {@link CargoAPI}.
     *
     * @param stack The {@link CargoStackAPI} to be moved.
     * @param to The destination {@link CargoAPI}.
     */
    public static void moveStack(CargoStackAPI stack, CargoAPI to)
    {
        to.addItems(stack.getType(), stack.getData(), stack.getSize());
        stack.getCargo().removeItems(stack.getType(),
                stack.getData(), stack.getSize());
    }

    /**
     * Moves all {@link CargoStackAPI}s from one {@link CargoAPI} to another.
     *
     * @param from The {@link CargoAPI} to be emptied.
     * @param to The destination {@link CargoAPI}.
     * @see CargoUtils#moveStack(com.fs.starfarer.api.campaign.CargoStackAPI, com.fs.starfarer.api.campaign.CargoAPI)
     */
    public static void moveCargo(CargoAPI from, CargoAPI to)
    {
        for (CargoStackAPI stack : from.getStacksCopy())
        {
            moveStack(stack, to);
        }
    }

    /**
     * Returns the amount of space taken by weapons in a {@link CargoAPI}.
     *
     * @param cargo The [@link CargoAPI} to analyze.
     * @return The amount of space taken by weapons in {@code cargo}.
     */
    public static float getSpaceTakenByWeapons(CargoAPI cargo)
    {
        float totalSpace = 0f;

        for (CargoStackAPI stack : cargo.getStacksCopy())
        {
            if (stack.isWeaponStack())
            {
                totalSpace += stack.getCargoSpace();
            }
        }

        return totalSpace;
    }

    /**
     * Returns the amount of space taken by crew in a {@link CargoAPI}.
     *
     * @param cargo The [@link CargoAPI} to analyze.
     * @return The amount of space taken by crew in {@code cargo}.
     */
    public static float getSpaceTakenByCrew(CargoAPI cargo)
    {
        float totalSpace = 0f;

        for (CargoStackAPI stack : cargo.getStacksCopy())
        {
            if (stack.isCrewStack())
            {
                totalSpace += stack.getCargoSpace();
            }
        }

        return totalSpace;
    }

    /**
     * Returns the amount of space taken by supplies in a {@link CargoAPI}.
     *
     * @param cargo The [@link CargoAPI} to analyze.
     * @return The amount of space taken by supplies in {@code cargo}.
     */
    public static float getSpaceTakenBySupplies(CargoAPI cargo)
    {
        float totalSpace = 0f;

        for (CargoStackAPI stack : cargo.getStacksCopy())
        {
            if (stack.isSupplyStack())
            {
                totalSpace += stack.getCargoSpace();
            }
        }

        return totalSpace;
    }

    /**
     * Returns the amount of space taken by fuel in a {@link CargoAPI}.
     *
     * @param cargo The [@link CargoAPI} to analyze.
     * @return The amount of space taken by fuel in {@code cargo}.
     */
    public static float getSpaceTakenByFuel(CargoAPI cargo)
    {
        float totalSpace = 0f;

        for (CargoStackAPI stack : cargo.getStacksCopy())
        {
            if (stack.isFuelStack())
            {
                totalSpace += stack.getCargoSpace();
            }
        }

        return totalSpace;
    }

    /**
     * Returns the amount of space taken by all resources in a {@link CargoAPI}.
     *
     * @param cargo The [@link CargoAPI} to analyze.
     * @return The amount of space taken by resource stacks in {@code cargo}.
     */
    public static float getSpaceTakenByResources(CargoAPI cargo)
    {
        float totalSpace = 0f;

        for (CargoStackAPI stack : cargo.getStacksCopy())
        {
            if (stack.isResourceStack())
            {
                totalSpace += stack.getCargoSpace();
            }
        }

        return totalSpace;
    }

    private CargoUtils()
    {
    }
}
