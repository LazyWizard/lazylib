package org.lazywizard.lazylib.campaign;

import com.fs.starfarer.api.Global;
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
     * {@link MessageUtils#showMessage(java.lang.String)}
     */
    public static final int LINE_LENGTH = 80;

    /**
     * Formats and word-wraps the supplied text, then outputs it to the player.
     *
     * @param preamble The header for this message, won't be indented.
     * @param message  The main body of text.
     * @param indent   Whether to indent each line of the main body.
     * <p>
     * @since 1.0
     */
    public static void showMessage(String preamble,
            String message, boolean indent)
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
                if (indent)
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
     * Formats and word-wraps the supplied text, then outputs it to the player.
     *
     * @param message The message to output.
     * <p>
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
