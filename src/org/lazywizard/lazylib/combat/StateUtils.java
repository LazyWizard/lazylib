package org.lazywizard.lazylib.combat;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.fleet.FleetMemberType;
import com.fs.starfarer.api.mission.FleetSide;
import com.fs.starfarer.api.util.Misc;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.Nullable;
import org.lazywizard.lazylib.MathUtils;
import org.lazywizard.lazylib.VectorUtils;
import org.lwjgl.util.vector.Vector2f;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Contains methods that check with the states of a single ship or other entity in general,
 * rather than *large* battle map. These methods assume that you are in combat
 * ({@code Global.getCombatEngine() != null}).
 *
 * @author
 * @see Misc The Misc class provided by vanilla for a large number of useful utility methods.
 * @since
 */
public class StateUtils
{
    /**
     * Check if a {@link ShipAPI} consumed its peak performance time
     * and should losing CR when facing with any significant enemy.
     *
     * @param ship    The {@link ShipAPI} to check.
     * 
     * @return {@code true} if the ship consumed its peak performance time, {@code false} otherwise.
     *
     * @since
     */
    public static boolean mayLosingCR(ShipAPI ship) {
        if (!ship.losesCRDuringCombat()) return false;
        return ship.getTimeDeployedForCRReduction() >= ship.getMutableStats().getPeakCRDuration().computeEffective(ship.getHullSpec().getNoCRLossTime());
    }

    /**
     * Check if a {@link CombatEntityAPI}'s owner equals Misc.OWNER_PLAYER,
     * which means it is controlled by player side(green) or ally side(yellow)
     * @see Misc
     *
     * @param entity    The {@link CombatEntityAPI} to check.
     * 
     * @return {@code true} if entity's owner equals Misc.OWNER_PLAYER, {@code false} otherwise.
     *
     * @since
     */
    public static boolean isPlayerOrAllyOwner(CombatEntityAPI entity) {
        return entity.getOwner() == Misc.OWNER_PLAYER;
    }

    /**
     * Check if a {@link ShipAPI}'s owner equals Misc.OWNER_PLAYER, and is not ally
     * which means it is controlled by player side(green), and not the ally side(yellow)
     * @see Misc
     *
     * @param ship    The {@link ShipAPI} to check.
     * 
     * @return {@code true} if entity's owner equals Misc.OWNER_PLAYER and is not ally, {@code false} otherwise.
     *
     * @since
     */
    public static boolean isExactlyPlayerOwner(ShipAPI ship) {
        return isPlayerOrAllyOwner(ship) && !ship.isAlly();
    }
}