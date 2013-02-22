package org.lazywizard.lazylib;

import java.awt.geom.Line2D;
import org.lwjgl.util.vector.Vector2f;

public class Line extends Line2D.Float
{
    public Line(Vector2f start, Vector2f end)
    {
        super(start.x, start.y, end.x, end.y);
    }
}
