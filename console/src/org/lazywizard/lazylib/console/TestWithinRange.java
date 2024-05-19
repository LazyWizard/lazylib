package org.lazywizard.lazylib.console;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.input.InputEventAPI;
import org.lazywizard.console.BaseCommand;
import org.lazywizard.console.CommonStrings;
import org.lazywizard.console.Console;
import org.lazywizard.lazylib.MathUtils;
import org.lwjgl.util.vector.Vector2f;

import java.awt.Color;
import java.util.*;

public class TestWithinRange implements BaseCommand
{
    @Override
    public CommandResult runCommand(String args, CommandContext context)
    {
        if (!context.isInCombat())
        {
            Console.showMessage(CommonStrings.ERROR_COMBAT_ONLY);
            return CommandResult.WRONG_CONTEXT;
        }

        // Do stuff here
        Global.getCombatEngine().addPlugin(new TestWithinRangePlugin());
        return CommandResult.SUCCESS;
    }

    public static List<CombatEntityAPI> getEntitiesWithinRangeOld(Vector2f location, float range)
    {
        List<CombatEntityAPI> entities = new ArrayList<>();

        for (CombatEntityAPI tmp : Global.getCombatEngine().getShips())
        {
            if (MathUtils.isWithinRange(tmp.getLocation(), location, range))
            {
                entities.add(tmp);
            }
        }

        // This also includes missiles
        for (CombatEntityAPI tmp : Global.getCombatEngine().getProjectiles())
        {
            if (MathUtils.isWithinRange(tmp.getLocation(), location, range))
            {
                entities.add(tmp);
            }
        }

        for (CombatEntityAPI tmp : Global.getCombatEngine().getAsteroids())
        {
            if (MathUtils.isWithinRange(tmp.getLocation(), location, range))
            {
                entities.add(tmp);
            }
        }

        return entities;
    }

    public static List<CombatEntityAPI> inRangeGrid(Vector2f loc, float range)
    {
        final List<CombatEntityAPI> entities = new ArrayList<>();

        for (Iterator iter = Global.getCombatEngine().getAllObjectGrid().getCheckIterator(loc, range * 2f, range * 2f); iter.hasNext(); )
        {
            final CombatEntityAPI entity = (CombatEntityAPI) iter.next();
            if (!(entity instanceof DamagingProjectileAPI || entity instanceof ShipAPI || entity instanceof CombatAsteroidAPI))
            {
                continue;
            }

            if (MathUtils.isWithinRange(entity.getLocation(), loc, range))
            {
                entities.add(entity);
            }
        }

        return entities;
    }

    class TestWithinRangePlugin extends BaseEveryFrameCombatPlugin
    {
        private static final float TEST_RANGE = 1500f;

        @Override
        public void advance(float amount, List<InputEventAPI> events)
        {
            final CombatEngineAPI engine = Global.getCombatEngine();
            final ShipAPI player = Global.getCombatEngine().getPlayerShip();
            if (engine.isPaused() || engine.getCombatUI().isShowingCommandUI() || engine.isUIShowingDialog() ||
                    player == null || !engine.isEntityInPlay(player))
            {
                return;
            }

            final List<CombatEntityAPI> newInRange = inRangeGrid(player.getLocation(), TEST_RANGE),
                    oldInRange = getEntitiesWithinRangeOld(player.getLocation(), TEST_RANGE);

            final Set<CombatEntityAPI> uniqueNew = new HashSet<>(newInRange),
                    uniqueOld = new HashSet<>(oldInRange);
            uniqueNew.removeAll(oldInRange);
            uniqueOld.removeAll(newInRange);

            for (CombatEntityAPI entity : uniqueNew)
            {
                engine.addSmokeParticle(entity.getLocation(), entity.getVelocity(), entity.getCollisionRadius(), 0.5f, 0.5f, Color.GREEN);
                System.out.println("Only in new method: " + entity + " | Distance: " + MathUtils.getDistance(player, entity) +
                        " (real: " + MathUtils.getDistance(player.getLocation(), entity.getLocation()) + ")");
            }

            for (CombatEntityAPI entity : uniqueOld)
            {
                engine.addSmokeParticle(entity.getLocation(), entity.getVelocity(), entity.getCollisionRadius(), 0.5f, 0.5f, Color.RED);
                System.out.println("Only in old method: " + entity + " | Distance: " + MathUtils.getDistance(player, entity) +
                        " (real: " + MathUtils.getDistance(player.getLocation(), entity.getLocation()) + ")");
            }
        }
    }
}
