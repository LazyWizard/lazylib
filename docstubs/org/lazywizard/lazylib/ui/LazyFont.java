package org.lazywizard.lazylib.ui;

import com.fs.starfarer.api.graphics.SpriteAPI;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.util.vector.Vector2f;

import java.awt.Color;
import java.util.Map;

/**
 * An efficiently drawable bitmap font implementation. May not be a pixel-perfect match to Starsector's implementation.
 * <p>
 * <h3>Basic Usage:</h3>
 * <ul>
 * <li>Load a font with {@link LazyFont#loadFont(String)}. This can be kept around safely without worrying about
 * memory usage - the underlying texture makes up the bulk of the size, and that is only loaded once per unique
 * font and stored in a global cache. {@link LazyFont} itself only contains font metadata used for rendering,
 * and is relatively lightweight.</li>
 * <li>Using said font object, create a {@link DrawableString} with
 * {@link LazyFont#createText(String, Color, float, float, float)}. This methods arguments determine how the
 * text will appear and any size restrictions the block of text should be given. These can all be changed later
 * by calling methods directly on the returned {@link DrawableString}.</li>
 * <li>Draw the text where you need it with {@link DrawableString#draw(float, float)} once per frame.</li>
 * <li>That's it! So long as you keep a reference to the {@link DrawableString}, you can re-draw the text
 * extremely efficiently, or edit/append the text at will. Editing the {@link DrawableString} in any way
 * will cause a full rebuild of its internal display list the next time you call
 * {@link DrawableString#draw(float, float)}, though the performance impact should be minimal even with
 * extremely long text blocks.</li>
 * <li>When finished using the {@link DrawableString}, optionally call {@link DrawableString#dispose()} to ensure
 * its underlying OpenGL resources are cleaned up immediately. Ensure you don't call
 * {@link DrawableString#draw(float, float)} after calling {@link DrawableString#dispose()} or your game
 * will crash!</li>
 * </ul>
 *
 * <h3>Example Usage:</h3>
 * <pre><code>
 * package data.scripts.plugins;
 *
 * import com.fs.starfarer.api.Global;
 * import com.fs.starfarer.api.combat.BaseEveryFrameCombatPlugin;
 * import com.fs.starfarer.api.combat.CombatEngineAPI;
 * import com.fs.starfarer.api.combat.ViewportAPI;
 * import org.lazywizard.lazylib.ui.FontException;
 * import org.lazywizard.lazylib.ui.LazyFont;
 * import org.lwjgl.input.Mouse;
 *
 * import java.awt.*;
 *
 * public class LazyFontExample extends BaseEveryFrameCombatPlugin
 * {
 *     private LazyFont.DrawableString toDraw;
 *
 *     // Set up the font and the DrawableString; only has to be done once
 *     &#064;Override
 *     public void init(CombatEngineAPI engine)
 *     {
 *         // Load the chosen .fnt file
 *         // Fonts are cached globally, so it's acceptable for each class using the same
 *         // font to request their own copy of it - they will all share the underlying data
 *         final LazyFont font;
 *         try
 *         {
 *             font = LazyFont.loadFont("graphics/fonts/insignia15LTaa.fnt");
 *         }
 *         // FontException is thrown if the .fnt file does not exist or has malformed data
 *         catch (FontException ex)
 *         {
 *             Global.getLogger(this.getClass()).error("Failed to load font", ex);
 *             engine.removePlugin(this);
 *             return;
 *         }
 *
 *         // Create a renderable block of text (in this case, will be yellow with font size 15)
 *         toDraw = font.createText("This is some sample text.", Color.YELLOW, 15f);
 *
 *         // Enable line wrapping when text reaches 400 pixels wide
 *         toDraw.setMaxWidth(400f);
 *
 *         // If you need to add text to the DrawableString, do so like this:
 *         toDraw.appendText("\nThis is a second line of sample text.");
 *         toDraw.appendText("\nThis is a third line of sample text that shows off the automatic" +
 *                 " word wrapping when a line of text reaches the maximum width you've chosen.");
 *     }
 *
 *     &#064;Override
 *     public void renderInUICoords(ViewportAPI view)
 *     {
 *         // Call draw() once per frame to render the text
 *         // In this case, draw the text slightly below the mouse cursor
 *         // The draw point is the top left corner of the textbox, so we adjust the X
 *         // position to center the text horizontally below the mouse cursor
 *         if (toDraw != null) // Needed to work around a vanilla combat plugin bug when loading the campaign
 *         {
 *             toDraw.draw(Mouse.getX() - (toDraw.getWidth() / 2f), Mouse.getY() - 30f);
 *         }
 *     }
 * }
 * </code></pre>
 *
 * @author LazyWizard
 * @since 2.3
 */
