package org.lazywizard.lazylib.campaign;

import com.fs.starfarer.api.campaign.CargoAPI;
import com.fs.starfarer.api.campaign.CargoStackAPI;

public class CargoUtils
{
    public static void moveStack(CargoStackAPI stack, CargoAPI to)
    {
        to.addItems(stack.getType(), stack.getData(), stack.getSize());
        stack.getCargo().removeItems(stack.getType(),
                stack.getData(), stack.getSize());
    }

    public static void moveCargo(CargoAPI from, CargoAPI to)
    {
        for (CargoStackAPI stack : from.getStacksCopy())
        {
            moveStack(stack, to);
        }
    }

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
