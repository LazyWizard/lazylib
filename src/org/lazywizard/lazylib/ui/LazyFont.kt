@file:JvmName("LazyFont")

package org.lazywizard.lazylib.ui

import com.fs.starfarer.api.Global
import org.apache.log4j.Logger
import org.lazywizard.lazylib.opengl.ColorUtils.glColor
import org.lwjgl.opengl.GL11.*
import org.lwjgl.util.vector.Vector2f
import java.awt.Color
import java.io.IOException
import java.net.URI
import java.util.*

// Documentation for this class is in the docstubs directory
class LazyFont private constructor(val textureId: Int, val baseHeight: Float, val textureWidth: Float, val textureHeight: Float) {
    // Quick array lookup for characters 32-255 (standard ASCII range)
    private val lookupTable: Array<LazyChar?> = arrayOfNulls(224)
    // Much slower map-based lookup for international characters (256+ in Unicode)
    private val extendedChars = HashMap<Char, LazyChar>()

    // File format documentation: http://www.angelcode.com/products/bmfont/doc/file_format.html
    private companion object FontLoader {
        // These are used for validating read data
        private const val METADATA_LENGTH = 51
        private const val CHARDATA_LENGTH = 21
        private const val KERNDATA_LENGTH = 7

        // TODO: Write a proper file parser (this works fine for now, but requires an unmaintainable mess of magic offset numbers)
        private val SPLIT_REGEX = """=|\s+(?=([^"]*"[^"]*")*[^"]*$)""".toRegex()
        private val Log: Logger = Logger.getLogger(LazyFont::class.java)
        private val fontCache = HashMap<String, LazyFont>()

        @JvmStatic
        @Throws(FontException::class)
        fun loadFont(fontPath: String): LazyFont {
            val canonPath = URI(fontPath).normalize().path
            Log.debug("\n\n$fontPath -> $canonPath\n\n")
            if (fontCache.contains(canonPath)) return fontCache.getValue(canonPath)

            // Load the font file contents for later parsing
            var header = ""
            val charLines = ArrayList<String>()
            val kernLines = ArrayList<String>()
            try {
                Scanner(Global.getSettings().openStream(canonPath)).use { reader ->
                    // Store header with font metadata
                    header = "${reader.nextLine()} ${reader.nextLine()} ${reader.nextLine()}"

                    // Read raw font data
                    while (reader.hasNextLine()) {
                        val line = reader.nextLine()
                        if (line.startsWith("char ")) { // Character data
                            charLines.add(line)
                        } else if (line.startsWith("kerning ")) { // Kerning data
                            kernLines.add(line)
                        }
                    }
                }
            } catch (ex: IOException) {
                throw FontException("Failed to load font at '$canonPath'", ex)
            }

            // Parse the file data we retrieved earlier and convert it into something usable
            try {
                // TODO: Parse and store ALL font metadata
                val metadata = header.split(SPLIT_REGEX).dropLastWhile { it.isEmpty() }.toTypedArray()
                if (metadata.size != METADATA_LENGTH) {
                    Log.error("Metadata length mismatch: ${metadata.size} vs expected length of $METADATA_LENGTH.")
                    Log.error("Input string: $header")
                    throw FontException("Metadata length mismatch in '$canonPath'")
                }

                //val fontName = metadata[2].replace("\"", "")
                val baseHeight = java.lang.Float.parseFloat(metadata[27])

                // Get image file path from metadata
                val dirIndex = canonPath.lastIndexOf("/")
                val imgFile = (if (dirIndex == -1)
                    canonPath
                else
                    canonPath.substring(0, dirIndex + 1)) + metadata[50].replace("\"", "")

                // Load the font image into a texture
                // TODO: Add support for multiple image files; 'pages' in the font file
                val textureId: Int
                val textureWidth: Float
                val textureHeight: Float
                try {
                    Global.getSettings().loadTexture(imgFile)
                    val texture = Global.getSettings().getSprite(imgFile)
                    textureId = texture.textureId
                    textureWidth = texture.width
                    textureHeight = texture.height
                } catch (ex: IOException) {
                    throw FontException("Failed to load texture atlas '$imgFile'", ex)
                }

                val font = LazyFont(textureId, baseHeight, textureWidth, textureHeight)

                // Parse character data and place into a quick lookup table or extended character map
                for (charLine in charLines) {
                    val charData = charLine.split(SPLIT_REGEX).dropLastWhile { it.isEmpty() }.toTypedArray()
                    if (charData.size != CHARDATA_LENGTH) {
                        Log.error("Character data length mismatch: ${charData.size} vs expected length of $CHARDATA_LENGTH.")
                        Log.error("Input string: $charLine")
                        throw FontException("Character data length mismatch in '$canonPath'")
                    }

                    font.addChar(id = Integer.parseInt(charData[2]),
                            tx = Integer.parseInt(charData[4]),
                            ty = Integer.parseInt(charData[6]),
                            width = Integer.parseInt(charData[8]),
                            height = Integer.parseInt(charData[10]),
                            xOffset = Integer.parseInt(charData[12]),
                            yOffset = Integer.parseInt(charData[14]),
                            advance = Integer.parseInt(charData[16]) + 1)
                    //page = Integer.parseInt(data[18]),
                    // channel = Integer.parseInt(data[20]));
                }

                // Parse and add kerning data
                for (kernLine in kernLines) {
                    val kernData = kernLine.split(SPLIT_REGEX).dropLastWhile { it.isEmpty() }.toTypedArray()
                    if (kernData.size != KERNDATA_LENGTH) {
                        Log.error("Kerning data length mismatch: ${kernData.size} vs expected length of $KERNDATA_LENGTH.")
                        Log.error("Input string: $kernLine")
                        throw FontException("Kerning data length mismatch in '$canonPath'")
                    }

                    val id = Integer.parseInt(kernData[4])
                    val otherId = Integer.parseInt(kernData[2])
                    val kernAmount = Integer.parseInt(kernData[6])
                    font.getChar(id.toChar()).setKerning(otherId, kernAmount)
                }

                fontCache[canonPath] = font
                return font
            } catch (ex: NumberFormatException) {
                throw FontException("Failed to parse font at '$canonPath'", ex)
            }
        }
    }

