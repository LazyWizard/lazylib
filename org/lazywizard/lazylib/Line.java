/*
 * Copyright (c) 2007, Slick 2D

 All rights reserved.

 Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name of the Slick 2D nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.

 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.lazywizard.lazylib;

import org.lwjgl.util.vector.Vector2f;

// A gutted and modified version of the Slick library's class of the same name
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

    public Vector2f intersect(Line other)
    {
        float dx1 = end.getX() - start.getX();
        float dx2 = other.end.getX() - other.start.getX();
        float dy1 = end.getY() - start.getY();
        float dy2 = other.end.getY() - other.start.getY();
        float denom = (dy2 * dx1) - (dx2 * dy1);

        if (denom == 0)
        {
            return null;
        }

        float ua = (dx2 * (start.getY() - other.start.getY()))
                - (dy2 * (start.getX() - other.start.getX()));
        ua /= denom;
        float ub = (dx1 * (start.getY() - other.start.getY()))
                - (dy1 * (start.getX() - other.start.getX()));
        ub /= denom;

        if ((ua < 0) || (ua > 1) || (ub < 0) || (ub > 1))
        {
            return null;
        }

        float u = ua;

        float ix = start.getX() + (u * (end.getX() - start.getX()));
        float iy = start.getY() + (u * (end.getY() - start.getY()));

        return new Vector2f(ix, iy);
    }
}
