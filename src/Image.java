// Java Scene Parser
// Jacob Supplee
// October 2026
// Academic project for UMGC CMSC 330; based on course-provided skeleton.

import java.awt.*;

// Shared color and fill style for drawable scene objects.
abstract class Image {
    private Color color;
    private boolean filled;

    public Image(Color color) {
        this(color, true);
    }

    public Image(Color color, boolean filled) {
        this.color = color;
        this.filled = filled;
    }

    public void colorDrawing(Graphics graphics) {
        graphics.setColor(color);
    }

    public boolean isFilled() {
        return filled;
    }

    public void setFilled(boolean filled) {
        this.filled = filled;
    }

    abstract void draw(Graphics graphics);
}