    private fun addChar(id: Int, tx: Int, ty: Int, width: Int, height: Int, xOffset: Int, yOffset: Int, advance: Int) {
        val tmp = LazyChar(id, tx, ty, width, height, xOffset, yOffset, advance)
        if (tmp.id in 32..255) lookupTable[tmp.id - 32] = tmp
        else extendedChars[tmp.id.toChar()] = tmp
    }

    fun getChar(character: Char): LazyChar {
        val ch: LazyChar? = if (character.toInt() in 32..255) lookupTable[character.toInt() - 32] else extendedChars[character]
        if (ch != null) return ch

        // This assumes that the font will always have a question mark character defined
        System.out.println("Error: couldn't find char '$character'")
        return getChar('?')
    }

    fun calcWidth(rawLine: String, fontSize: Float): Float {
        val scaleFactor = fontSize / baseHeight
        var lastChar: LazyChar? = null
        var curWidth = 0f
        for (tmp in rawLine) {
            val ch = getChar(tmp)
            val kerning = if (lastChar != null) ch.getKerning(lastChar.id).toFloat() else 0f
            curWidth += (kerning + ch.advance) * scaleFactor
            lastChar = ch
        }

        return curWidth
    }

    private fun buildUntilLimit(rawLine: String, fontSize: Float, maxWidth: Float): String {
        if (rawLine.isBlank() || maxWidth <= 0f) return ""

        val scaleFactor = fontSize / baseHeight
        var lastChar: LazyChar? = null
        var curChar = 0
        var curWidth = 0f
        for (tmp in rawLine) {
            val ch = getChar(tmp)
            val kerning = if (lastChar != null) ch.getKerning(lastChar.id).toFloat() else 0f
            val width = (kerning + ch.advance) * scaleFactor

            if (curWidth + width > maxWidth) {
                return rawLine.take(curChar)
            }

            curWidth += width
            curChar++
            lastChar = ch
        }

        // Entire line fits
        return rawLine
    }

