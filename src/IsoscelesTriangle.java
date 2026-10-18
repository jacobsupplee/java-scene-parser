// CMSC 330 Advanced Programming Languages
// Project 1 - Parser and Display Program
// Jacob Supplee
// September 2026
//Description: Defines an isosceles triangle

import java.awt.*;

class IsoscelesTriangle extends SolidPolygon {
    //Constructor that initializes vertices of the isosceles triangle
    public IsoscelesTriangle(Color color, Point topVertex, int height, int width) {

        //Creates a solid polygon w/ three vertices
        super(color, 3);

        //Calculates x coordinates of the three vertices
        int[] x_points = {
            topVertex.x,
            topVertex.x - width / 2,
            topVertex.x + width / 2
        };
        //Calculates y coordinates of the three vertices
        int[] y_points = {
            topVertex.y,
            topVertex.y + height,
            topVertex.y + height
        };
        createPolygon(x_points, y_points);
    }
}