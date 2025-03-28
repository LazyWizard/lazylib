package org.lazywizard.lazylib.opengl;

import org.lazywizard.lazylib.MathUtils;

import java.awt.Color;

import static org.lwjgl.opengl.GL11.glColor4ub;

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
     *
     * @param color                 The color to set.
     * @param alphaMult             Multiplies the color's alpha channel with
     *                              this, or replaces it if
     *                              {@code overrideOriginalAlpha} is
     *                              {@code true}.
     * @param overrideOriginalAlpha Whether to completely override
     *                              {@code color}'s alpha channel with
     *                              {@code alphaMult}.
     *
     * @since 1.9
     */
    public static void glColor(Color color, float alphaMult, boolean overrideOriginalAlpha)
    {
        glColor4ub((byte) color.getRed(), (byte) color.getGreen(),
                (byte) color.getBlue(), (byte) MathUtils.clamp((int) (overrideOriginalAlpha
                        ? alphaMult * 255f : (color.getAlpha() * alphaMult)), 0, 255));
    }

    /**
     * Sets the OpenGL color using an AWT {@link Color} object.
     *
     * @param color The color to set.
     * @param alpha The color's opacity, from 0 to 1.
     *
     * @since 2.3
     */
    public static void glColor(Color color, float alpha)
    {
        glColor(color, alpha, true);
    }

    /**
     * Sets the OpenGL color using an AWT {@link Color} object.
     *
     * @param color The color to set.
     *
     * @since 1.9
     */
    public static void glColor(Color color)
    {
        glColor4ub((byte) color.getRed(), (byte) color.getGreen(),
                (byte) color.getBlue(), (byte) color.getAlpha());
    }

    /**
     * Generates a {@link Color} with randomized RGB components.
     *
     * @return A {@link Color} whose RGB components are all randomized. Alpha will be at 100%.
     *
     * @since 3.0
     */
    public static Color genRandomColor()
    {
        return new Color(MathUtils.getRandomNumberInRange(0, 0xff000000));
    }

    private ColorUtils()
    {
    }
}
