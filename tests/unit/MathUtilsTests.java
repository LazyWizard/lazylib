import org.junit.Test;
import org.lazywizard.lazylib.FastTrig;
import org.lazywizard.lazylib.MathUtils;
import org.lazywizard.lazylib.VectorUtils;
import org.lwjgl.util.vector.Vector2f;

import java.text.NumberFormat;

import static org.junit.Assert.*;
import static org.lazywizard.lazylib.MathUtils.*;

// Every test should have two parts: a loop to confirm the consistency of results,
// and testing return values with known arguments to check the method's correctness
public class MathUtilsTests
{
    private static final int NUM_TESTS = 1_200_000;

    @Test
    public void testGetDistance()
    {
        // Ensure distances are the same regardless of call order
        for (int i = 0; i < NUM_TESTS; i++)
        {
            final float distanceX = MathUtils.getRandomNumberInRange(-150_000f, 150_000f),
                    distanceY = MathUtils.getRandomNumberInRange(-150_000f, 150_000f);
            final Vector2f origin1 = new Vector2f(0f, 0f),
                    end1 = new Vector2f(origin1.x + distanceX, origin1.y + distanceY),
                    origin2 = new Vector2f(-distanceX, -distanceY),
                    end2 = new Vector2f(0f, 0f);

            assertEquals(getDistance(origin1, end1),
                    getDistance(origin2, end2), 0f);
            assertEquals(getDistance(origin1, end1),
                    getDistance(end1, origin1), 0f);
            assertEquals(getDistance(origin2, end2),
                    getDistance(end2, origin2), 0f);
        }

        assertEquals(50f, getDistance(new Vector2f(0f, 0f),
                new Vector2f(50f, 0f)), 0f);
        assertEquals(50f, getDistance(new Vector2f(0f, 0f),
                new Vector2f(0f, 50f)), 0f);
        assertEquals(50f, getDistance(new Vector2f(0f, 0f),
                new Vector2f(-50f, 0f)), 0f);
        assertEquals(50f, getDistance(new Vector2f(0f, 0f),
                new Vector2f(0f, -50f)), 0f);
        assertEquals(70.7106f, getDistance(new Vector2f(0f, 0f),
                new Vector2f(50f, 50f)), 0.0001f);
    }

    @Test
    public void testGetDistanceSquared()
    {
        // Ensure distances are the same regardless of call order
        for (int i = 0; i < NUM_TESTS; i++)
        {
            final float distanceX = MathUtils.getRandomNumberInRange(-150_000f, 150_000f),
                    distanceY = MathUtils.getRandomNumberInRange(-150_000f, 150_000f);
            final Vector2f origin1 = new Vector2f(0f, 0f),
                    end1 = new Vector2f(origin1.x + distanceX, origin1.y + distanceY),
                    origin2 = new Vector2f(-distanceX, -distanceY),
                    end2 = new Vector2f(0f, 0f);

            assertEquals(getDistanceSquared(origin1, end1),
                    getDistanceSquared(origin2, end2), 0f);
            assertEquals(getDistanceSquared(origin1, end1),
                    getDistanceSquared(end1, origin1), 0f);
            assertEquals(getDistanceSquared(origin2, end2),
                    getDistanceSquared(end2, origin2), 0f);
        }

        assertEquals(2_500f, getDistanceSquared(new Vector2f(0f, 0f),
                new Vector2f(50f, 0f)), 0f);
        assertEquals(2_500f, getDistanceSquared(new Vector2f(0f, 0f),
                new Vector2f(0f, 50f)), 0f);
        assertEquals(2_500f, getDistanceSquared(new Vector2f(0f, 0f),
                new Vector2f(-50f, 0f)), 0f);
        assertEquals(2_500f, getDistanceSquared(new Vector2f(0f, 0f),
                new Vector2f(0f, -50f)), 0f);
        assertEquals(5_000f, getDistanceSquared(new Vector2f(0f, 0f),
                new Vector2f(50f, 50f)), 0f);
    }

    @Test
    public void testClampAngle()
    {
        // Test bounds with random values (doesn't test exact return value; that's done later)
        for (int i = 0; i < NUM_TESTS; i++)
        {
            final float clamped = clampAngle(getRandomNumberInRange(Float.MIN_VALUE, Float.MAX_VALUE));
            assertTrue("Angle not within 0-360 degrees: " +
                            NumberFormat.getInstance().format(clamped),
                    (clamped < 360f && clamped >= 0f));
        }

        // Test return value with known arguments
        assertEquals(60f, clampAngle(420f), 0f);
        assertEquals(300f, clampAngle(-420f), 0f);
        assertEquals(0f, clampAngle(0f), 0f);
        assertEquals(0f, clampAngle(360f), 0f);
        assertEquals(120f, clampAngle(120f), 0f);
    }

