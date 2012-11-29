package data.scripts.lazylib;

import org.lwjgl.util.vector.Vector2f;

public class Line
{
    private Vector2f start, end;

    public Line(Vector2f start, Vector2f end)
    {
        this.start = start;
        this.end = end;
    }

    public Vector2f getStart()
    {
        return start;
    }

    public Vector2f getEnd()
    {
        return end;
    }

    // This is taken from the Slick library, modded to work with LWJGL Vector2f
    public boolean intersect(Line other, boolean limit, Vector2f result)
    {
        float dx1 = end.getX() - start.getX();
        float dx2 = other.end.getX() - other.start.getX();
        float dy1 = end.getY() - start.getY();
        float dy2 = other.end.getY() - other.start.getY();
        float denom = (dy2 * dx1) - (dx2 * dy1);

        if (denom == 0)
        {
            return false;
        }

        float ua = (dx2 * (start.getY() - other.start.getY()))
                - (dy2 * (start.getX() - other.start.getX()));
        ua /= denom;
        float ub = (dx1 * (start.getY() - other.start.getY()))
                - (dy1 * (start.getX() - other.start.getX()));
        ub /= denom;

        if ((limit) && ((ua < 0) || (ua > 1) || (ub < 0) || (ub > 1)))
        {
            return false;
        }

        float u = ua;

        float ix = start.getX() + (u * (end.getX() - start.getX()));
        float iy = start.getY() + (u * (end.getY() - start.getY()));

        result.set(ix, iy);
        return true;
    }
}
