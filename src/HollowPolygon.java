// Java Scene Parser
// Jacob Supplee
// October 2026
// Academic project for UMGC CMSC 330; based on course-provided skeleton.

import java.awt.*;

// Polygon class that defaults to outline mode for existing scene files.
class HollowPolygon extends Polygon_ {
    public HollowPolygon(Color color, int vertexCount) {
        super(color, vertexCount, false);
    }

    public HollowPolygon(Color color, int vertexCount, boolean filled) {
        super(color, vertexCount, filled);
    }
}
