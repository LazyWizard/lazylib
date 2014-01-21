package org.lazywizard.lazylib.opengl;

import java.awt.Color;
import static org.lwjgl.opengl.GL11.*;

/**
 *
 * @author LazyWizard
 * @since 1.8
 */
class ColorUtils
{
    public static void glColorAWT(Color color)
    {
        glColor4ub((byte) color.getRed(), (byte) color.getGreen(),
                (byte) color.getBlue(), (byte) color.getAlpha());
    }

    private ColorUtils()
    {
    }
}
