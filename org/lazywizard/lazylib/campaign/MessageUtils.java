package org.lazywizard.lazylib.campaign;

import com.fs.starfarer.api.Global;

/**
 * Allows formatted, multi-line, word-wrapped sector messages
 * @author LazyWizard
 */
public class MessageUtils
{
    /** How long a line can be before being split by {@link MessageUtils#showMessage(java.lang.String)} */
    public static final int LINE_LENGTH = 80;

    /**
     * Formats and word-wraps the supplied text, then outputs it to the player.
     *
     * @param preamble the header for this message, won't be indented
     * @param message the main body of text
     * @param indent whether to indent each line of the main body
     */
    public static void showMessage(String preamble,
            String message, boolean indent)
    {
        if (preamble != null)
        {
            showMessage(preamble);
        }

        if (message == null)
        {
            message = "";
        }

        // Analyse each line of the message seperately
        String[] lines = message.split("\n");
        StringBuilder line = new StringBuilder(LINE_LENGTH);

        // Word wrapping is complicated ;)
        for (int x = 0; x < lines.length; x++)
        {
            // Check if the string even needs to be broken up
            if (lines[x].length() > LINE_LENGTH)
            {
                // Clear the StringBuilder so we can generate a new line
                line.setLength(0);
                // Split the line up into the individual words, and append each
                // word to the next line until the character limit is reached
                String[] words = lines[x].split(" ");
                for (int y = 0; y < words.length; y++)
                {
                    // If this word by itself is longer than the line limit,
                    // just go ahead and post it in its own line
                    if (words[y].length() > LINE_LENGTH)
                    {
                        // Make sure to post the previous line in queue, if any
                        if (line.length() > 0)
                        {
                            printLine(line.toString(), indent);
                            line.setLength(0);
                        }

                        printLine(words[y], indent);
                    }
                    // If this word would put us over the length limit, post
                    // the queue and back up a step (re-check this word with
                    // a blank line - this is in case it trips the above block)
                    else if (words[y].length() + line.length() > LINE_LENGTH)
                    {
                        printLine(line.toString(), indent);
                        line.setLength(0);
                        y--;
                    }
                    // This word won't put us over the limit, add it to the queue
                    else
                    {
                        line.append(words[y]);
                        line.append(" ");

                        // If we have reached the end of the message, ensure
                        // that we post the remaining part of the queue
                        if (y == (words.length - 1))
                        {
                            printLine(line.toString(), indent);
                        }
                    }
                }
            }
            // Entire message fits into a single line
            else
            {
                printLine(lines[x], indent);
            }
        }
    }

    /**
     * Formats and word-wraps the supplied text, then outputs it to the player.
     *
     * @param message the message to output
     */
    public static void showMessage(String message)
    {
        showMessage(null, message, false);
    }

    private static void printLine(String message, boolean indent)
    {
        if (indent)
        {
            Global.getSector().addMessage("   " + message);
        }
        else
        {
            Global.getSector().addMessage(message);
        }
    }

    private MessageUtils()
    {
    }
}
