package org.lazywizard.lazylib.geom;

import org.lwjgl.util.vector.Vector2f;

/**
 * A simple Circle geometry
 *
 * @author Kevin Glass
 */
public strictfp class Circle extends Ellipse
{
    /** The radius of the circle */
    public float radius;

    /**
     * Create a new circle based on its radius
     *
     * @param centerPointX The x location of the center of the circle
     * @param centerPointY The y location of the center of the circle
     * @param radius The radius of the circle
     */
    public Circle(float centerPointX, float centerPointY, float radius)
    {
        this(centerPointX, centerPointY, radius, DEFAULT_SEGMENT_COUNT);
    }

    /**
     * Create a new circle based on its radius
     *
     * @param centerPointX The x location of the center of the circle
     * @param centerPointY The y location of the center of the circle
     * @param radius The radius of the circle
     * @param segmentCount The number of segments to build the circle out of
     */
    public Circle(float centerPointX, float centerPointY, float radius, int segmentCount)
    {
        super(centerPointX, centerPointY, radius, radius, segmentCount);
        this.x = centerPointX - radius;
        this.y = centerPointY - radius;
        this.radius = radius;
        boundingCircleRadius = radius;
    }

    /**
     * Get the x coordinate of the centre of the circle
     *
     * @return The x coordinate of the centre of the circle
     */
    @Override
    public float getCenterX()
    {
        return getX() + radius;
    }

    /**
     * Get the y coordinate of the centre of the circle
     *
     * @return The y coordinate of the centre of the circle
     */
    @Override
    public float getCenterY()
    {
        return getY() + radius;
    }

    /**
     * Set the radius of this circle
     *
     * @param radius The radius of this circle
     */
    public void setRadius(float radius)
    {
        if (radius != this.radius)
        {
            pointsDirty = true;
            this.radius = radius;
            setRadii(radius, radius);
        }
    }

    /**
     * Get the radius of the circle
     *
     * @return The radius of the circle
     */
    public float getRadius()
    {
        return radius;
    }

    /**
     * Check if this circle touches another
     *
     * @param shape The other circle
     * @return True if they touch
     */
    @Override
    public boolean intersects(Shape shape)
    {
        if (shape instanceof Circle)
        {
            Circle other = (Circle) shape;
            float totalRad2 = getRadius() + other.getRadius();

            if (Math.abs(other.getCenterX() - getCenterX()) > totalRad2)
            {
                return false;
            }
            if (Math.abs(other.getCenterY() - getCenterY()) > totalRad2)
            {
                return false;
            }

            totalRad2 *= totalRad2;

            float dx = Math.abs(other.getCenterX() - getCenterX());
            float dy = Math.abs(other.getCenterY() - getCenterY());

            return totalRad2 >= ((dx * dx) + (dy * dy));
        }
        else if (shape instanceof Rectangle)
        {
            return intersects((Rectangle) shape);
        }
        else
        {
            return super.intersects(shape);
        }
    }

    /**
     * Check if a point is contained by this circle
     *
     * @param x The x coordinate of the point to check
     * @param y The y coordinate of the point to check
     * @return True if the point is contained by this circle
     */
    @Override
    public boolean contains(float x, float y)
    {
        return (x - getX()) * (x - getX()) + (y - getY()) * (y - getY()) < getRadius() * getRadius();
    }

    /**
     * Check if circle contains the line
     * @param line Line to check against
     * @return True if line inside circle
     */
    private boolean contains(Line line)
    {
        return contains(line.getX1(), line.getY1()) && contains(line.getX2(), line.getY2());
    }

    /**
     * @see org.newdawn.slick.geom.Ellipse#findCenter()
     */
    @Override
    protected void findCenter()
    {
        center = new float[2];
        center[0] = x + radius;
        center[1] = y + radius;
    }

    /**
     * @see org.newdawn.slick.geom.Ellipse#calculateRadius()
     */
    @Override
    protected void calculateRadius()
    {
        boundingCircleRadius = radius;
    }

    /**
     * Check if this circle touches a rectangle
     *
     * @param other The rectangle to check against
     * @return True if they touch
     */
    private boolean intersects(Rectangle other)
    {
        Rectangle box = other;
        Circle circle = this;

        if (box.contains(x + radius, y + radius))
        {
            return true;
        }

        float x1 = box.getX();
        float y1 = box.getY();
        float x2 = box.getX() + box.getWidth();
        float y2 = box.getY() + box.getHeight();

        Line[] lines = new Line[4];
        lines[0] = new Line(x1, y1, x2, y1);
        lines[1] = new Line(x2, y1, x2, y2);
        lines[2] = new Line(x2, y2, x1, y2);
        lines[3] = new Line(x1, y2, x1, y1);

        float r2 = circle.getRadius() * circle.getRadius();

        Vector2f pos = new Vector2f(circle.getCenterX(), circle.getCenterY());

        for (int i = 0; i < 4; i++)
        {
            float dis = lines[i].distanceSquared(pos);
            if (dis < r2)
            {
                return true;
            }
        }

        return false;
    }

    /**
     * Check if circle touches a line.
     * @param other The line to check against
     * @return True if they touch
     */
    private boolean intersects(Line other)
    {
        // put it nicely into vectors
        Vector2f lineSegmentStart = new Vector2f(other.getX1(), other.getY1());
        Vector2f lineSegmentEnd = new Vector2f(other.getX2(), other.getY2());
        Vector2f circleCenter = new Vector2f(getCenterX(), getCenterY());

        // calculate point on line closest to the circle center and then
        // compare radius to distance to the point for intersection result
        Vector2f closest;
        //Vector2f segv = lineSegmentEnd.copy().sub(lineSegmentStart);
        //Vector2f ptv = circleCenter.copy().sub(lineSegmentStart);
        Vector2f segv = Vector2f.sub(lineSegmentEnd, lineSegmentStart, null);
        Vector2f ptv = Vector2f.sub(circleCenter, lineSegmentStart, null);
        float segvLength = segv.length();
        float projvl = Vector2f.dot(ptv, segv) / segvLength;
        if (projvl < 0)
        {
            closest = lineSegmentStart;
        }
        else if (projvl > segvLength)
        {
            closest = lineSegmentEnd;
        }
        else
        {
            //Vector2f projv = segv.copy().scale(projvl / segvLength);
            //closest = lineSegmentStart.copy().add(projv);
            float scale = projvl / segvLength;
            Vector2f projv = new Vector2f(segv.x * scale, segv.y * scale);
            closest = Vector2f.add(lineSegmentStart, projv, null);
        }
        boolean intersects = Vector2f.sub(circleCenter, closest, null).lengthSquared() <= getRadius() * getRadius();

        return intersects;
    }
}
