// CMSC 330 Advanced Programming Languages
// Project 1 - Parser and Display Program
// Jacob Supplee
// September 2026

//Description: Defines solid parallelogram

import java.awt.*;

class Parallelogram extends SolidPolygon {
    //Constructor that initializes vertices of parallelogram
    public Parallelogram(Color color, Point upperLeft, Point lowerRight, int offset) {
        //Creates a solid polygon w/ four vertices
        super(color, 4);
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