// Javadoc stubs for LazyFont, since Dokka is a steaming pile
// The actual source code can be found in LazyFont.kt
@SuppressWarnings("ALL")
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

    /**
     * Returns the word-wrapping that would be done to a block of text to fit it within a specific area at a specific
     * font size. Can be used alongside {@link LazyFont#calcWidth(String, float)} to determine the size of a block of
     * rendered text without needing the expense of building a {@link DrawableString} first.
     *
     * @param toWrap    The text to be word-wrapped.
     * @param fontSize  The font size the text would be rendered at.
     * @param maxWidth  The max width of the text area. Text will be wrapped at the last word that fit within this
     *                  width.
     * @param maxHeight The max height of the text area. Text beyond this will be discarded.
     * @param indent    The number of empty spaces at the start of each line. Used by {@link
     *                  DrawableString#appendText(String, int)} and {@link
     *                  DrawableString#appendText(String, Color, int)}.
     *
     * @return {@code toWrap}, wrapped to fit within the area specified by {@code maxWidth} and {@code maxHeight}.
     *
     * @since 2.3
     */
    @NotNull
    public String wrapString(String toWrap, float fontSize, float maxWidth, float maxHeight, int indent)
    {
        return "";
    }

    /**
     * Returns the word-wrapping that would be done to a block of text to fit it within a specific area at a specific
     * font size. Can be used alongside {@link LazyFont#calcWidth(String, float)} to determine the size of a block of
     * rendered text without needing the expense of building a {@link DrawableString} first.
     *
     * @param toWrap    The text to be word-wrapped.
     * @param fontSize  The font size the text would be rendered at.
     * @param maxWidth  The max width of the text area. Text will be wrapped at the last word that fit within this
     *                  width.
     * @param maxHeight The max height of the text area. Text beyond this will be discarded.
     *
     * @return {@code toWrap}, wrapped to fit within the area specified by {@code maxWidth} and {@code maxHeight}.
     *
     * @since 2.3
     */
    @NotNull
    public String wrapString(String toWrap, float fontSize, float maxWidth, float maxHeight)
    {
        return "";
    }

    /**
     * Returns the word-wrapping that would be done to a block of text to fit it within a specific area at a specific
     * font size. Can be used alongside {@link LazyFont#calcWidth(String, float)} to determine the size of a block of
     * rendered text without needing the expense of building a {@link DrawableString} first.
     *
     * @param toWrap   The text to be word-wrapped.
     * @param fontSize The font size the text would be rendered at.
     * @param maxWidth The max width of the text area. Text will be wrapped at the last word that fit within this
     *                 width.
     *
     * @return {@code toWrap}, wrapped to fit within the area specified by {@code maxWidth}.
     *
     * @since 2.3
     */
    @NotNull
    public String wrapString(String toWrap, float fontSize, float maxWidth)
    {
        return "";
    }

    /**
     * Returns the base height of the underlying font. Rendered text will look best when drawn at an evenly divisible
     * ratio of this value.
     *
     * @return The base height of the font, in pixels.
     *
     * @since 2.3
     */
    public float getBaseHeight()
    {
        return 0f;
    }

    /**
     * Returns the height of the font's underlying texture atlas.
     *
     * @return The height of the underlying texture atlas, in pixels.
     *
     * @since 2.3
     */
    public float getTextureHeight()
    {
        return 0f;
    }

    /**
     * Returns the width of the font's underlying texture atlas.
     *
     * @return The width of the underlying texture atlas, in pixels.
     *
     * @since 2.3
     */
    public float getTextureWidth()
    {
        return 0f;
    }

    /**
     * Returns the ID of the font's underlying texture atlas. Equivalent to calling {@link SpriteAPI#getTextureId()}.
     *
     * @return The ID of the underlying texture atlas.
     *
     * @since 2.3
     */
    public int getTextureId()
    {
        return 0;
    }

    /**
     * Returns the raw width of a {@link String} at a specific font size, without taking into account word wrapping.
     *
     * @param rawLine  The {@link String} to measure.
     * @param fontSize The font size the {@link String} would be rendered at.
     *
     * @return The width of {@code rawLine} if drawn with size {@code fontSize}, not taking word wrapping into account.
     *
     * @since 2.3
     */
    public float calcWidth(String rawLine, float fontSize)
    {
        return 0f;
    }

    /***
     * Returns the name of the font, as defined in its .fnt file.
     *
     * @return The name of the loaded font.
     *
     * @since 2.5b
     */
    @NotNull
    public String getFontName()
    {
        return "";
    }

    /**
     * Returns font metadata for a specific character. If the font does not support the character, data for '?' is
     * returned instead.
     *
     * @param character The character to look up.
     *
     * @return A {@link LazyChar} containing metadata for how the font handles {@code character}, or metadata for '?' if
     *         that character is not supported by the font.
     *
     * @since 2.3
     */
    @NotNull
    public LazyFont.LazyChar getChar(char character)
    {
        return null;
    }

    /**
     * <b>DEPRECATED AND SLATED FOR REMOVAL - DO NOT USE!</b>
     * <br>
     * Renders a block of text, used to manually create display lists - <b>not recommended for general
     * usage</b>. Use {@link LazyFont#createText(String, Color, float, float, float)} instead.
     *
     * @param text      The text to be drawn.
     * @param x         The x coordinate of the starting point for rendering (top left corner of text).
     * @param y         The y coordinate of the starting point for rendering (top left corner of text).
     * @param fontSize  The size of the text to be drawn. For best results, this should be evenly divisible by {@link
     *                  LazyFont#getBaseHeight()}. Other values may cause slight blurriness or jaggedness.
     * @param maxWidth  The maximum width of the drawn text before further text will be wrapped to a new line.
     * @param maxHeight The maximum height of the drawn text. All further text past this point will be discarded.
     *
     * @return A {@link Vector2f} containing the width and height of the drawn text area.
     *
     * @see LazyFont#createText(String, Color, float, float, float) for when you need to draw the same
     *         block of text multiple times (99% of use cases).
     * @since 2.3
     * @deprecated Use {@link #createText(String, Color, float, float, float)} instead! This will be removed soon.
     */
    @NotNull
    @Deprecated
    public Vector2f drawText(String text, float x, float y, float fontSize, float maxWidth, float maxHeight)
    {
        return null;
    }

    /**
     * Create a {@link DrawableString} with the specified initial text, color, and font size, with text wrapping and
     * a max height - any appended text past that limit will be discarded.
     *
     * @param text      The initial text to be drawn. You can modify it later using the returned {@link DrawableString}.
     * @param color     The color of the drawn text.
     * @param size      The font size of the drawn text. For best results, this should be evenly divisible by {@link
     *                  LazyFont#getBaseHeight()}. Other values may cause slight blurriness or jaggedness.
     * @param maxWidth  The maximum width of the drawn text before further text will be wrapped to a new line.
     * @param maxHeight The maximum height of the drawn text. All further text past this point will be discarded.
     *
     * @return A {@link DrawableString} with the specified text, color, and font size, with text wrapping
     *         at {@code maxWidth}, and a max height of {@code maxHeight}.
     *
     * @since 2.3
     */
    @NotNull
    public DrawableString createText(String text, Color color, float size, float maxWidth, float maxHeight)
    {
        return null;
    }

    /**
     * Create a {@link DrawableString} with the specified initial text, color, and font size, with text wrapping.
     *
     * @param text     The initial text to be drawn. You can modify it later using the returned {@link DrawableString}.
     * @param color    The color of the drawn text.
     * @param size     The font size of the drawn text. For best results, this should be evenly divisible by {@link
     *                 LazyFont#getBaseHeight()}. Other values may cause slight blurriness or jaggedness.
     * @param maxWidth The maximum width of the drawn text before further text will be wrapped to a new line.
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
     * @param text  The initial text to be drawn. You can modify it later using the returned {@link DrawableString}.
     * @param color The color of the drawn text.
     * @param size  The font size of the drawn text. For best results, this should be evenly divisible by {@link
     *              LazyFont#getBaseHeight()}. Other values may cause slight blurriness or jaggedness.
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
     * @param text  The initial text to be drawn. You can modify it later using the returned {@link DrawableString}.
     * @param color The color of the drawn text.
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
     * @param text The initial text to be drawn. You can modify it later using the returned {@link DrawableString}.
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

    //<editor-fold desc="LazyChar" defaultstate="collapsed">

    /**
     * Keeps track of the metadata for each supported character in a font. This is used internally to render and wrap
     * text, and it is unlikely that end users will ever need to use this class or any of its methods in their code.
     * <p>
     * In the rare case that these methods are needed, their names should match the bitmap font file format found <a
     * href="http://www.angelcode.com/products/bmfont/doc/file_format.html">here</a> under the {@code char} and
     * {@code kerning} headings.
     *
     * @author LazyWizard
     * @since 2.3
     */
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
    //</editor-fold>

    //<editor-fold desc="DrawableString">

    /**
     * Represents an efficiently redrawable block of text. See the documentation for {@link LazyFont} for example usage.
     *
     * @author LazyWizard
     * @since 2.3
     */
    public class DrawableString
    {
        /**
         * Returns a reference to the {@link LazyFont} used to create this {@link DrawableString}.
         *
         * @return The font used to create this {@link DrawableString}.
         *
         * @since 2.3
         */
        public LazyFont getFont()
        {
            return null;
        }

        /**
         * Returns the width of the rendered text area. This is not the same as {@link DrawableString#getMaxWidth()} -
         * this only considers the area used by the text.
         *
         * @return The width of the text area to be rendered.
         *
         * @since 2.3
         */
        public float getWidth()
        {
            return 0f;
        }

        /**
         * Returns the height of the rendered text area. This is not the same as {@link DrawableString#getMaxHeight()} -
         * this only considers the area used by the text.
         *
         * @return The height of the text area to be rendered.
         *
         * @since 2.3
         */
        public float getHeight()
        {
            return 0f;
        }

        /**
         * Returns the font size text will be drawn at.
         *
         * @return The font size of this {@link DrawableString}.
         *
         * @since 2.3
         */
        public float getFontSize()
        {
            return 0f;
        }

        /**
         * Changes the font size that this text will be rendered at.
         * <p>
         * Changing the font size will necessitate a full rebuild of the {@link DrawableString} the next time you
         * attempt to draw or measure the text.
         *
         * @param fontSize The new font size to render at.
         *
         * @since 2.3
         */
        public void setFontSize(float fontSize)
        {
        }

        /**
         * Returns the maximum width of the area that this text will be rendered within.
         *
         * @return The maximum width of the text area. Text will wrapped at the last word that fit within this area.
         *
         * @since 2.3
         */
        public float getMaxWidth()
        {
            return 0f;
        }

        /**
         * Changes the maximum width of the area that this text will be rendered within.
         * <p>
         * Changing the text area constraints will necessitate a full rebuild of the {@link DrawableString} the next
         * time you attempt to draw or measure the text.
         *
         * @param maxWidth The nex maximum width of the text area. Text will be wrapped at the last word that fit
         *                 within this area.
         *
         * @since 2.3
         */
        public void setMaxWidth(float maxWidth)
        {
        }

        /**
         * Returns the maximum height of the area that this text will be rendered within.
         *
         * @return The maximum height of the text area. Further text will be discarded once this limit is reached.
         *
         * @since 2.3
         */
        public float getMaxHeight()
        {
            return 0f;
        }

        /**
         * Changes the maximum height of the area that this text will be rendered within.
         * <p>
         * Changing the text area constraints will necessitate a full rebuild of the {@link DrawableString} the next
         * time you attempt to draw or measure the text.
         *
         * @param maxHeight The nex maximum height of the text area. Further text will be discarded once this limit is
         *                  reached.
         *
         * @since 2.3
         */
        public void setMaxHeight(float maxHeight)
        {
        }

        /**
         * Returns the current color text will be drawn as.
         *
         * @return The color text will be drawn as.
         *
         * @since 2.3
         */
        public Color getColor()
        {
            return null;
        }

        /**
         * Sets the color text will be drawn as.
         * <p>
         * Unlike most setX() methods in this class, calling this will <i>not</i> trigger a rebuild of the entire {@link
         * DrawableString}.
         *
         * @param color The color to draw the text as.
         *
         * @since 2.3
         */
        public void setColor(Color color)
        {
        }

        /**
         * Returns the raw text of this {@link DrawableString}, before any word-wrapping is applied.
         *
         * <b>Performance note:</b> due to the underlying text being stored in a {@link StringBuilder}, a new {@link
         * String} will be built every time this method is called.
         *
         * @return The raw text of this {@link DrawableString} (without word-wrapping applied).
         *
         * @see LazyFont#wrapString(String, float, float, float) to calculate the text after wrapping is applied.
         * @since 2.3
         */
        public String getText()
        {
            return "";
        }

        /**
         * Replaces all text in the {@link DrawableString} with the entered {@link String}.
         * <p>
         * Changing a {@link DrawableString}'s text will necessitate a full rebuild of its contents the next time you
         * attempt to draw or measure the text.
         *
         * @param text The {@link String} to set this {@link DrawableString}'s contents to.
         *
         * @since 2.3
         */
        public void setText(String text)
        {
        }

        /**
         * Adds additional text to the end of the current text.
         * <p>
         * Changing a {@link DrawableString}'s text will necessitate a full rebuild of its contents the next time you
         * attempt to draw or measure the text.
         *
         * @param text The text to add to the {@link DrawableString}.
         *
         * @since 2.3
         */
        public void appendText(String text)
        {
        }

        /**
         * Adds a colored substring to the end of the current text. Only this substring
         * will use the given color, and all further normally appended text will use
         * the color returned by {@link DrawableString#getColor()}.
         * <p>
         * Changing a {@link DrawableString}'s text will necessitate a full rebuild of its contents the next time you
         * attempt to draw or measure the text.
         *
         * @param text  The text to add to the {@link DrawableString}.
         * @param color The color this text should appear as.
         *
         * @since 2.5c
         */
        public void appendText(String text, Color color)
        {
        }

        /**
         * Adds additional text to the end of the current text.
         * <p>
         * Changing a {@link DrawableString}'s text will necessitate a full rebuild of its contents the next time you
         * attempt to draw or measure the text.
         *
         * @param text   The text to add to the {@link DrawableString}.
         * @param indent All lines will be prepended with this number of blank spaces.
         *
         * @since 2.3
         */
        public void appendText(String text, int indent)
        {
        }

        /**
         * Adds a colored substring to the end of the current text. Only this substring
         * will use the given color, and all further normally appended text will use
         * the color returned by {@link DrawableString#getColor()}.
         * <p>
         * Changing a {@link DrawableString}'s text will necessitate a full rebuild of its contents the next time you
         * attempt to draw or measure the text.
         *
         * @param text   The text to add to the {@link DrawableString}.
         * @param indent All lines will be prepended with this number of blank spaces.
         * @param color  The color this text should appear as.
         *
         * @since 2.5c
         */
        public void appendText(String text, Color color, int indent)
        {
        }

        /**
         * Renders the text area at the specified coordinates. This method must be called once per frame.
         * <p>
         * You can render the same block of text in multiple places. The OpenGL commands are cached in a display list,
         * so performance should not be an issue (provided the underlying data isn't changed, necessitating a rebuild).
         *
         * @param x The X coordinate to draw at.
         * @param y The Y coordinate to draw at.
         *
         * @since 2.3
         */
        public void draw(float x, float y)
        {
        }

        /**
         * Renders the text area at the specified coordinates. This method must be called once per frame.
         * <p>
         * You can render the same block of text in multiple places. The OpenGL commands are cached in a display list,
         * so performance should not be an issue (provided the underlying data isn't changed, necessitating a rebuild).
         *
         * @param location The coordinates to draw at.
         *
         * @since 2.3
         */
        public void draw(Vector2f location)
        {
        }

        /**
         * Renders the text area at the specified coordinates with the specified angle. This method must be called once
         * per frame.
         * <p>
         * You can render the same block of text in multiple places. The OpenGL commands are cached in a display list,
         * so performance should not be an issue (provided the underlying data isn't changed, necessitating a rebuild).
         *
         * @param x     The X coordinate to draw at.
         * @param y     The Y coordinate to draw at.
         * @param angle The angle to draw at.
         *
         * @since 2.3
         */
        public void drawAtAngle(float x, float y, float angle)
        {
        }

        /**
         * Renders the text area at the specified coordinates with the specified angle. This method must be called once
         * per frame.
         * <p>
         * You can render the same block of text in multiple places. The OpenGL commands are cached in a display list,
         * so performance should not be an issue (provided the underlying data isn't changed, necessitating a rebuild).
         *
         * @param location The coordinates to draw at.
         * @param angle    The angle to draw at.
         *
         * @since 2.3
         */
        public void drawAtAngle(Vector2f location, float angle)
        {
        }

        /**
         * Whether this object's underlying OpenGL resources have been cleaned up - attempting to render after disposal
         * will cause a {@link RuntimeException}!
         *
         * @return {@code true} if {@link DrawableString#dispose()} has been called; {@code false} otherwise.
         *
         * @since 2.3
         */
        public boolean isDisposed()
        {
            return false;
        }

        /**
         * Cleans up the underlying OpenGL resources of this {@link DrawableString}. Calling any draw() method after
         * this will cause a {@link RuntimeException}.
         * <p>
         * Calling this method is optional, and rarely required. However, there are a limited number of display lists
         * available, so if you are creating many thousands of {@link DrawableString}s, you will need to call this when
         * done with each to prevent the pool from being exhausted before the garbage collector can get around to
         * cleaning them up for you.
         *
         * @since 2.3
         */
        public void dispose()
        {
        }

        private DrawableString()
        {
        }
    }
    //</editor-fold>

    private LazyFont()
    {
    }
}
