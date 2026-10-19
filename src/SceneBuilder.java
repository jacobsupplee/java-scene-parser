// Java Scene Parser
// Jacob Supplee
// October 2026

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.DefaultListModel;

// Interactive interface for building scene files and passing them through the existing parser.
class SceneBuilder extends JFrame {
    private final JTextField sceneName = new JTextField("MyScene", 12);
    private final JTextField canvasWidth = new JTextField("500", 5);
    private final JTextField canvasHeight = new JTextField("500", 5);
    private final JComboBox<String> shapeChoice = new JComboBox<>(new String[] {
        "Rectangle", "RightTriangle", "Isosceles", "Parallelogram", "RegularPolygon", "Text"
    });

    private final JTextField x = new JTextField("50", 5);
    private final JTextField y = new JTextField("50", 5);
    private final JTextField height = new JTextField("100", 5);
    private final JTextField width = new JTextField("100", 5);
    private final JTextField x2 = new JTextField("150", 5);
    private final JTextField y2 = new JTextField("150", 5);
    private final JTextField offset = new JTextField("25", 5);
    private final JTextField sides = new JTextField("6", 5);
    private final JTextField radius = new JTextField("60", 5);
    private final JTextField textValue = new JTextField("Hello World", 12);

    private Color selectedColor = new Color(40, 130, 220);
    private final JButton colorButton = new JButton("Choose color");
    private final DefaultListModel<String> objectListModel = new DefaultListModel<>();
    private final JList<String> objectList = new JList<>(objectListModel);
    private final ArrayList<String> sceneObjects = new ArrayList<>();

    public SceneBuilder() {
        super("Java Scene Parser - Scene Builder");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(12, 12));

        JPanel sceneSettings = new JPanel(new FlowLayout(FlowLayout.LEFT));
        sceneSettings.setBorder(BorderFactory.createTitledBorder("Scene settings"));
        sceneSettings.add(new JLabel("Scene name"));
        sceneSettings.add(sceneName);
        sceneSettings.add(new JLabel("Canvas width"));
        sceneSettings.add(canvasWidth);
        sceneSettings.add(new JLabel("Canvas height"));
        sceneSettings.add(canvasHeight);
        add(sceneSettings, BorderLayout.NORTH);

        JPanel fields = new JPanel(new GridLayout(0, 4, 8, 6));
        fields.setBorder(BorderFactory.createTitledBorder("Shape properties"));
        fields.add(new JLabel("Shape"));
        fields.add(shapeChoice);
        fields.add(new JLabel("Color"));
        colorButton.setBackground(selectedColor);
        colorButton.setForeground(Color.WHITE);
        colorButton.addActionListener(event -> chooseColor());
        fields.add(colorButton);
        addField(fields, "X", x);
        addField(fields, "Y", y);
        addField(fields, "Height", height);
        addField(fields, "Width", width);
        addField(fields, "Second point X", x2);
        addField(fields, "Second point Y", y2);
        addField(fields, "Offset", offset);
        addField(fields, "Polygon sides", sides);
        addField(fields, "Polygon radius", radius);
        addField(fields, "Text content", textValue);

        JButton addShape = new JButton("Add shape");
        addShape.addActionListener(event -> addShape());
        fields.add(addShape);
        JButton removeShape = new JButton("Remove selected");
        removeShape.addActionListener(event -> removeSelectedShape());
        fields.add(removeShape);
        add(fields, BorderLayout.CENTER);

        JPanel objectPanel = new JPanel(new BorderLayout(6, 6));
        objectPanel.setBorder(BorderFactory.createTitledBorder("Shapes in this scene"));
        objectList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        objectPanel.add(new JScrollPane(objectList), BorderLayout.CENTER);
        JButton drawScene = new JButton("Draw scene");
        drawScene.addActionListener(event -> drawScene());
        objectPanel.add(drawScene, BorderLayout.SOUTH);
        objectPanel.setPreferredSize(new java.awt.Dimension(260, 260));
        add(objectPanel, BorderLayout.EAST);

        JLabel help = new JLabel("Add one or more shapes, then draw the scene. The existing lexer and parser validate the generated scene.");
        help.setBorder(BorderFactory.createEmptyBorder(0, 10, 8, 10));
        add(help, BorderLayout.SOUTH);

