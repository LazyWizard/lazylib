package org.lazywizard.lazylib.campaign;

import com.fs.starfarer.api.campaign.CargoAPI;
import com.fs.starfarer.api.campaign.CargoStackAPI;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;

/**
 * Contains methods for working with cargo and item stacks.
 *
 * @author LazyWizard
 * @since 1.0
 */
public class CargoUtils
{
    /**
     * Moves an entire {@link CargoStackAPI} from its current location to
     * another {@link CargoAPI}.
     *
     * @param stack The {@link CargoStackAPI} to be moved.
     * @param to    The destination {@link CargoAPI}.
     * <p>
     * @since 1.0
     */
    public static void moveStack(CargoStackAPI stack, CargoAPI to)
    {
        if (!stack.isNull() && stack.getCargo() != to)
        {
            to.addItems(stack.getType(), stack.getData(), stack.getSize());
            stack.getCargo().removeItems(stack.getType(),
                    stack.getData(), stack.getSize());
        }
    }

    /**
     * Moves an entire {@link CargoStackAPI} from its current location to the
     * {@link CargoAPI} of another {@link SectorEntityToken}.
     *
     * @param stack The {@link CargoStackAPI} to be moved.
     * @param to    The destination {@link SectorEntityToken}.
     * <p>
     * @since 1.2
     */
    public static void moveStack(CargoStackAPI stack, SectorEntityToken to)
    {
        if (to.getCargo() != null)
        {
            moveStack(stack, to.getCargo());
        }
    }

    /**
     * Moves all mothballed {@link FleetMemberAPI}s from one {@link CargoAPI} to
     * another.
     * <p>
     * @param from The {@link CargoAPI} to be emptied.
     * @param to   The destination {@link CargoAPI}.
     * <p>
     * @since 1.9
     */
    public static void moveMothballedShips(CargoAPI from, CargoAPI to)
    {
        for (FleetMemberAPI tmp : from.getMothballedShips().getMembersListCopy())
        {
            to.getMothballedShips().addFleetMember(tmp);
        }

        from.getMothballedShips().clear();
    }

    /**
     * Moves all {@link CargoStackAPI}s from one {@link CargoAPI} to another.
     *
     * @param from The {@link CargoAPI} to be emptied.
     * @param to   The destination {@link CargoAPI}.
     * <p>
     * @see CargoUtils#moveStack(com.fs.starfarer.api.campaign.CargoStackAPI,
     * com.fs.starfarer.api.campaign.CargoAPI)
     * @since 1.0
     */
    public static void moveCargo(CargoAPI from, CargoAPI to)
    {
        if (from == to)
        {
            return;
        }

        for (CargoStackAPI stack : from.getStacksCopy())
        {
            moveStack(stack, to);
        }
    }

    /**
     * Checks if a cargo contains a specific mothballed ship.
     * <p>
     * @param fleetMemberId The fleet member ID of the ship to check. This can
     *                      be retrieved with {@link ShipAPI#getFleetMemberId()}
     *                      or {@link FleetMemberAPI#getId()}.
     * @param cargo         The cargo to check for the presence of this ship in.
     * <p>
     * @return {@code true} if {@code cargo} contains a ship with this ID,
     *         {@code false} otherwise.
     * <p>
     * @since 2.0
     */
    public static boolean isShipInMothballed(String fleetMemberId, CargoAPI cargo)
    {
        if (fleetMemberId == null || fleetMemberId.isEmpty() || cargo == null)
        {
            return false;
        }

        for (FleetMemberAPI tmp : cargo.getMothballedShips().getMembersListCopy())
        {
            if (fleetMemberId.equals(tmp.getId()))
            {
                return true;
            }
        }

        return false;
    }

    /**
     * Returns the amount of space taken by weapons in a {@link CargoAPI}.
     *
     * @param cargo The {@link CargoAPI} to analyze.
     * <p>
     * @return The amount of space taken by weapons in {@code cargo}.
     * <p>
     * @since 1.0
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
     * @param cargo The {@link CargoAPI} to analyze.
     * <p>
     * @return The amount of space taken by crew in {@code cargo}.
     * <p>
     * @since 1.0
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
     * @param cargo The {@link CargoAPI} to analyze.
     * <p>
     * @return The amount of space taken by supplies in {@code cargo}.
     * <p>
     * @since 1.0
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
     * @param cargo The {@link CargoAPI} to analyze.
     * <p>
     * @return The amount of space taken by fuel in {@code cargo}.
     * <p>
     * @since 1.0
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
     * Returns the amount of space taken by all commodities in a {@link CargoAPI}.
     *
     * @param cargo The {@link CargoAPI} to analyze.
     * <p>
     * @return The amount of space taken by commodity stacks in {@code cargo}.
     * <p>
     * @since 1.0
     */
    public static float getSpaceTakenByCommodities(CargoAPI cargo)
    {
        float totalSpace = 0f;

        for (CargoStackAPI stack : cargo.getStacksCopy())
        {
            if (stack.isCommodityStack())
            {
                totalSpace += stack.getCargoSpace();
            }
        }

        return totalSpace;
    }

    /**
     * Returns the amount of space taken by all cargo types in a
     * {@link CargoAPI}.
     *
     * @param cargo The {@link CargoAPI} to analyze.
     * <p>
     * @return The amount of space used in {@code cargo}.
     * <p>
     * @since 1.2
     */
    public static float getSpaceTakenByCargo(CargoAPI cargo)
    {
        float totalSpace = 0f;

        for (CargoStackAPI stack : cargo.getStacksCopy())
        {
            if (!stack.isNull())
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
