// Java Scene Parser
// Jacob Supplee
// October 2026
// Academic project for UMGC CMSC 330; based on course-provided skeleton.

import java.awt.Color;
import java.awt.Font;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

// Opens the scene builder.
class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            applyTheme();
            new SceneBuilder().setVisible(true);
        });
    }

    // Sets the look and feel before Swing creates the window and its controls.
    private static void applyTheme() {
        UIManager.put("Panel.background", new Color(31, 41, 55));
        UIManager.put("Label.foreground", new Color(229, 231, 235));
        UIManager.put("TextField.background", new Color(17, 24, 39));
        UIManager.put("TextField.foreground", new Color(243, 244, 246));
        UIManager.put("TextField.caretForeground", new Color(243, 244, 246));
        UIManager.put("TextField.selectionBackground", new Color(91, 70, 150));
        UIManager.put("ComboBox.background", new Color(55, 65, 81));
        UIManager.put("ComboBox.foreground", new Color(243, 244, 246));
        UIManager.put("List.background", new Color(17, 24, 39));
        UIManager.put("List.foreground", new Color(229, 231, 235));
        UIManager.put("Button.background", new Color(55, 65, 81));
        UIManager.put("Button.foreground", new Color(243, 244, 246));
        UIManager.put("OptionPane.background", new Color(31, 41, 55));
        UIManager.put("OptionPane.messageForeground", new Color(229, 231, 235));
        UIManager.put("TitledBorder.titleColor", new Color(196, 181, 253));
        Font font = new Font("Segoe UI", Font.PLAIN, 13);
        UIManager.put("Label.font", font);
        UIManager.put("Button.font", font);
        UIManager.put("TextField.font", font);
        UIManager.put("ComboBox.font", font);
    }
}
