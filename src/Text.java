// CMSC 330 Advanced Programming Languages
// Project 1 - Parser and Display Program
// Jacob Supplee
// September 2026

//Description: Defines text image

import java.awt.*;

class Text extends Image {
    private Point point;
    private String text;
    //Constructor: initializes color, location, and text
    public Text(Color color, Point point, String text) {
        super(color);
        this.point = point;
        this.text = text;
    }
    //Draws text at specified location
    @Override
    public void draw(Graphics graphics) {
        colorDrawing(graphics);
        graphics.drawString(text, point.x, point.y);
    }
}