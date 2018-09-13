package org.lazywizard.lazylib.ui;

import org.jetbrains.annotations.NotNull;
import org.lwjgl.util.vector.Vector2f;

import java.awt.*;

/**
 * An efficiently drawable bitmap font implementation. May not be a pixel-perfect match of
 * Starsector's implementation.
 *
 * @author LazyWizard
 * @since 3.0
 */
// Javadoc stubs for LazyFont, since Dokka is a steaming pile
public class LazyFont
{
    public LazyFont(int textureId, float baseHeight, float textureWidth, float textureHeight)
    {
        //super(textureId, baseHeight, textureWidth, textureHeight);
        try
        {
            //LazyFont.DrawableString tmp = LazyFont.loadFont("test").createText("huh");
        }
        catch (Exception ex)
        {
        }
    }

    /**
     * Loads a bitmap font and returns the {@link LazyFont} representation.
     * This method caches loaded fonts, so only one {@link LazyFont} will exist for each
     * font file loaded, and subsequent calls will be near instantaneous.
     *
     * @param fontPath The relative path to the .fnt file (ex: {@code "graphics/fonts/insignia15LTaa.fnt"}).
     * @return A {@link LazyFont} representation of the bitmap font at {@code fontPath}.
     * @throws FontException If there's no font found at {@code fontPath} or the data in the font is malformed.
     * @since 3.0
     */
    @NotNull
    public static LazyFont loadFont(String fontPath) throws FontException
    {
        return null;
    }


    @NotNull
    public String wrapString(String toWrap, float fontSize, float maxWidth, float maxHeight, int indent)
    {
        return "";
    }

    @NotNull
    public String wrapString(String toWrap, float fontSize, float maxWidth, float maxHeight)
    {
        return "";
    }

    @NotNull
    public String wrapString(String toWrap, float fontSize, float maxWidth)
    {
        return "";
    }

    public float getBaseHeight()
    {
        return 0f;
    }

    public float getTextureHeight()
    {
        return 0f;
    }

    public float getTextureWidth()
    {
        return 0f;
    }

    public int getTextureId()
    {
        return 0;
    }

    public float calcWidth(String rawLine, float fontSize)
    {
        return 0f;
    }

    @NotNull
    public LazyChar getChar(char character)
    {
        return null;
    }

    /**
     * Renders a block of text, for manually creating display lists or VBOs. <b>Not recommended for general usage</b>
     * - use {@link LazyFont#createText(String, Color, float, float, float)} instead.
     *
     * @return A {@link Vector2f} containing the width and height of the drawn text area.
     * @see LazyFont#createText(String, Color, float, float, float) for efficiently drawing the same
     * block of text multiple times
     * @since 3.0
     */
    @NotNull
    public Vector2f drawText(String text, float x, float y, float fontSize, float maxWidth, float maxHeight)
    {
        return null;
    }

    @NotNull
    public DrawableString createText(String text, Color color, float size, float maxWidth, float maxHeight)
    {
        return null;
    }

    @NotNull
    public DrawableString createText(String text, Color color, float size, float maxWidth)
    {
        return null;
    }

    @NotNull
    public DrawableString createText(String text, Color color, float size)
    {
        return null;
    }

    @NotNull
    public DrawableString createText(String text, Color color)
    {
        return null;
    }

    @NotNull
    public DrawableString createText(String text)
    {
        return null;
    }

    public class LazyChar
    {
    }

    public class DrawableString
    {
    }
}
