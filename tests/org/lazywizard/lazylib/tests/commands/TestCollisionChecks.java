package org.lazywizard.lazylib.tests.commands;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseEveryFrameCombatPlugin;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ViewportAPI;
import org.lazywizard.console.BaseCommand;
import org.lazywizard.console.CommonStrings;
import org.lazywizard.console.Console;
import org.lazywizard.lazylib.CollisionUtils;
import org.lazywizard.lazylib.opengl.DrawUtils;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector2f;

import java.awt.*;

import static org.lazywizard.lazylib.opengl.ColorUtils.glColor;
import static org.lwjgl.opengl.GL11.*;

public class TestCollisionChecks implements BaseCommand
{
    @Override
    public CommandResult runCommand(String args, CommandContext context)
    {
        if (!context.isInCombat())
        {
            Console.showMessage(CommonStrings.ERROR_COMBAT_ONLY);
            return CommandResult.WRONG_CONTEXT;
        }

        Global.getCombatEngine().addPlugin(new TestCollisionChecksPlugin());
        Console.showMessage("Now testing collision checks.\nLegend:\n - Magenta: nearest point on bounds\n" +
                " - Pink: collision point in direct line\n - Blue square: origin (mouse location)");
        return CommandResult.SUCCESS;
    }

    private static final class TestCollisionChecksPlugin extends BaseEveryFrameCombatPlugin
    {
        @Override
        public void renderInWorldCoords(ViewportAPI view)
        {
            final ShipAPI player = Global.getCombatEngine().getPlayerShip();
            final Vector2f playerLoc = player.getLocation();
            final Vector2f mouseLoc = new Vector2f(view.convertScreenXToWorldX(Mouse.getX()),
                    view.convertScreenYToWorldY(Mouse.getY()));
            final Vector2f nearest = CollisionUtils.getNearestPointOnBounds(mouseLoc, player);
            final Vector2f direct = CollisionUtils.getCollisionPoint(mouseLoc, playerLoc, player);
            glDisable(GL_TEXTURE_2D);
            glEnable(GL_BLEND);
            glLineWidth(3f);
            glBegin(GL_LINES);
            glColor(Color.MAGENTA);
            glVertex2f(mouseLoc.x, mouseLoc.y);
            glVertex2f(nearest.x, nearest.y);
            if (direct == null)
            {
                glColor(Color.RED);
                glVertex2f(mouseLoc.x, mouseLoc.y);
                glVertex2f(playerLoc.x, playerLoc.y);
                System.out.println("Failed to find collision point with player!");
            }
            else
            {
                glColor(Color.PINK);
                glVertex2f(mouseLoc.x, mouseLoc.y);
                glVertex2f(direct.x, direct.y);
            }
            glEnd();
            glColor(Color.BLUE);
            DrawUtils.drawCircle(mouseLoc.x, mouseLoc.y, 15, 4, false);
        }
    }
}
