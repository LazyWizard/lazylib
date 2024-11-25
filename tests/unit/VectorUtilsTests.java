import org.junit.Test;
import org.lazywizard.lazylib.MathUtils;
import org.lazywizard.lazylib.ShapeUtils;
import org.lwjgl.util.vector.Vector2f;

import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.lazywizard.lazylib.VectorUtils.toFloatArray;
import static org.lazywizard.lazylib.VectorUtils.toVectorList;

public class VectorUtilsTests
{
    private static final int NUM_TESTS = 1_200_000;

    @Test
    public void testGetFacing()
    {

    }

    @Test
    public void testGetAngle()
    {

    }

    @Test
    public void testGetDirectionalVector()
    {

    }

    @Test
    public void testGetCrossProduct()
    {

    }

    @Test
    public void testResize()
    {

    }

    @Test
    public void testClampLength()
    {

    }

    @Test
    public void testRotate()
    {
        // Also include rotateAroundPivot()
    }

    @Test
    public void testToVectorList()
    {
        for (int i = 1; i <= NUM_TESTS; i++)
        {
            final float[] asArray = ShapeUtils.createCircle(MathUtils.getRandomNumberInRange(-5000, 5000),
                    MathUtils.getRandomNumberInRange(-5000, 5000),
                    MathUtils.getRandomNumberInRange(500, 5000),
                    MathUtils.getRandomNumberInRange(10, 1000));
            final List<Vector2f> asList = toVectorList(asArray);
            assertEquals(asList.size(), asArray.length / 2);
            for (int j = 0; j < asList.size(); j++)
            {
                final Vector2f vector = asList.get(j);
                assertEquals("Test " + i + " failed on x coordinate: ",
                        asArray[j * 2], vector.x, 0f);
                assertEquals("Test " + i + " failed on y coordinate: ",
                        asArray[j * 2 + 1], vector.y, 0f);
            }
        }
    }

    @Test
    public void testToFloatArray()
    {
        for (int i = 1; i <= NUM_TESTS; i++)
        {
            final float[] asArray = ShapeUtils.createCircle(MathUtils.getRandomNumberInRange(-5000, 5000),
                    MathUtils.getRandomNumberInRange(-5000, 5000),
                    MathUtils.getRandomNumberInRange(500, 5000),
                    MathUtils.getRandomNumberInRange(10, 1000));
            final List<Vector2f> asList = toVectorList(asArray);
            assertArrayEquals("Reconverted array does not match original",
                    asArray, toFloatArray(asList), 0f);
        }
    }
}
