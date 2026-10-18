// CMSC 330 Advanced Programming Languages
// Project 1 - Parser and Display Program
// Jacob Supplee
// September 2026

//Description: Defines regular polygon

import java.awt.*;

class RegularPolygon extends SolidPolygon {
    //Constructor that initializes vertices of a regular polygon
    public RegularPolygon(Color color, int sides, Point center, int radius) {
        //Creates a solid polygon with number of sides
        super(color, sides);
        //Creates arrays for vertex coordinates
        int[] x_points = new int[sides];
        int[] y_points = new int[sides];
        //Calculates coordinates of each vertex
        for (int i = 0; i < sides; i++) {
            double angle = 2 * Math.PI * i / sides;
            x_points[i] = (int)(center.x + radius * Math.cos(angle));
            y_points[i] = (int)(center.y + radius * Math.sin(angle));
        }
        createPolygon(x_points, y_points);
    }
}