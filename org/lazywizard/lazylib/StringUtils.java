package org.lazywizard.lazylib;

/**
 * Contains methods for working with {@link String}s.
 *
 * @author LazyWizard
 * @since 1.8
 */
public class StringUtils
{
    /**
     * Word-wraps a {@link String} (ensures it fits within a certain width).
     *
     * @param toWrap        The {@link String} to be word-wrapped.
     * @param maxLineLength How long a line can reach before it's split in two.
     * <p>
     * @return {@code toWrap} wrapped to {@code maxLineLength} width.
     * <p>
     * @since 1.8
     */
    // TODO: This whole thing could be rewritten far more efficiently
    public static String wrapString(String toWrap, int maxLineLength)
    {
        if (toWrap == null)
        {
            return "";
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
                message.append(rawLine).append("\n");
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
                            message.append(line.toString()).append("\n");
                            line.setLength(0);
                        }

                        // TODO: split line at maxLineLength and add dash/newline
                        message.append(words[y]).append("\n");
                    }
                    // If this word would put us over the length limit, post
                    // the queue and back up a step (re-check this word with
                    // a blank line - this is in case it trips the above block)
                    else if (words[y].length() + line.length() > maxLineLength)
                    {
                        message.append(line.toString()).append("\n");
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
                            message.append(line.toString()).append("\n");
                        }
                    }
                }
            }
        }

        return message.toString();
    }

    /**
     * Prepends every line of a {@link String} with another {@link String}.
     * <p>
     * @param toIndent   The {@link String} to be indented.
     * @param indentWith The {@link String} to indent each line with. For
     *                   example, a bulleted list could be created by passing in
     *                   {@code " * "}.
     * <p>
     * @return {@code toIndent} with every line prepended with
     *         {@code indentWith}.
     * <p>
     * @since 1.8
     */
    public static String indent(String toIndent, String indentWith)
    {
        StringBuilder output = new StringBuilder((int) (toIndent.length() * 1.2f));

        for (String tmp : toIndent.split("\n"))
        {
            output.append(indentWith).append(tmp);
        }

        return output.toString();
    }

    private StringUtils()
    {
    }
}