    @JvmOverloads
    fun wrapString(toWrap: String, fontSize: Float, maxWidth: Float, maxHeight: Float = Float.MAX_VALUE, indent: Int = 0): String {
        val maxLines = (maxHeight / fontSize).toInt()
        val wrappedString = StringBuilder((toWrap.length * 1.1).toInt())
        var numLines = 0

        // Indentation support
        val indentWith: String = "".padStart(indent)
        val maxWidthWithIndent = maxWidth - calcWidth(indentWith, fontSize)
        if (maxWidthWithIndent <= 0f) return ""

        outer@
        for (rawLine in toWrap.split('\n')) {
            // Can't write to loop parameters in Kotlin, so we need to do this in a separate loop
            var line = rawLine

            if (line.isBlank()) {
                wrappedString.append('\n')
                numLines++
                continue@outer
            }

            inner@
            while (!line.isBlank()) {
                // We've reached our limit
                if (numLines >= maxLines) break

                // Calculate the longest substring that will fit maxWidth
                val tmp = buildUntilLimit(line, fontSize, maxWidthWithIndent)

                // Entire line fits within maxWidth
                if (tmp.length == line.length) {
                    wrappedString.append(indentWith).append(tmp).append('\n')
                    numLines++
                    break@inner
                }
                // String needs to be broken up - here's where things get complicated
                else {
                    // If there's whitespace, break at the last occurrence
                    val lastSpace = tmp.lastIndexOf(' ')
                    if (lastSpace != -1) {
                        wrappedString.append(indentWith).append(line.take(lastSpace)).append('\n')
                        line = if (line.length > lastSpace) line.substring(lastSpace + 1) else ""
                        numLines++
                    }
                    // No whitespace means we need to break up the word with a dash
                    else {
                        while (true) {
                            val splitIndex = (buildUntilLimit("-$line", fontSize, maxWidthWithIndent).length - 1).coerceAtLeast(0)
                            if (splitIndex < line.length) {
                                wrappedString.append(indentWith).append(line.take(splitIndex)).append("-\n")
                                line = line.substring(splitIndex)
                                numLines++
                                continue@inner
                            } else {
                                wrappedString.append(indentWith).append(line).append('\n')
                                numLines++
                                break@inner
                            }
                        }
                    }
                }
            }
        }

        // Don't end with a newline if the source string didn't
        //if (!toWrap.endsWith('\n'))
        wrappedString.deleteCharAt(wrappedString.length - 1)

        return wrappedString.toString()
    }

    fun drawText(text: String?, x: Float, y: Float, fontSize: Float, maxWidth: Float, maxHeight: Float): Vector2f {
        if (text == null || text.isBlank() || maxHeight < fontSize) {
            return Vector2f(0f, 0f)
        }

        var lastChar: LazyChar? = null // For kerning purposes
        val scaleFactor = fontSize / baseHeight
        var xOffset = 0f
        var yOffset = 0f
        var sizeX = 0f
        var sizeY = fontSize
        val toDraw = wrapString(text, fontSize, maxWidth, maxHeight)

        glBindTexture(GL_TEXTURE_2D, textureId)
        glPushAttrib(GL_ENABLE_BIT or GL_COLOR_BUFFER_BIT)
        glEnable(GL_TEXTURE_2D)
        glBlendFunc(GL_ONE, GL_ONE) // TODO: Proper blending
        glEnable(GL_BLEND)
        glPushMatrix()
        glTranslatef(x + 0.01f, y + 0.01f, 0f)
        glBegin(GL_QUADS)

        // TODO: Colored substring support
        outer@
        for (tmp in toDraw) {
            // Ignore tabs and carriage returns
            if (tmp.isWhitespace() && tmp != '\n' && tmp != ' ')
                continue

            // Newline support
            if (tmp == '\n') {
                if (-yOffset + fontSize > maxHeight) {
                    break@outer
                }

                yOffset -= fontSize
                sizeY += fontSize
                sizeX = Math.max(sizeX, xOffset)
                xOffset = 0f
                lastChar = null
                continue@outer
            }

            val ch = getChar(tmp)
            val kerning = if (lastChar == null) 0f else ch.getKerning(lastChar.id) * scaleFactor
            val advance = kerning + ch.advance * scaleFactor
            val chWidth = ch.width * scaleFactor
            val chHeight = ch.height * scaleFactor

            // TODO: If we're very certain of our wrapString() method, this shouldn't be necessary anymore
            if (xOffset + advance > maxWidth) {
                // Check if we're about to exceed the max textbox height
                if (-yOffset + fontSize > maxHeight) {
                    glEnd()
                    glPopMatrix()
                    glDisable(GL_TEXTURE_2D)
                    glPopAttrib()

                    sizeX = Math.max(sizeX, xOffset)
                    return Vector2f(sizeX, sizeY)
                }

                yOffset -= fontSize
                sizeY += fontSize
                sizeX = Math.max(sizeX, xOffset)
                xOffset = -kerning // Not a mistake - negates localX kerning adjustment below
            }

            val localX = xOffset + kerning + ch.xOffset * scaleFactor
            val localY = yOffset - ch.yOffset * scaleFactor

            glTexCoord2f(ch.tx1, ch.ty1)
            glVertex2f(localX, localY)
            glTexCoord2f(ch.tx1, ch.ty2)
            glVertex2f(localX, localY - chHeight)
            glTexCoord2f(ch.tx2, ch.ty2)
            glVertex2f(localX + chWidth, localY - chHeight)
            glTexCoord2f(ch.tx2, ch.ty1)
            glVertex2f(localX + chWidth, localY)

            xOffset += advance
            lastChar = ch
        }

        glEnd()
        glPopMatrix()
        glDisable(GL_TEXTURE_2D)
        glPopAttrib()

        sizeX = Math.max(sizeX, xOffset)
        return Vector2f(sizeX, sizeY)
    }

