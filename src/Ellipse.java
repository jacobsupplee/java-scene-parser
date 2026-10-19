// Java Scene Parser
// Jacob Supplee
// October 2026

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

// Draws an oval using its upper-left corner, width, height, and fill style.
class Ellipse extends Image {
    private final int x;
    private final int y;
    private final int width;
    private final int height;

    public Ellipse(Color color, Point upperLeft, int width, int height, boolean filled) {
        super(color, filled);
        this.x = upperLeft.x;
        this.y = upperLeft.y;
        this.width = width;
        this.height = height;
    }

    @Override
    public void draw(Graphics graphics) {
        colorDrawing(graphics);
        if (isFilled()) {
            graphics.fillOval(x, y, width, height);
        } else {
            graphics.drawOval(x, y, width, height);
        }
    }
}
