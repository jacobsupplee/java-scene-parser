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
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JTabbedPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.DefaultListModel;

// Interactive interface for building scene files and passing them through the existing parser.
class SceneBuilder extends JFrame {
    private final JTextField sceneName = new JTextField("MyScene", 12);
    private final JTextField canvasWidth = new JTextField("500", 5);
    private final JTextField canvasHeight = new JTextField("500", 5);
    private final JComboBox<String> shapeChoice = new JComboBox<>(new String[] {
        "Rectangle", "RightTriangle", "Isosceles", "Parallelogram", "RegularPolygon", "Circle", "Ellipse", "Line", "RoundedRectangle", "Text"
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
    private final JTextField cornerRadius = new JTextField("18", 5);
    private final JComboBox<String> fillStyle = new JComboBox<>(new String[] {"Filled", "Outline"});
    private final JTextField textValue = new JTextField("Hello World", 12);
    private final JPanel fields = new JPanel(new GridLayout(0, 4, 10, 10));

    private Color selectedColor = new Color(167, 139, 250);
    private final JButton colorButton = new JButton("Pick a color");
    private final DefaultListModel<String> objectListModel = new DefaultListModel<>();
    private final JList<String> objectList = new JList<>(objectListModel);
    private final JTextArea scenePreview = new JTextArea();
    private final ArrayList<String> sceneObjects = new ArrayList<>();

    public SceneBuilder() {
        super("Scene Studio");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(14, 14));
        getContentPane().setBackground(new Color(17, 24, 39));
        ((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(14, 14, 12, 14));

        JPanel sceneSettings = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 10));
        stylePanel(sceneSettings);
        sceneSettings.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(55, 65, 81)), BorderFactory.createEmptyBorder(8, 8, 8, 8)));
        sceneSettings.add(new JLabel("SCENE"));
        sceneSettings.add(sceneName);
        sceneSettings.add(new JLabel("WIDTH"));
        sceneSettings.add(canvasWidth);
        sceneSettings.add(new JLabel("HEIGHT"));
        sceneSettings.add(canvasHeight);
        add(sceneSettings, BorderLayout.NORTH);

        stylePanel(fields);
        fields.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(55, 65, 81)), BorderFactory.createEmptyBorder(14, 14, 14, 14)));
        colorButton.setBackground(selectedColor);
        colorButton.setOpaque(true);
        colorButton.setFocusPainted(false);
        colorButton.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        colorButton.setForeground(Color.WHITE);
        colorButton.addActionListener(event -> chooseColor());
        rebuildFields();
        add(fields, BorderLayout.CENTER);

        JPanel objectPanel = new JPanel(new BorderLayout(8, 8));
        stylePanel(objectPanel);
        objectPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(55, 65, 81)), BorderFactory.createEmptyBorder(12, 12, 12, 12)));
        objectList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        objectList.setBackground(new Color(17, 24, 39));
        objectList.setForeground(new Color(229, 231, 235));
        objectList.setSelectionBackground(new Color(91, 70, 150));
        objectList.setSelectionForeground(Color.WHITE);
        objectList.setFixedCellHeight(34);
        scenePreview.setEditable(false);
        scenePreview.setBackground(new Color(17, 24, 39));
        scenePreview.setForeground(new Color(229, 231, 235));
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Objects", new JScrollPane(objectList));
        tabs.addTab("Scene code", new JScrollPane(scenePreview));
        objectPanel.add(tabs, BorderLayout.CENTER);
        JPanel actions = new JPanel(new GridLayout(0, 1, 4, 4));
        JButton drawScene = new JButton("Render scene  →");
        drawScene.addActionListener(event -> drawScene());
        actions.add(drawScene);
        JButton openScene = new JButton("Load a .txt scene");
        openScene.addActionListener(event -> openExistingScene());
        actions.add(openScene);
        JButton saveScene = new JButton("Save scene as...");
        saveScene.addActionListener(event -> saveScene());
        actions.add(saveScene);
        objectPanel.add(actions, BorderLayout.SOUTH);
        objectPanel.setPreferredSize(new java.awt.Dimension(260, 260));
        add(objectPanel, BorderLayout.EAST);

        JLabel help = new JLabel("Make something. Pick a shape, tweak the details, then render it.");
        help.setForeground(new Color(156, 163, 175));
        help.setBorder(BorderFactory.createEmptyBorder(0, 10, 8, 10));
        add(help, BorderLayout.SOUTH);

        shapeChoice.addActionListener(event -> rebuildFields());
        sceneName.addActionListener(event -> refreshSceneCode());
        canvasWidth.addActionListener(event -> refreshSceneCode());
        canvasHeight.addActionListener(event -> refreshSceneCode());
        refreshSceneCode();
        setSize(1080, 650);
        setMinimumSize(new java.awt.Dimension(900, 580));
        setLocationRelativeTo(null);
    }

    private void stylePanel(JPanel panel) {
        panel.setBackground(new Color(31, 41, 55));
    }

    // Adds text fields and selectors to the property grid.
    private void addField(JPanel panel, String label, java.awt.Component field) {
        panel.add(new JLabel(label));
        panel.add(field);
    }

    // Opens Swing's built-in color picker.
    private void chooseColor() {
        Color chosen = JColorChooser.showDialog(this, "Pick a color", selectedColor);
        if (chosen != null) {
            selectedColor = chosen;
            colorButton.setBackground(chosen);
            colorButton.setForeground(chosen.getRed() + chosen.getGreen() + chosen.getBlue() < 380
                    ? Color.WHITE : Color.BLACK);
        }
    }

    // Rebuilds the property grid so only relevant options are visible.
    private void rebuildFields() {
        String shape = (String) shapeChoice.getSelectedItem();
        fields.removeAll();
        fields.add(new JLabel("SHAPE"));
        fields.add(shapeChoice);
        fields.add(new JLabel("COLOR"));
        fields.add(colorButton);

        addField(fields, "X", x);
        addField(fields, "Y", y);

        if ("Rectangle".equals(shape) || "RightTriangle".equals(shape) || "Isosceles".equals(shape)
                || "RoundedRectangle".equals(shape)) {
            addField(fields, "Height", height);
            addField(fields, "Width", width);
        }

        if ("Ellipse".equals(shape)) {
            addField(fields, "Width", width);
            addField(fields, "Height", height);
        }

        if ("Line".equals(shape) || "Parallelogram".equals(shape)) {
            addField(fields, "Second point X", x2);
            addField(fields, "Second point Y", y2);
        }

        if ("RoundedRectangle".equals(shape)) {
            addField(fields, "Corner radius", cornerRadius);
        }

        if ("Parallelogram".equals(shape)) {
            addField(fields, "Offset", offset);
        }

        if ("RegularPolygon".equals(shape)) {
            addField(fields, "Polygon sides", sides);
            addField(fields, "Polygon radius", radius);
        } else if ("Circle".equals(shape)) {
            addField(fields, "Circle radius", radius);
        }

        if ("Text".equals(shape)) {
            addField(fields, "Text content", textValue);
        } else {
            addField(fields, "Style", fillStyle);
        }

        JButton addShape = new JButton("+ Add shape");
        addShape.addActionListener(event -> addShape());
        fields.add(addShape);

        JButton removeShape = new JButton("Remove selected");
        removeShape.addActionListener(event -> removeSelectedShape());
        fields.add(removeShape);

        fields.revalidate();
        fields.repaint();
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
            if ("Rectangle".equals(shape) || "RightTriangle".equals(shape) || "Isosceles".equals(shape)
                    || "RoundedRectangle".equals(shape)) {
                int h = positiveInteger(height, "Height");
                int w = positiveInteger(width, "Width");
                if ("RoundedRectangle".equals(shape)) {
                    int corner = positiveInteger(cornerRadius, "Corner radius");
                    definition = shape + " " + color + " at (" + px + ", " + py + ") Height " + h
                            + " Width " + w + " Radius " + corner + ";";
                } else {
                    definition = shape + " " + color + " at (" + px + ", " + py + ") Height " + h + " Width " + w + ";";
                }
                summary = shape + " at (" + px + ", " + py + ") - " + w + " x " + h;
            } else if ("Ellipse".equals(shape)) {
                int w = positiveInteger(width, "Width");
                int h = positiveInteger(height, "Height");
                definition = shape + " " + color + " at (" + px + ", " + py + ") Width " + w + " Height " + h + ";";
                summary = "Ellipse at (" + px + ", " + py + ") - " + w + " x " + h;
            } else if ("Line".equals(shape)) {
                int px2 = readInteger(x2, "Second point X");
                int py2 = readInteger(y2, "Second point Y");
                definition = shape + " " + color + " at (" + px + ", " + py + ") to (" + px2 + ", " + py2 + ");";
                summary = "Line from (" + px + ", " + py + ") to (" + px2 + ", " + py2 + ")";
            } else if ("Parallelogram".equals(shape)) {
                int px2 = readInteger(x2, "Second point X");
                int py2 = readInteger(y2, "Second point Y");
                int shift = readInteger(offset, "Offset");
                definition = shape + " " + color + " at (" + px + ", " + py + ") (" + px2 + ", " + py2
                        + ") Offset " + shift + ";";
                summary = shape + " from (" + px + ", " + py + ") to (" + px2 + ", " + py2 + ")";
            } else if ("Circle".equals(shape)) {
                int circleRadius = positiveInteger(radius, "Circle radius");
                definition = shape + " " + color + " at (" + px + ", " + py + ") Radius " + circleRadius + ";";
                summary = "Circle at (" + px + ", " + py + ") - radius " + circleRadius;
            } else if ("RegularPolygon".equals(shape)) {
                int sideCount = positiveInteger(sides, "Polygon sides");
                if (sideCount < 3) {
                    throw new IllegalArgumentException("A polygon needs at least 3 sides.");
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

            if (!"Text".equals(shape) && !"Line".equals(shape)) {
                String style = (String) fillStyle.getSelectedItem();
                definition = definition.substring(0, definition.length() - 1) + " " + style + ";";
                summary += " - " + style.toLowerCase();
            }
            sceneObjects.add(definition);
            objectListModel.addElement(summary);
            refreshSceneCode();
        } catch (NumberFormatException error) {
            showError("Use whole numbers for the shape details.");
        } catch (IllegalArgumentException error) {
            showError(error.getMessage());
        }
    }

    private int readInteger(JTextField field, String label) {
        try {
            return Integer.parseInt(field.getText().trim());
        } catch (NumberFormatException error) {
            throw new IllegalArgumentException(label + " needs to be a whole number.");
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
            refreshSceneCode();
        }
    }

    // Keeps the code tab synchronized with the current scene settings and shapes.
    private void refreshSceneCode() {
        StringBuilder source = new StringBuilder();
        source.append("Scene ").append(sceneName.getText().trim()).append(" (")
                .append(canvasWidth.getText().trim()).append(", ")
                .append(canvasHeight.getText().trim()).append(")\n");
        for (String object : sceneObjects) {
            source.append("    ").append(object).append("\n");
        }
        source.append("End.\n");
        scenePreview.setText(source.toString());
        scenePreview.setCaretPosition(0);
    }

    // Writes the scene definition to a temporary file and reuses the existing parser to draw it.
    private void drawScene() {
        File temporaryScene = null;
        try {
            String name = sceneName.getText().trim();
            if (name.isEmpty() || !name.matches("[A-Za-z][A-Za-z0-9_]*")) {
                showError("Give the scene a name that starts with a letter. Letters, numbers, and underscores work after that.");
                return;
            }
            int w = positiveInteger(canvasWidth, "Canvas width");
            int h = positiveInteger(canvasHeight, "Canvas height");
            if (sceneObjects.isEmpty()) {
                showError("Add a shape first, then render your scene.");
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
            showError("Couldn’t read the scene file: " + error.getMessage());
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

    // Keeps the original file-based workflow available from the new launcher.
    private void openExistingScene() {
        JFileChooser chooser = new JFileChooser(new File("."));
        int choice = chooser.showOpenDialog(this);
        if (choice != JFileChooser.APPROVE_OPTION) {
            return;
        }

        try (Parser parser = new Parser(chooser.getSelectedFile())) {
            Scene scene = parser.parseScene();
            scene.draw();
        } catch (SyntaxError | LexicalError error) {
            showError(error.getMessage());
        } catch (IOException error) {
            showError("Couldn’t open that scene: " + error.getMessage());
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Quick fix", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        Main.main(args);
    }
}