    @JvmOverloads
    fun createText(text: String = "", color: Color = Color.WHITE, size: Float = baseHeight, maxWidth: Float = Float.MAX_VALUE,
                   maxHeight: Float = Float.MAX_VALUE): DrawableString = DrawableString(text, size, maxWidth, maxHeight, color)

    inner class LazyChar(val id: Int, tx: Int, ty: Int, val width: Int, val height: Int,
                         val xOffset: Int, val yOffset: Int, val advance: Int) {
        val kernings: MutableMap<Int, Int> = HashMap()
        // Internal texture coordinates
        val tx1: Float = tx / textureWidth
        val tx2: Float = tx1 + (width / textureWidth)
        val ty1: Float = (textureHeight - ty) / textureHeight
        val ty2: Float = ty1 - (height / textureHeight)

        fun setKerning(otherCharId: Int, kerning: Int) {
            kernings[otherCharId] = kerning
        }

        fun getKerning(otherCharId: Int): Int {
            return kernings.getOrElse(otherCharId) { 0 }
        }

        fun setKerning(otherChar: Char, kerning: Int) {
            kernings[otherChar.toInt()] = kerning
        }

        fun getKerning(otherChar: Char): Int {
            return kernings.getOrElse(otherChar.toInt()) { 0 }
        }
    }

    inner class DrawableString(text: String, fontSize: Float, maxWidth: Float, maxHeight: Float, var color: Color) {
        private val sb: StringBuilder = StringBuilder(text)
        private val displayListId: Int = glGenLists(1)
        private var needsRebuild = true
        val font: LazyFont get() = this@LazyFont
        var isDisposed = false
            private set
        var width: Float = 0f
            get() {
                checkRebuild()
                return field
            }
            private set
        var height: Float = 0f
            get() {
                checkRebuild()
                return field
            }
            private set
        var fontSize: Float = fontSize
            set(value) {
                if (value != field) {
                    field = value
                    needsRebuild = true
                }
            }
        var maxWidth: Float = maxWidth
            set(value) {
                if (value != field) {
                    field = value
                    needsRebuild = true
                }
            }
        var maxHeight: Float = maxHeight
            set(value) {
                if (value != field) {
                    field = value
                    needsRebuild = true
                }
            }
        var text: String
            get() = sb.toString()
            set(value) {
                sb.setLength(0)
                appendText(value)
            }

        fun appendText(text: String) {
            sb.append(text)
            needsRebuild = true
        }

        fun appendText(text: String, indent: Int) {
            sb.append(wrapString(text, fontSize, maxWidth, maxHeight, indent))
            needsRebuild = true
        }

        private fun checkRebuild() {
            if (isDisposed) throw RuntimeException("Tried to draw using a disposed of DrawableString!")
            if (!needsRebuild) return

            glPushAttrib(GL_ENABLE_BIT)
            glNewList(displayListId, GL_COMPILE)
            val tmp: Vector2f = drawText(text, 0.01f, 0.01f, fontSize, maxWidth, maxHeight)
            glEndList()
            glPopAttrib()

            width = tmp.x
            height = tmp.y
            needsRebuild = false
        }

        fun draw(x: Float, y: Float) {
            checkRebuild()

            glPushMatrix()
            glTranslatef(x, y, 0.01f)
            glColor(color)
            glCallList(displayListId)
            glPopMatrix()
        }

        fun draw(location: Vector2f) = draw(location.x, location.y)

        fun drawAtAngle(x: Float, y: Float, angle: Float) {
            checkRebuild()

            glPushMatrix()
            glTranslatef(x, y, 0f)
            glRotatef(angle, 0f, 0f, 1f)
            glColor(color)
            glCallList(displayListId)
            glPopMatrix()
        }

        fun drawAtAngle(location: Vector2f, angle: Float) = drawAtAngle(location.x, location.y, angle)

        fun dispose() {
            if (!isDisposed) glDeleteLists(displayListId, 1)
            isDisposed = true
        }

        @Suppress("ProtectedInFinal")
        protected fun finalize() {
            if (!isDisposed) {
                Log.debug("DrawableString cleaned up in finalizer (not disposed of before discarded)")
                dispose()
            }
        }
    }
}

class FontException : Exception {
    constructor(message: String) : super(message)
    constructor(message: String, cause: Throwable) : super(message, cause)
    constructor(cause: Throwable) : super(cause)
}
