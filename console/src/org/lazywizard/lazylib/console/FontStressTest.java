package org.lazywizard.lazylib.console;

import com.fs.starfarer.api.EveryFrameScript;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseEveryFrameCombatPlugin;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.ViewportAPI;
import com.fs.starfarer.api.input.InputEventAPI;
import com.fs.starfarer.api.util.IntervalUtil;
import org.lazywizard.console.BaseCommand;
import org.lazywizard.console.CommandUtils;
import org.lazywizard.console.Console;
import org.lazywizard.lazylib.MathUtils;
import org.lazywizard.lazylib.ui.LazyFont;
import org.lazywizard.lazylib.ui.LazyFont.DrawableString;
import org.lazywizard.lazylib.ui.LazyFont.TextAlignment;
import org.lazywizard.lazylib.ui.LazyFont.TextAnchor;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;

import java.awt.Color;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public class FontStressTest implements BaseCommand
{
    @Override
    public CommandResult runCommand(String args, CommandContext context)
    {
        if (context.isInCampaign())
        {
            //Console.showMessage(CommonStrings.ERROR_COMBAT_ONLY);
            Global.getSector().addTransientScript(new FontTestCampaignPlugin());
            return CommandResult.WRONG_CONTEXT;
        }

        final int numPlugins;
        if (CommandUtils.isInteger(args))
        {
            numPlugins = Integer.parseInt(args);
        }
        else
        {
            numPlugins = 1;
        }

        for (int i = 0; i < numPlugins; i++)
        {
            Global.getCombatEngine().addPlugin(new FontTestCombatPlugin());
        }

        Console.showMessage("Added " + numPlugins + " instances of the font stress-testing plugin.");
        return CommandResult.SUCCESS;
    }

    private static class FontTestCampaignPlugin implements EveryFrameScript
    {
        private static final LazyFont font;
        private final DrawableString ul, uc, ur, cl, cc, cr, ll, lc, lr;

        static
        {
            try
            {
                font = LazyFont.loadFont("graphics/fonts/insignia16.fnt");
            }
            catch (Exception ex)
            {
                throw new RuntimeException(ex);
            }
        }

        private FontTestCampaignPlugin()
        {
            ul = font.createText("Anchored at\nupper left");
            ul.setMaxWidth(300f);
            ul.setMaxHeight(300f);
            ul.setAnchor(TextAnchor.TOP_LEFT);
            ul.setAlignment(TextAlignment.LEFT);
            ul.setRenderDebugBounds(true);

            uc = font.createText("Anchored at\nupper center");
            uc.setMaxWidth(300f);
            uc.setMaxHeight(300f);
            uc.setAnchor(TextAnchor.TOP_CENTER);
            uc.setAlignment(TextAlignment.CENTER);
            uc.setRenderDebugBounds(true);

            ur = font.createText("Anchored at\nupper right");
            ur.setMaxWidth(300f);
            ur.setMaxHeight(300f);
            ur.setAnchor(TextAnchor.TOP_RIGHT);
            ur.setAlignment(TextAlignment.RIGHT);
            ur.setRenderDebugBounds(true);

            cl = font.createText("Anchored at\ncenter left");
            cl.setMaxWidth(300f);
            cl.setMaxHeight(300f);
            cl.setAnchor(TextAnchor.CENTER_LEFT);
            cl.setAlignment(TextAlignment.LEFT);
            cl.setRenderDebugBounds(true);

            cc = font.createText("Anchored at\ntrue center");
            cc.setMaxWidth(300f);
            cc.setMaxHeight(300f);
            cc.setAnchor(TextAnchor.CENTER);
            cc.setAlignment(TextAlignment.CENTER);
            cc.setRenderDebugBounds(true);

            cr = font.createText("Anchored at\ncenter right");
            cr.setMaxWidth(300f);
            cr.setMaxHeight(300f);
            cr.setAnchor(TextAnchor.CENTER_RIGHT);
            cr.setAlignment(TextAlignment.RIGHT);
            cr.setRenderDebugBounds(true);

            ll = font.createText("Anchored at\nlower left");
            ll.setMaxWidth(300f);
            ll.setMaxHeight(300f);
            ll.setAnchor(TextAnchor.BOTTOM_LEFT);
            ll.setAlignment(TextAlignment.LEFT);
            ll.setRenderDebugBounds(true);

            lc = font.createText("Anchored at\nlower center");
            lc.setMaxWidth(300f);
            lc.setMaxHeight(300f);
            lc.setAnchor(TextAnchor.BOTTOM_CENTER);
            lc.setAlignment(TextAlignment.CENTER);
            lc.setRenderDebugBounds(true);

            lr = font.createText("Anchored at\nlower right");
            lr.setMaxWidth(300f);
            lr.setMaxHeight(300f);
            lr.setAnchor(TextAnchor.BOTTOM_RIGHT);
            lr.setAlignment(TextAlignment.RIGHT);
            lr.setRenderDebugBounds(true);
        }

        @Override
        public boolean isDone()
        {
            return false;
        }

        @Override
        public boolean runWhilePaused()
        {
            return true;
        }

        @Override
        public void advance(float v)
        {
            final int width = (int) (Display.getWidth() * Display.getPixelScaleFactor()),
                    height = (int) (Display.getHeight() * Display.getPixelScaleFactor());
            final Vector2f mousePos = new Vector2f(Mouse.getX(), Mouse.getY());

            glPushAttrib(GL_ALL_ATTRIB_BITS);
            glMatrixMode(GL_PROJECTION);
            glPushMatrix();
            glLoadIdentity();
            glViewport(0, 0, width, height);
            glOrtho(0, width, 0, height, -1, 1);
            glMatrixMode(GL_MODELVIEW);
            glPushMatrix();
            glLoadIdentity();
            glEnable(GL_TEXTURE_2D);
            glEnable(GL_BLEND);
            glTranslatef(0.01f, 0.01f, 0);

            int angle = 0;//(int) (System.currentTimeMillis() / 10) % 360;

            ul.drawAtAngle(mousePos, angle);
            //uc.drawAtAngle(mousePos, angle);
            ur.drawAtAngle(mousePos, angle);
            //cl.drawAtAngle(mousePos, angle);
            //cc.drawAtAngle(mousePos, angle);
            //cr.drawAtAngle(mousePos, angle);
            ll.drawAtAngle(mousePos, angle);
            //lc.drawAtAngle(mousePos, angle);
            lr.drawAtAngle(mousePos, angle);

            glDisable(GL_BLEND);
            glPopMatrix();
            glMatrixMode(GL_PROJECTION);
            glPopMatrix();
            glPopAttrib();
        }
    }

    private static class FontTestCombatPlugin extends BaseEveryFrameCombatPlugin
    {
        private static final LazyFont font;
        private final IntervalUtil relocate = new IntervalUtil(3f, 7f);
        private final Vector2f loc = new Vector2f(0f, 0f);
        private final LazyFont.DrawableString toDraw = font.createText("", Color.LIGHT_GRAY,
                font.getBaseHeight(), 120f, 120f);

        static
        {
            try
            {
                font = LazyFont.loadFont("graphics/fonts/insignia16.fnt");
            }
            catch (Exception ex)
            {
                throw new RuntimeException(ex);
            }
        }

        private Color genRandomColor()
        {
            return new Color(MathUtils.getRandomNumberInRange(0, 0xff000000));
        }

        private void buildRandomString(DrawableString toDraw, int len)
        {
            toDraw.setText("");
            for (int i = 0; i < len; i++)
            {
                //if (Math.random() < .1) toDraw.append(' '); // Spaces should be extra common
                toDraw.append((char) MathUtils.getRandomNumberInRange(32, 126), genRandomColor());
            }
        }

        private void relocate()
        {
            /*loc.x = MathUtils.getRandomNumberInRange(10, Display.getWidth() - 60f);
            toDraw.setMaxWidth(MathUtils.getRandomNumberInRange(50, Display.getWidth() - loc.x));

            loc.y = MathUtils.getRandomNumberInRange(60, Display.getHeight() - 10f);
            toDraw.setMaxHeight(MathUtils.getRandomNumberInRange(50, Display.getHeight() - loc.y));

            toDraw.setFontSize(8 * MathUtils.getRandomNumberInRange(1, 4));
            toDraw.setColor(genRandomColor());*/

            loc.x = 10f;
            loc.y = Display.getHeight() - 10f;
            toDraw.setMaxWidth(Display.getWidth() - 20f);
            toDraw.setMaxHeight(Display.getHeight() - 20f);
            toDraw.setFontSize(8);// * MathUtils.getRandomNumberInRange(1, 4));
            toDraw.setColor(genRandomColor());
        }

        @Override
        public void advance(float amount, List<InputEventAPI> events)
        {
            if (Global.getCombatEngine().isPaused()) return;

            relocate.advance(amount);
            if (relocate.intervalElapsed())
            {
                relocate();
            }

            buildRandomString(toDraw, 80000);
        }

        @Override
        public void renderInUICoords(ViewportAPI viewport)
        {
            toDraw.draw(loc);
        }

        @Override
        public void init(CombatEngineAPI engine)
        {
            //toDraw.setRenderDebugBounds(true);
            relocate();
        }
    }
}
