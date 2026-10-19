// Java Scene Parser
// Jacob Supplee
// October 2026

//Description: Defines regular polygon

import java.awt.*;

class RegularPolygon extends SolidPolygon {
    //Constructor that initializes vertices of a regular polygon
    public RegularPolygon(Color color, int sides, Point center, int radius) {
        this(color, sides, center, radius, true);
    }

    public RegularPolygon(Color color, int sides, Point center, int radius, boolean filled) {
        //Creates a solid polygon with number of sides
        super(color, sides, filled);
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