// Java Scene Parser
// Jacob Supplee
// October 2026
// Academic project for UMGC CMSC 330; based on course-provided skeleton.

import java.awt.*;

// Class that defines a hollow rectangle object

class Rectangle extends HollowPolygon {

    // Constructor that initializes the vertices of the rectangle

    public Rectangle(Color color, Point upperLeft, int height, int width) {
        this(color, upperLeft, height, width, false);
    }

    public Rectangle(Color color, Point upperLeft, int height, int width, boolean filled) {
        super(color, 4, filled);
        int[] x_points = {upperLeft.x, upperLeft.x + width, upperLeft.x + width, upperLeft.x};
        int[] y_points = {upperLeft.y, upperLeft.y, upperLeft.y + height, upperLeft.y + height};
        createPolygon(x_points, y_points);
    }
}
