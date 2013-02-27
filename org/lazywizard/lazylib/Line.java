package org.lazywizard.lazylib;

import java.awt.geom.Line2D;
import org.lwjgl.util.vector.Vector2f;

/**
 * A simple extension of {@link java.awt.geom.Line2D.Float} that supports {@link Vector2f}s.
 *
 * @author LazyWizard
 */
public class Line extends Line2D.Float
{
    /**
     * A constructor that supports {@link Vector2f}s.
     *
     * @param start The start point of the line.
     * @param end The end point of the line.
     */
    public Line(Vector2f start, Vector2f end)
    {
        super(start.x, start.y, end.x, end.y);
    }
}
