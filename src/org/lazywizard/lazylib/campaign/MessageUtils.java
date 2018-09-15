package org.lazywizard.lazylib.campaign;

import com.fs.starfarer.api.Global;
import org.jetbrains.annotations.Nullable;
import org.lazywizard.lazylib.StringUtils;

/**
 * Allows formatted, multi-line, word-wrapped sector messages
 *
 * @author LazyWizard
 * @since 1.0
 */
public class MessageUtils
{
    /**
     * How long a line can be before being split by
     * {@link MessageUtils#showMessage(java.lang.String)}. This is hardcoded to
     * a number that still looks good at very low resolutions. If you wish to
     * have a different message length, you can use {@link
     * StringUtils#wrapString(java.lang.String, int)} then output the results.
     */
    public static final int LINE_LENGTH = 80;

    /**
     * Formats and word-wraps the supplied text, then outputs it as a sector
     * message.
     *
     * @param preamble      The header for this message, won't be indented. Can be null.
     * @param message       The main body of text.
     * @param indentMessage Whether to indent each line of the main body.
     *
     * @since 1.0
     */
    public static void showMessage(@Nullable String preamble,
                                   String message, boolean indentMessage)
    {
        if (preamble != null)
        {
            for (String tmp : StringUtils.wrapString(preamble, LINE_LENGTH).split("\n"))
            {
                Global.getSector().getCampaignUI().addMessage(tmp);
            }
        }

        if (message != null)
        {
            for (String tmp : StringUtils.wrapString(message, LINE_LENGTH).split("\n"))
            {
                if (indentMessage)
                {
                    Global.getSector().getCampaignUI().addMessage("   " + tmp);
                }
                else
                {
                    Global.getSector().getCampaignUI().addMessage(tmp);
                }
            }
        }
    }

    /**
     * Formats and word-wraps the supplied text, then outputs it as a sector
     * message.
     *
     * @param message The message to output.
     *
     * @since 1.0
     */
    public static void showMessage(String message)
    {
        showMessage(null, message, false);
    }

    private MessageUtils()
    {
    }
}
