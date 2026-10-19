// Java Scene Parser
// Jacob Supplee
// October 2026
// Academic project for UMGC CMSC 330; based on course-provided skeleton.

import java.awt.*;

// Class that defines a hollow right triangle

class RightTriangle extends HollowPolygon {

    // Constructor that initializes the vertices of the right triangle

    public RightTriangle(Color color, Point upperLeft, int height, int width) {
        super(color, 3);
        int[] x_points = {upperLeft.x, upperLeft.x, upperLeft.x + width};
        int[] y_points = {upperLeft.y, upperLeft.y + height, upperLeft.y + height};
        createPolygon(x_points, y_points);
    }
}
