package org.lazywizard.lazylib.ui;

import org.jetbrains.annotations.NotNull;
import org.lwjgl.util.vector.Vector2f;

import java.awt.*;
import java.util.Map;

/**
 * An efficiently drawable bitmap font implementation. May not be a pixel-perfect match of
 * Starsector's implementation.
 * <p>
 * Usage:
 * <ol>
 * <li>Load a font with {@link LazyFont#loadFont(String)}. This can be kept around safely without worrying about
 * memory - the underlying texture makes up the bulk of the size, and that is only loaded once per unique font.
 * {@link LazyFont} itself only contains font metadata used for rendering, and is relatively lightweight.</li>
 * <li>Using said font, create a {@link DrawableString} with
 * {@link LazyFont#createText(String, Color, float, float, float)}. This methods arguments determine how the
 * text will appear and any size restrictions the block of text should be given. These can all be changed later
 * by calling methods on the returned {@link DrawableString} directly.</li>
 * <li>Draw the text where you need it with {@link DrawableString#draw(float, float)}.</li>
 * <li>That's it! So long as you keep a reference to the {@link DrawableString}, you can re-draw the text
 * extremely efficiently, or edit/append the text at will.</li>
 * <li>When finished using the {@link DrawableString}, call {@link DrawableString#dispose()} to ensure
 * its underlying OpenGL resources are cleaned up. Ensure you don't call {@link DrawableString#draw(float, float)}
 * after calling {@link DrawableString#dispose()} or your game will crash!</li>
 * </ol>
 *
 * @author LazyWizard
 * @since 2.3
 */
// Javadoc stubs for LazyFont, since Dokka is a steaming pile
// The actual source code can be found in LazyFont.kt
public class LazyFont
{
    /**
     * Loads a bitmap font and returns the {@link LazyFont} representation.
     * This method caches loaded fonts, so only one {@link LazyFont} will exist for each
     * font file loaded, and subsequent calls will be near instantaneous.
     *
     * @param fontPath The relative path to the .fnt file (ex: {@code "graphics/fonts/insignia15LTaa.fnt"}).
     *
     * @return A {@link LazyFont} representation of the bitmap font at {@code fontPath}.
     *
     * @throws FontException If there's no font found at {@code fontPath} or the data in the font is malformed.
     * @since 2.3
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
    public LazyFont.LazyChar getChar(char character)
    {
        return null;
    }

    /**
     * Renders a block of text, for manually creating display lists or VBOs - <b>not recommended for general usage</b>.
     * Use {@link LazyFont#createText(String, Color, float, float, float)} instead.
     *
     * @return A {@link Vector2f} containing the width and height of the drawn text area.
     *
     * @see LazyFont#createText(String, Color, float, float, float) for efficiently drawing the same
     *         block of text multiple times
     * @since 2.3
     */
    @NotNull
    public Vector2f drawText(String text, float x, float y, float fontSize, float maxWidth, float maxHeight)
    {
        return null;
    }

    /**
     * Create a {@link DrawableString} with the specified initial text, color, and font size, with text wrapping and
     * a max height - any appended text past that limit will be discarded.
     *
     * @return A {@link DrawableString} with the specified text, color, and font size, with text wrapping
     *         at {@code maxWidth}, and a max height of {@code maxHeight}.
     *
     * @since 2.3
     */
    // TODO: Finish Javadoc (add params)
    @NotNull
    public DrawableString createText(String text, Color color, float size, float maxWidth, float maxHeight)
    {
        return null;
    }

    /**
     * Create a {@link DrawableString} with the specified initial text, color, and font size, with text wrapping.
     *
     * @return A {@link DrawableString} with the specified text, color, and font size, with text wrapping
     *         at {@code maxWidth}.
     *
     * @see LazyFont#createText(String, Color, float, float, float)
     * @since 2.3
     */
    @NotNull
    public DrawableString createText(String text, Color color, float size, float maxWidth)
    {
        return null;
    }

