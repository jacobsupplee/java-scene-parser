// Java Scene Parser
// Jacob Supplee
// October 2026

import java.awt.*;

class Parallelogram extends SolidPolygon {
    //Constructor that initializes vertices of parallelogram
    public Parallelogram(Color color, Point upperLeft, Point lowerRight, int offset) {
        this(color, upperLeft, lowerRight, offset, true);
    }

    public Parallelogram(Color color, Point upperLeft, Point lowerRight, int offset, boolean filled) {
        //Creates a solid polygon w/ four vertices
        super(color, 4, filled);
        //Calculates x coordinates of the four vertices
        int[] x_points = {
            upperLeft.x + offset,
            lowerRight.x,
            lowerRight.x - offset,
            upperLeft.x
        };
        //Calculates y coordinates of the four vertices
        int[] y_points = {
            upperLeft.y,
            upperLeft.y,
            lowerRight.y,
            lowerRight.y
        };
        createPolygon(x_points, y_points);
    }
}