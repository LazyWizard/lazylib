package org.lazywizard.lazylib;

import com.fs.starfarer.api.Global;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Arrays;

/**
 * Contains classes for reading and writing files within the game's classloader restrictions.
 *
 * @author LazyWizard
 * @since 2.6
 */
public class IOUtils
{
    private static final int MAX_BUFFER_SIZE = Integer.MAX_VALUE - 8;
    private static final int BUFFER_SIZE = 8192;

    /**
     * This is a port of {@link java.nio.file.Files#readAllBytes(Path)}
     * that has been modified to work within the game's classloader restrictions.
     *
     * @param filePath The local file path of the file to read.
     *
     * @return The contents of the file at {@code filePath} as a byte array.
     *
     * @since 2.6
     */
    public static byte[] readAllBytes(String filePath) throws IOException
    {
        try (final InputStream source = Global.getSettings().openStream(filePath))
        {
            int capacity = source.available();
            byte[] buf = new byte[capacity];
            int nread = 0;
            int n;
            while (true)
            {
                // read to EOF which may read more or less than initialSize (eg: file
                // is truncated while we are reading)
                while ((n = source.read(buf, nread, capacity - nread)) > 0)
                {
                    nread += n;
                }

                // if last call to source.read() returned -1, we are done
                // otherwise, try to read one more byte; if that failed we're done too
                if (n < 0 || (n = source.read()) < 0)
                {
                    break;
                }

                // one more byte was read; need to allocate a larger buffer
                if (capacity <= MAX_BUFFER_SIZE - capacity)
                {
                    capacity = Math.max(capacity << 1, BUFFER_SIZE);
                }
                else
                {
                    if (capacity == MAX_BUFFER_SIZE)
                    {
                        throw new OutOfMemoryError("Required array size too large");
                    }
                    capacity = MAX_BUFFER_SIZE;
                }
                buf = Arrays.copyOf(buf, capacity);
                buf[nread++] = (byte) n;
            }

            return (capacity == nread) ? buf : Arrays.copyOf(buf, nread);
        }
    }

    private IOUtils()
    {
    }
}
