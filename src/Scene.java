// Java Scene Parser
// Jacob Supplee
// October 2026
// Academic project for UMGC CMSC 330; based on course-provided skeleton.

import java.awt.GraphicsEnvironment;
import javax.swing.*;

// Class that defines a scene
class Scene {
    private JFrame window;
    private DrawingPanel drawing;
    // Constructor that initializes scene and creates window when graphics are available
    public Scene(String name, int height, int width) {
        drawing = new DrawingPanel();
        //Allows parser tests to run w/o opening a window
        if (!GraphicsEnvironment.isHeadless()) {
            window = new JFrame(name);
            window.setSize(width, height);
            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            window.add(drawing);
        }
    }
    // Adds a graphic object to scene's drawing panel
    public void addImage(Image image) {
        drawing.addImage(image);
    }
    // Displays scene when graphics are available
    public void draw() {
        if (window != null) {
            window.setVisible(true);
            drawing.repaint();
        }
    }
}