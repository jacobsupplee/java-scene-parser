// Java Scene Parser
// Jacob Supplee
// October 2026
// Academic project for UMGC CMSC 330; based on course-provided skeleton.

import java.awt.*;

// Abstract base class for polygon shapes.
abstract class Polygon_ extends Image {
    private int vertexCount;
    private Polygon polygon;

    public Polygon_(Color color, int vertexCount) {
        super(color);
        this.vertexCount = vertexCount;
    }

    public Polygon_(Color color, int vertexCount, boolean filled) {
        super(color, filled);
        this.vertexCount = vertexCount;
    }

    public void createPolygon(int[] xPoints, int[] yPoints) {
        polygon = new Polygon(xPoints, yPoints, vertexCount);
    }

    @Override
    public void draw(Graphics graphics) {
        colorDrawing(graphics);
        drawPolygon(graphics, polygon);
    }

    // Uses the selected style regardless of the polygon's shape class.
    public void drawPolygon(Graphics graphics, Polygon polygon) {
        if (isFilled()) {
            graphics.fillPolygon(polygon);
        } else {
            graphics.drawPolygon(polygon);
        }
    }
}
