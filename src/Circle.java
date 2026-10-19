// Java Scene Parser
// Jacob Supplee
// October 2026

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

// Draws a circle using the selected color and fill style.
class Circle extends Image {
    private final int x;
    private final int y;
    private final int radius;

    public Circle(Color color, Point center, int radius, boolean filled) {
        super(color, filled);
        this.x = center.x - radius;
        this.y = center.y - radius;
        this.radius = radius;
    }

    @Override
    public void draw(Graphics graphics) {
        colorDrawing(graphics);
        int diameter = radius * 2;
        if (isFilled()) {
            graphics.fillOval(x, y, diameter, diameter);
        } else {
            graphics.drawOval(x, y, diameter, diameter);
        }
    }
}
