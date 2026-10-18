// CMSC 330 Advanced Programming Languages
// Project 1 - Parser and Display Program
// Jacob Supplee
// September 2026
//Description: Defines solid polygons

import java.awt.*;

class SolidPolygon extends Polygon_ {
    //Constructor that calls super constructor
    public SolidPolygon(Color color, int vertexCount) {
        super(color, vertexCount);
    }
    //Draws solid polygon
    @Override
    public void drawPolygon(Graphics graphics, Polygon polygon) {
        graphics.fillPolygon(polygon);
    }
}