package org.lazywizard.lazylib.opengl;

import java.awt.Color;
import org.lwjgl.util.vector.ReadableVector3f;
import org.lwjgl.util.vector.ReadableVector4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

/**
 *
 * @author LazyWizard
 * @since 1.7
 */
class ColorUtils
{
    public static ReadableVector3f toVector3f(Color color)
    {
        return new Vector3f(
                color.getRed() / 255f,
                color.getGreen() / 255f,
                color.getBlue() / 255f);
    }

    public static ReadableVector4f toVector4f(Color color)
    {
        return new Vector4f(
                color.getRed() / 255f,
                color.getGreen() / 255f,
                color.getBlue() / 255f,
                color.getAlpha() / 255f);
    }

    private ColorUtils()
    {
    }
}
