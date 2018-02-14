package org.lazywizard.lazylib.opengl;

import java.awt.Color;
import static org.lwjgl.opengl.GL11.*;

/**
 * Contains methods for working with OpenGL and AWT color objects.
 *
 * @author LazyWizard
 * @since 1.9
 */
public class ColorUtils
{
    /**
     * Sets the OpenGL color using an AWT {@link Color} object.
     * <p>
     * @param color                 The color to set.
     * @param alphaMult             Multiplies the color's alpha channel with
     *                              this, or replaces it if
     *                              {@code overrideOriginalAlpha} is
     *                              {@code true}.
     * @param overrideOriginalAlpha Whether to completely override
     *                              {@code color}'s alpha channel with
     *                              {@code alphaMult}.
     * <p>
     * @since 1.9
     */
    public static void glColor(Color color, float alphaMult,
            boolean overrideOriginalAlpha)
    {
        glColor4ub((byte) color.getRed(), (byte) color.getGreen(),
                (byte) color.getBlue(), (byte) ((overrideOriginalAlpha
                ? alphaMult * 255f : (color.getAlpha() * alphaMult))));
    }

    /**
     * Sets the OpenGL color using an AWT {@link Color} object.
     * <p>
     * @param color The color to set.
     * <p>
     * @since 1.9
     */
    public static void glColor(Color color)
    {
        glColor4ub((byte) color.getRed(), (byte) color.getGreen(),
                (byte) color.getBlue(), (byte) color.getAlpha());
    }

    private ColorUtils()
    {
    }
}
