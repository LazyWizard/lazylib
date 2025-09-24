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
     * Note: this does not work properly with text containing tab characters
     * ({@code \t}).
     *
     * @param toWrap        The {@link String} to be word-wrapped.
     * @param maxLineLength How long a line can reach before it's split in two.
     *                      Must be at least 2, otherwise it will return an
     *                      empty String.
     *
     * @return {@code toWrap} wrapped to {@code maxLineLength} width.
     *
     * @since 1.8
     */
    public static String wrapString(String toWrap, int maxLineLength)
    {
        if (toWrap == null || maxLineLength <= 1)
        {
            return "";
        }

        // Analyze each line of the message separately
        String[] lines = toWrap.split("\n");
        // StringBuilder doesn't auto-resize down, so setting the length here
        // is an optimization even though length is reset to 0 each line
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
                    // break it up into multiple sub-lines separated by a dash
                    if (words[y].length() >= maxLineLength)
                    {
                        // Make sure to post the previous line in the queue, if any
                        if (!line.isEmpty())
                        {
                            message.append(line).append("\n");
                            line.setLength(0);
                        }

                        // Break up word into multiple lines separated with a dash
                        while (words[y].length() > maxLineLength)
                        {
                            message.append(words[y], 0, maxLineLength - 1).append("-\n");
                            words[y] = words[y].substring(maxLineLength - 1);
                        }

                        // Add any remaining text to the next line
                        if (!words[y].isEmpty())
                        {
                            // If we have reached the end of the message, ensure
                            // that we post the remaining part of the queue
                            if (y == (words.length - 1))
                            {
                                message.append(words[y]).append("\n");
                            }
                            else
                            {
                                line.append(words[y]);
                            }
                        }
                    }
                    // If this word would put us over the length limit, post
                    // the queue and back up a step (re-check this word with
                    // a blank line - this is in case it trips the above block)
                    else if (words[y].length() + line.length() >= maxLineLength)
                    {
                        message.append(line).append("\n");
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
                            message.append(line).append("\n");
                        }
                    }
                }
            }
        }

        // Don't end with a newline if the original string didn't
        if (!toWrap.endsWith("\n"))
        {
            message.deleteCharAt(message.length() - 1);
        }

        return message.toString();
    }

    /**
     * Prepends every line of a {@link String} with another {@link String}.
     *
     * @param toIndent   The {@link String} to be indented.
     * @param indentWith The {@link String} to prefix each line with. For
     *                   example, a bulleted list could be created by passing in
     *                   {@code " * "}.
     *
     * @return {@code toIndent} with every line prepended with
     *         {@code indentWith}.
     *
     * @since 1.8
     */
    public static String indent(String toIndent, String indentWith)
    {
        String[] lines = toIndent.split("\n", -1);
        StringBuilder output = new StringBuilder(toIndent.length()
                + (indentWith.length() * lines.length));
        for (int x = 0; x < lines.length; x++)
        {
            output.append(indentWith).append(lines[x]);

            if (x < (lines.length - 1))
            {
                output.append("\n");
            }
        }

        return output.toString();
    }

    private StringUtils()
    {
    }
}
