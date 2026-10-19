// Java Scene Parser
// Jacob Supplee
// October 2026

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

// Draws a rectangle with rounded corners and a selectable fill style.
class RoundedRectangle extends Image {
    private final int x;
    private final int y;
    private final int height;
    private final int width;
    private final int cornerRadius;

    public RoundedRectangle(Color color, Point upperLeft, int height, int width, int cornerRadius, boolean filled) {
        super(color, filled);
        this.x = upperLeft.x;
        this.y = upperLeft.y;
        this.height = height;
        this.width = width;
        this.cornerRadius = cornerRadius * 2;
    }

    @Override
    public void draw(Graphics graphics) {
        colorDrawing(graphics);
        if (isFilled()) {
            graphics.fillRoundRect(x, y, width, height, cornerRadius, cornerRadius);
        } else {
            graphics.drawRoundRect(x, y, width, height, cornerRadius, cornerRadius);
        }
    }
}