    /**
     * Create a {@link DrawableString} with the specified initial text, color, and font size, with no text wrapping.
     *
     * @return A {@link DrawableString} with the specified text, color, and font size, with no text wrapping.
     *
     * @see LazyFont#createText(String, Color, float, float, float)
     * @since 2.3
     */
    @NotNull
    public DrawableString createText(String text, Color color, float size)
    {
        return null;
    }

    /**
     * Create a {@link DrawableString} with the specified initial text and color. Defaults to the base
     * font size, and no text wrapping.
     *
     * @return A {@link DrawableString} with the specified text and color, the base font size, and no text wrapping.
     *
     * @see LazyFont#createText(String, Color, float, float, float)
     * @since 2.3
     */
    @NotNull
    public DrawableString createText(String text, Color color)
    {
        return null;
    }

    /**
     * Create a {@link DrawableString} with the specified initial text. Defaults to white text, the base
     * font size, and no text wrapping.
     *
     * @return A {@link DrawableString} with the specified text, white color, the base font size, and no text wrapping.
     *
     * @see LazyFont#createText(String, Color, float, float, float)
     * @since 2.3
     */
    @NotNull
    public DrawableString createText(String text)
    {
        return null;
    }

    /**
     * Create a {@link DrawableString} with no text, ready for appending. Defaults to white text, the base
     * font size, and no text wrapping.
     *
     * @return An empty {@link DrawableString} with white text, the base font size, and no text wrapping.
     *
     * @see LazyFont#createText(String, Color, float, float, float)
     * @since 2.3
     */
    @NotNull
    public DrawableString createText()
    {
        return null;
    }

    public class LazyChar
    {
        public Map<Integer, Integer> getKernings()
        {
            return null;
        }

        public float getTx1()
        {
            return 0f;
        }

        public float getTx2()
        {
            return 0f;
        }

        public float getTy1()
        {
            return 0f;
        }

        public float getTy2()
        {
            return 0f;
        }

        public void setKerning(int otherCharId, int kerning)
        {
        }

        public int getKerning(int otherCharId)
        {
            return 0;
        }

        public void setKerning(char otherChar, int kerning)
        {
        }

        public int getKerning(char otherChar)
        {
            return 0;
        }

        public int getId()
        {
            return 0;
        }

        public int getWidth()
        {
            return 0;
        }

        public int getHeight()
        {
            return 0;
        }

        public int getXOffset()
        {
            return 0;
        }

        public int getYOffset()
        {
            return 0;
        }

        public int getAdvance()
        {
            return 0;
        }

        private LazyChar()
        {
        }
    }

    public class DrawableString
    {
        public boolean isDisposed()
        {
            return false;
        }

        public float getWidth()
        {
            return 0f;
        }

        public float getHeight()
        {
            return 0f;
        }

        public float getFontSize()
        {
            return 0f;
        }

        public void setFontSize(float fontSize)
        {
        }

        public float getMaxWidth()
        {
            return 0f;
        }

        public void setMaxWidth(float maxWidth)
        {
        }

        public float getMaxHeight()
        {
            return 0f;
        }

        public void setMaxHeight(float maxHeight)
        {
        }

        public Color getColor()
        {
            return null;
        }

        public void setColor(Color color)
        {
        }

        public String getText()
        {
            return "";
        }

        public void setText(String text)
        {
        }

        public boolean getDrawDebug()
        {
            return false;
        }

        public void setDrawDebug(boolean drawDebug)
        {
        }

        public void appendText(String text)
        {
        }

        public void appendText(String text, int indent)
        {
        }

        public void draw(float x, float y)
        {
        }

        public void draw(Vector2f location)
        {
        }

        public void drawAtAngle(float x, float y, float angle)
        {
        }

        public void drawAtAngle(Vector2f location, float angle)
        {
        }

        public void dispose()
        {
        }

        private DrawableString()
        {
        }
    }


    private LazyFont()
    {
    }
}
