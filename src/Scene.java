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

    // Creates a drawing panel and window using the requested canvas dimensions.
    public Scene(String name, int width, int height) {
        drawing = new DrawingPanel();
        // Allows parser tests to run without opening a window.
        if (!GraphicsEnvironment.isHeadless()) {
            window = new JFrame(name);
            window.setSize(width, height);
            window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            window.add(drawing);
        }
    }

    // Adds a graphic object to the scene's drawing panel.
    public void addImage(Image image) {
        drawing.addImage(image);
    }

    // Displays the scene when graphics are available.
    public void draw() {
        if (window != null) {
            window.setVisible(true);
            drawing.repaint();
        }
    }
}