    @Test
    public void testClamp()
    {
        for (int i = 0; i < NUM_TESTS; i++)
        {
            float ranF = getRandomNumberInRange(-10f, 10f);
            assertTrue(Math.abs(clamp(ranF, -5f, 5f)) <= 5f);

            int ranI = getRandomNumberInRange(-10, 10);
            assertTrue(Math.abs(clamp(ranI, -5, 5)) <= 5);
        }

        // Float variant
        assertEquals(3f, clamp(5f, 1f, 3f), 0f);
        assertEquals(1f, clamp(-5f, 1f, 3f), 0f);
        assertEquals(2f, clamp(2f, 1f, 3f), 0f);

        // Integer variant
        assertEquals(3, clamp(5, 1, 3));
        assertEquals(1, clamp(-5, 1, 3));
        assertEquals(2, clamp(2, 1, 3));
    }

    @Test
    public void testGetShortestRotation()
    {
        for (int i = 0; i < NUM_TESTS; i++)
        {
            final float currAngle = getRandomNumberInRange(-360f, 360f),
                    destAngle = getRandomNumberInRange(-360f, 360f),
                    rotation = getShortestRotation(currAngle, destAngle);
            assertTrue("Shortest rotation is not less than 180 degrees: "
                            + currAngle + " to " + destAngle + " -> " + rotation,
                    Math.abs(rotation) <= 180f);
        }

        // Test return value with known arguments
        assertEquals(90f, getShortestRotation(90f, 180f), 0f);
        assertEquals(-90f, getShortestRotation(180f, 90f), 0f);
        assertEquals(179f, getShortestRotation(0f, 179f), 0f);
        assertEquals(-179f, getShortestRotation(0f, 181f), 0.01f);
        assertEquals(0f, getShortestRotation(0f, 360f), 0f);
        assertEquals(60f, getShortestRotation(60f, 120f), 0f);
        assertEquals(-60f, getShortestRotation(120f, 60f), 0f);
        assertEquals(180f, Math.abs(getShortestRotation(300f, 120f)), 0.001f);
    }

    //@Test
    public void testGetMidpoint()
    {
        // TODO: test return value with known arguments
    }

    @Test
    public void testGetPoint()
    {
        // Generate random points and check that they return the correct positions
        for (int i = 0; i < NUM_TESTS; i++)
        {
            final Vector2f origin = getRandomPointInCircle(null, 150_000f);
            final float angle = getRandomNumberInRange(0f, 359.9f);
            final Vector2f point = getPoint(origin, 100f, angle);
            assertEquals(10_000f, getDistanceSquared(point, origin), 2f);
            assertEquals(angle, VectorUtils.getAngleStrict(origin, point), 0.01f);
        }

        // Test return value with known arguments
        assertEquals(new Vector2f(50f, 0f), getPoint(null, 50f, 0f));
        assertEquals(new Vector2f(0f, 50f), getPoint(null, 50f, 90f));
        assertEquals(new Vector2f(-50f, 0f), getPoint(null, 50f, 180f));
        assertEquals(new Vector2f(0f, -50f), getPoint(null, 50f, 270f));
        assertEquals(new Vector2f(35.35534f, 35.35534f), getPoint(null, 50f, 45f));
    }

    @Test
    public void testGetRandomPointOnLine()
    {
        // TODO
    }

    @Test
    public void testIsPointWithinCircle()
    {
        for (int i = 0; i < NUM_TESTS; i++)
        {
            final Vector2f origin = getRandomPointInCircle(null, 1_500f),
                    inCircle = getRandomPointInCircle(origin, 499.999f),
                    notInCircle = getRandomPointOnCircumference(origin, 500.001f);
            assertTrue(isPointWithinCircle(inCircle, origin, 500f));
            assertFalse(isPointWithinCircle(notInCircle, origin, 500f));
        }
    }

    //@Test
    public void testIsPointOnLine()
    {
        // TODO: test return value with known arguments
    }

    @Test
    public void testGetRandomNumberInRange()
    {
        final float minF = -1f, maxF = 1f;
        float totalF = 0f;
        final int minI = -100, maxI = 100;
        int totalI = 0;
        for (int i = 0; i < NUM_TESTS; i++)
        {
            final float fNum = getRandomNumberInRange(minF, maxF);
            assertTrue(fNum >= minF && fNum < maxF);
            totalF += fNum;

            final int iNum = getRandomNumberInRange(minI, maxI);
            assertTrue(iNum >= minI && iNum <= maxI);
            totalI += iNum;
        }

        // Check for even distribution
        assertEquals(0f, totalF / NUM_TESTS, 0.01f);
        assertEquals(0, totalI / NUM_TESTS);
    }

    @Test
    public void testFastTrig()
    {
        for (int i = 0; i < NUM_TESTS; i++)
        {
            final double rads = Math.toRadians(getRandomNumberInRange(-360f, 360f));
            assertEquals(Math.cos(rads), FastTrig.cos(rads), 0.001f);
            assertEquals(Math.sin(rads), FastTrig.sin(rads), 0.001f);
        }
    }
}
