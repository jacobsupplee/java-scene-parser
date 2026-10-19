// Java Scene Parser
// Jacob Supplee
// October 2026

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

// Draws a line between two points.
class Line extends Image {
    private final Point start;
    private final Point end;

    public Line(Color color, Point start, Point end) {
        super(color, false);
        this.start = start;
        this.end = end;
    }

    @Override
    public void draw(Graphics graphics) {
        colorDrawing(graphics);
        graphics.drawLine(start.x, start.y, end.x, end.y);
    }
}