        shapeChoice.addActionListener(event -> updateFieldAvailability());
        updateFieldAvailability();
        setSize(1000, 520);
        setLocationRelativeTo(null);
    }

    // Adds a labeled field to the property grid.
    private void addField(JPanel panel, String label, JTextField field) {
        panel.add(new JLabel(label));
        panel.add(field);
    }

    // Opens Swing's built-in color picker.
    private void chooseColor() {
        Color chosen = JColorChooser.showDialog(this, "Choose shape color", selectedColor);
        if (chosen != null) {
            selectedColor = chosen;
            colorButton.setBackground(chosen);
            colorButton.setForeground(chosen.getRed() + chosen.getGreen() + chosen.getBlue() < 380
                    ? Color.WHITE : Color.BLACK);
        }
    }

    // Enables only the fields used by the currently selected shape.
    private void updateFieldAvailability() {
        String shape = (String) shapeChoice.getSelectedItem();
        boolean dimensions = "Rectangle".equals(shape) || "RightTriangle".equals(shape)
                || "Isosceles".equals(shape);
        height.setEnabled(dimensions);
        width.setEnabled(dimensions);
        x2.setEnabled("Parallelogram".equals(shape));
        y2.setEnabled("Parallelogram".equals(shape));
        offset.setEnabled("Parallelogram".equals(shape));
        sides.setEnabled("RegularPolygon".equals(shape));
        radius.setEnabled("RegularPolygon".equals(shape));
        textValue.setEnabled("Text".equals(shape));
    }

    // Validates the selected shape's numeric fields and stores its scene-language definition.
    private void addShape() {
        try {
            String shape = (String) shapeChoice.getSelectedItem();
            int px = readInteger(x, "X");
            int py = readInteger(y, "Y");
            String color = "Color (" + selectedColor.getRed() + ", " + selectedColor.getGreen()
                    + ", " + selectedColor.getBlue() + ")";

            String definition;
            String summary;
            if ("Rectangle".equals(shape) || "RightTriangle".equals(shape) || "Isosceles".equals(shape)) {
                int h = positiveInteger(height, "Height");
                int w = positiveInteger(width, "Width");
                definition = shape + " " + color + " at (" + px + ", " + py + ") Height " + h + " Width " + w + ";";
                summary = shape + " at (" + px + ", " + py + ") - " + w + " x " + h;
            } else if ("Parallelogram".equals(shape)) {
                int px2 = readInteger(x2, "Second point X");
                int py2 = readInteger(y2, "Second point Y");
                int shift = readInteger(offset, "Offset");
                definition = shape + " " + color + " at (" + px + ", " + py + ") (" + px2 + ", " + py2
                        + ") Offset " + shift + ";";
                summary = shape + " from (" + px + ", " + py + ") to (" + px2 + ", " + py2 + ")";
            } else if ("RegularPolygon".equals(shape)) {
                int sideCount = positiveInteger(sides, "Polygon sides");
                if (sideCount < 3) {
                    throw new IllegalArgumentException("A regular polygon must have at least 3 sides.");
                }
                int polygonRadius = positiveInteger(radius, "Polygon radius");
                definition = shape + " " + color + " at (" + px + ", " + py + ") Sides " + sideCount
                        + " Radius " + polygonRadius + ";";
                summary = shape + " at (" + px + ", " + py + ") - " + sideCount + " sides";
            } else {
                String safeText = textValue.getText().replace("\\", "\\\\").replace("\"", "\\\"");
                definition = shape + " " + color + " at (" + px + ", " + py + ") \"" + safeText + "\";";
                summary = "Text at (" + px + ", " + py + "): " + textValue.getText();
            }

            sceneObjects.add(definition);
            objectListModel.addElement(summary);
        } catch (NumberFormatException error) {
            showError("Enter whole numbers in the numeric fields.");
        } catch (IllegalArgumentException error) {
            showError(error.getMessage());
        }
    }

    private int readInteger(JTextField field, String label) {
        try {
            return Integer.parseInt(field.getText().trim());
        } catch (NumberFormatException error) {
            throw new IllegalArgumentException(label + " must be a whole number.");
        }
    }

    private int positiveInteger(JTextField field, String label) {
        int value = readInteger(field, label);
        if (value <= 0) {
            throw new IllegalArgumentException(label + " must be greater than zero.");
        }
        return value;
    }

    // Removes the selected item from both the visible list and the generated scene.
    private void removeSelectedShape() {
        int selected = objectList.getSelectedIndex();
        if (selected >= 0) {
            objectListModel.remove(selected);
            sceneObjects.remove(selected);
        }
    }

    // Writes the scene definition to a temporary file and reuses the existing parser to draw it.
    private void drawScene() {
        File temporaryScene = null;
        try {
            String name = sceneName.getText().trim();
            if (name.isEmpty() || !name.matches("[A-Za-z][A-Za-z0-9_]*")) {
                showError("Scene name must start with a letter and contain only letters, numbers, or underscores.");
                return;
            }
            int w = positiveInteger(canvasWidth, "Canvas width");
            int h = positiveInteger(canvasHeight, "Canvas height");
            if (sceneObjects.isEmpty()) {
                showError("Add at least one shape before drawing the scene.");
                return;
            }

            StringBuilder source = new StringBuilder();
            source.append("Scene ").append(name).append(" (").append(w).append(", ").append(h).append(")\n");
            for (String object : sceneObjects) {
                source.append("    ").append(object).append("\n");
            }
            source.append("End.\n");

            temporaryScene = File.createTempFile("java-scene-builder-", ".txt");
            Files.write(temporaryScene.toPath(), source.toString().getBytes(StandardCharsets.UTF_8));
            // The parser remains responsible for validating and constructing the drawing.
            try (Parser parser = new Parser(temporaryScene)) {
                Scene scene = parser.parseScene();
                scene.draw();
            }
        } catch (SyntaxError | LexicalError error) {
            showError(error.getMessage());
        } catch (IOException error) {
            showError("Could not create or read the scene file: " + error.getMessage());
        } catch (IllegalArgumentException error) {
            showError(error.getMessage());
        } finally {
            if (temporaryScene != null) {
                try {
                    Files.deleteIfExists(temporaryScene.toPath());
                } catch (IOException error) {
                    // The temporary file is safe to leave if cleanup fails.
                }
            }
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Scene Builder", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SceneBuilder().setVisible(true));
    }
}
