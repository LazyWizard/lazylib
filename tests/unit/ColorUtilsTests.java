import org.junit.Test;
import org.lazywizard.lazylib.MathUtils;

import java.awt.Color;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ColorUtilsTests
{
    private static final int NUM_TESTS = 1_200_000;

    @Test
    public void testGenRandomColor()
    {
        int totalRed = 0, totalGreen = 0, totalBlue = 0, totalAlpha = 0;
        final float[] colComponents = new float[4];
        for (int i = 0; i < NUM_TESTS; i++)
        {
            final Color color = new Color(MathUtils.getRandomNumberInRange(0, 0xff000000));
            for (float component : color.getRGBColorComponents(colComponents))
            {
                assertTrue(component >= 0f && component <= 1f);
            }

            totalRed += color.getRed();
            totalGreen += color.getGreen();
            totalBlue += color.getBlue();
            totalAlpha += color.getAlpha();
        }

        // Ensure equal distribution of colors
        assertEquals(127, totalRed / NUM_TESTS);
        assertEquals(127, totalGreen / NUM_TESTS);
        assertEquals(127, totalBlue / NUM_TESTS);
        assertEquals(255, totalAlpha / NUM_TESTS);
    }
}
