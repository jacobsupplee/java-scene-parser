// Java Scene Parser
// Jacob Supplee
// October 2026

import java.awt.*;

// Polygon class that defaults to fill mode for existing scene files.
class SolidPolygon extends Polygon_ {
    public SolidPolygon(Color color, int vertexCount) {
        super(color, vertexCount, true);
    }

    public SolidPolygon(Color color, int vertexCount, boolean filled) {
        super(color, vertexCount, filled);
    }
}
