import org.junit.Test;
import org.lazywizard.lazylib.FastTrig;
import org.lazywizard.lazylib.MathUtils;

import static org.junit.Assert.assertEquals;

public class FastTrigTests
{
    private static final int NUM_TESTS = 2_000_000;

    @Test
    public void testFastTrig()
    {
        for (int i = 1; i <= NUM_TESTS; i++)
        {
            final double rand = MathUtils.getRandomNumberInRange(-1000f, 1000f),
                    rand2 = MathUtils.getRandomNumberInRange(-1000f, 1000f);
            assertEquals("Test " + i + " failed: sin(" + rand + ")",
                    Math.sin(rand), FastTrig.sin(rand), 0.005f);
            assertEquals("Test " + i + " failed: cos(" + rand + ")",
                    Math.cos(rand), FastTrig.cos(rand), 0.005f);
            assertEquals("Test " + i + " failed: atan(" + rand + ")",
                    Math.atan(rand), FastTrig.atan(rand), 0.005f);
            assertEquals("Test " + i + " failed: atan2(" + rand + ", " + rand2 + ")",
                    Math.atan2(rand, rand2), FastTrig.atan2(rand, rand2), 0.05f);
        }
    }
}
