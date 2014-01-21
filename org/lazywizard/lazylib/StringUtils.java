package org.lazywizard.lazylib;

/**
 *
 * @author LazyWizard
 * @since 1.8
 */
public class StringUtils
{
    /**
     *
     * @param toWrap
     * @param maxLineLength
     * @param indent
     *                      <p>
     * @return
     *         <p>
     * @since 1.8
     */
    public static String wrapString(String toWrap, int maxLineLength,
            String indent)
    {
        if (indent == null)
        {
            indent = "";
        }

        if (toWrap == null)
        {
            return indent;
        }

        // Analyse each line of the message seperately
        String[] lines = toWrap.split("\n");
        StringBuilder line = new StringBuilder(maxLineLength);
        StringBuilder message = new StringBuilder((int) (toWrap.length() * 1.1f));
        for (String rawLine : lines)
        {
            // Check if the string even needs to be broken up
            if (rawLine.length() <= maxLineLength)
            {
                // Entire message fits into a single line
                message.append(indent).append(rawLine).append("\n");
            }
            else
            {
                // Clear the StringBuilder so we can generate a new line
                line.setLength(0);
                // Split the line up into the individual words, and append each
                // word to the next line until the character limit is reached
                String[] words = rawLine.split(" ");
                for (int y = 0; y < words.length; y++)
                {
                    // If this word by itself is longer than the line limit,
                    // just go ahead and post it in its own line
                    if (words[y].length() > maxLineLength)
                    {
                        // Make sure to post the previous line in queue, if any
                        if (line.length() > 0)
                        {
                            message.append(indent).append(line.toString()).append("\n");
                            line.setLength(0);
                        }

                        // TODO: split line at maxLineLength and add dash/newline
                        message.append(indent).append(words[y]).append("\n");
                    }
                    // If this word would put us over the length limit, post
                    // the queue and back up a step (re-check this word with
                    // a blank line - this is in case it trips the above block)
                    else if (words[y].length() + line.length() > maxLineLength)
                    {
                        message.append(indent).append(line.toString()).append("\n");
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
                            message.append(indent).append(line.toString()).append("\n");
                        }
                    }
                }
            }
        }

        return message.toString();
    }

    public static void main(String[] args)
    {
        System.out.println(wrapString("Test\ntestklsdfja dklsjfklassd flasdjklfj"
                + " sdlfjsdlakj, jfladksjfkl dkltjalsdkjf lasdjkl\n  tesw"
                + "adfjklsjagkl;asdjflkasdjgl;kjasdkl;fjsdlak;jflasdk;jgasdjhfjasdh"
                + "dfhasklfjlasdkjglkasdjflksdja",
                80, "   "));
    }
}
