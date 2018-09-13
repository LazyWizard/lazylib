package org.lazywizard.lazylib.ui;

/**
 * Thrown if {@link LazyFont#loadFont(String)} encounters an error loading or parsing a bitmap font.
 * @author LazyWizard
 * @since 3.0
 */
public class FontException extends Exception
{
    public FontException(String message)
    {
    }

    public FontException(String message, Throwable cause)
    {
    }

    public FontException(Throwable cause)
    {
    }
}
