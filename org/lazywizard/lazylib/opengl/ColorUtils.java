package org.lazywizard.lazylib.opengl;

import java.awt.Color;
import static org.lwjgl.opengl.GL11.*;

/**
 *
 * @author LazyWizard
 * @since 1.9
 */
// TODO: Javadoc and add to changelog
public class ColorUtils
{
    public static void glColor(Color color, float alphaMult,
            boolean overrideOriginalAlpha)
    {
        glColor4ub((byte) color.getRed(), (byte) color.getGreen(),
                (byte) color.getBlue(), (byte) ((overrideOriginalAlpha
                ? alphaMult : (color.getAlpha() * alphaMult))));
    }

    public static void glColor(Color color)
    {
        glColor4ub((byte) color.getRed(), (byte) color.getGreen(),
                (byte) color.getBlue(), (byte) color.getAlpha());
    }

    private ColorUtils()
    {
    }
}
