// Java Scene Parser
// Jacob Supplee
// October 2026
// Academic project for UMGC CMSC 330; based on course-provided skeleton.

import javax.swing.SwingUtilities;

// Launches the interactive scene builder.
class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SceneBuilder().setVisible(true));
    }
}
