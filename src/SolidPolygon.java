// Java Scene Parser
// Jacob Supplee
// October 2026

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