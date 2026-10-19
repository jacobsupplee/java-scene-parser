// Java Scene Parser
// Jacob Supplee
// October 2026
// Academic project for UMGC CMSC 330; based on course-provided skeleton.

import java.awt.*;
import java.util.*;
import javax.swing.*;

// Class that defines the panel for drawing the images.
class DrawingPanel extends JPanel {

    private ArrayList<Image> images = new ArrayList<>();

    // Keeps the drawing canvas plain white, regardless of the launcher's theme.
    public DrawingPanel() {
        setBackground(Color.WHITE);
    }

    // Adds a graphic object to the drawing panel.
    public void addImage(Image image) {
        images.add(image);
    }

    // Draws all the images on the drawing panel.
    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        for (Image image : images) {
            image.draw(graphics);
        }
    }
}
