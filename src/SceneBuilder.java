// Java Scene Parser
// Jacob Supplee
// October 2026

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.GridBagConstraints;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
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
    private final JPanel fields = new JPanel(new java.awt.GridBagLayout());
    private int fieldRow = 0;

    private Color selectedColor = new Color(167, 139, 250);
    private final JButton colorButton = new JButton("Pick a color");
    private final DefaultListModel<String> objectListModel = new DefaultListModel<>();
    private final JList<String> objectList = new JList<>(objectListModel);
    private final JTextArea scenePreview = new JTextArea();
    private final ArrayList<String> sceneObjects = new ArrayList<>();
    private int editingIndex = -1;
    private JButton addShapeButton;
    private JButton undoButton;
    private JButton redoButton;
    private final Deque<SceneSnapshot> undoHistory = new ArrayDeque<>();
    private final Deque<SceneSnapshot> redoHistory = new ArrayDeque<>();

    // Stores the scene's object definitions and display labels at one point in time.
    private static class SceneSnapshot {
        private final ArrayList<String> definitions;
        private final ArrayList<String> labels;

        private SceneSnapshot(ArrayList<String> definitions, ArrayList<String> labels) {
            this.definitions = definitions;
            this.labels = labels;
        }
    }

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
        JPanel historyActions = new JPanel(new GridLayout(1, 2, 4, 4));
        stylePanel(historyActions);
        undoButton = new JButton("Undo");
        undoButton.addActionListener(event -> undo());
        redoButton = new JButton("Redo");
        redoButton.addActionListener(event -> redo());
        historyActions.add(undoButton);
        historyActions.add(redoButton);
        actions.add(historyActions);

        JButton editShape = new JButton("Edit selected");
        editShape.addActionListener(event -> editSelectedShape());
        actions.add(editShape);
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
        addPreviewListener(sceneName);
        addPreviewListener(canvasWidth);
        addPreviewListener(canvasHeight);
        refreshSceneCode();
        updateHistoryButtons();
        setSize(1080, 650);
        setMinimumSize(new java.awt.Dimension(900, 580));
        setLocationRelativeTo(null);
    }

    private void stylePanel(JPanel panel) {
        panel.setBackground(new Color(31, 41, 55));
    }

    // Adds text fields and selectors to the property grid.
    private void addField(JPanel panel, String label, java.awt.Component field) {
        GridBagConstraints labelConstraints = new GridBagConstraints();
        labelConstraints.gridx = 0;
        labelConstraints.gridy = fieldRow;
        labelConstraints.weightx = 0;
        labelConstraints.fill = GridBagConstraints.HORIZONTAL;
        labelConstraints.anchor = GridBagConstraints.WEST;
        labelConstraints.insets = new java.awt.Insets(5, 8, 5, 14);

        JLabel fieldLabel = new JLabel(label);
        fieldLabel.setPreferredSize(new java.awt.Dimension(135, 30));
        panel.add(fieldLabel, labelConstraints);

        GridBagConstraints fieldConstraints = new GridBagConstraints();
        fieldConstraints.gridx = 1;
        fieldConstraints.gridy = fieldRow++;
        fieldConstraints.weightx = 1;
        fieldConstraints.fill = GridBagConstraints.HORIZONTAL;
        fieldConstraints.anchor = GridBagConstraints.WEST;
        fieldConstraints.insets = new java.awt.Insets(5, 0, 5, 8);

        if (field instanceof JTextField) {
            field.setPreferredSize(new java.awt.Dimension(220, 30));
        } else if (field instanceof JComboBox<?>) {
            field.setPreferredSize(new java.awt.Dimension(220, 32));
        } else if (field == colorButton) {
            field.setPreferredSize(new java.awt.Dimension(260, 42));
        }
        panel.add(field, fieldConstraints);
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
        fieldRow = 0;
        addField(fields, "SHAPE", shapeChoice);
        addField(fields, "COLOR", colorButton);

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
        } else if (!"Line".equals(shape)) {
            addField(fields, "Style", fillStyle);
        }

        addShapeButton = new JButton(editingIndex >= 0 ? "Update selected" : "+ Add shape");
        addShapeButton.addActionListener(event -> addShape());
        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        buttonRow.setBackground(new Color(31, 41, 55));
        addShapeButton.setPreferredSize(new java.awt.Dimension(150, 34));
        buttonRow.add(addShapeButton);

        JButton removeShape = new JButton("Remove selected");
        removeShape.setPreferredSize(new java.awt.Dimension(150, 34));
        removeShape.addActionListener(event -> removeSelectedShape());
        buttonRow.add(removeShape);
        GridBagConstraints buttonConstraints = new GridBagConstraints();
        buttonConstraints.gridx = 0;
        buttonConstraints.gridy = fieldRow++;
        buttonConstraints.gridwidth = 2;
        buttonConstraints.weightx = 1;
        buttonConstraints.fill = GridBagConstraints.HORIZONTAL;
        buttonConstraints.anchor = GridBagConstraints.WEST;
        buttonConstraints.insets = new java.awt.Insets(16, 8, 8, 8);
        fields.add(buttonRow, buttonConstraints);

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
            saveUndoSnapshot();
            if (editingIndex >= 0) {
                sceneObjects.set(editingIndex, definition);
                objectListModel.setElementAt(summary, editingIndex);
                objectList.setSelectedIndex(editingIndex);
                editingIndex = -1;
                addShapeButton.setText("+ Add shape");
            } else {
                sceneObjects.add(definition);
                objectListModel.addElement(summary);
            }
            refreshSceneCode();
        } catch (NumberFormatException error) {
            showError("Use whole numbers for the shape details.");
        } catch (IllegalArgumentException error) {
            showError(error.getMessage());
        }
    }

    // Loads a selected shape's saved definition back into the form for editing.
    private void editSelectedShape() {
        int selected = objectList.getSelectedIndex();
        if (selected < 0) {
            showError("Select a shape from the Objects tab first.");
            return;
        }

        String definition = sceneObjects.get(selected);
        String shape = definition.substring(0, definition.indexOf(' '));
        Matcher base = Pattern.compile("Color \\((\\d+), (\\d+), (\\d+)\\) at \\((-?\\d+), (-?\\d+)\\)").matcher(definition);
        if (!base.find()) {
            showError("I couldn’t load that shape into the editor.");
            return;
        }

        try {
            int red = Integer.parseInt(base.group(1));
            int green = Integer.parseInt(base.group(2));
            int blue = Integer.parseInt(base.group(3));
            x.setText(base.group(4));
            y.setText(base.group(5));
            selectedColor = new Color(red, green, blue);
            colorButton.setBackground(selectedColor);
            colorButton.setForeground(red + green + blue < 380 ? Color.WHITE : Color.BLACK);
            shapeChoice.setSelectedItem(shape);

            if (definition.endsWith(" Outline;")) {
                fillStyle.setSelectedItem("Outline");
            } else {
                fillStyle.setSelectedItem("Filled");
            }

            Matcher dimensions = Pattern.compile("Height (-?\\d+) Width (-?\\d+)").matcher(definition);
            if (dimensions.find()) {
                height.setText(dimensions.group(1));
                width.setText(dimensions.group(2));
            }
            Matcher point = Pattern.compile("\\) \\((-?\\d+), (-?\\d+)\\)").matcher(definition);
            if ("Parallelogram".equals(shape) && point.find()) {
                x2.setText(point.group(1));
                y2.setText(point.group(2));
            }
            Matcher lineEnd = Pattern.compile(" to \\((-?\\d+), (-?\\d+)\\)").matcher(definition);
            if ("Line".equals(shape) && lineEnd.find()) {
                x2.setText(lineEnd.group(1));
                y2.setText(lineEnd.group(2));
            }
            Matcher number = Pattern.compile("Radius (-?\\d+)").matcher(definition);
            if (number.find()) {
                if ("RoundedRectangle".equals(shape)) {
                    cornerRadius.setText(number.group(1));
                } else {
                    radius.setText(number.group(1));
                }
            }
            Matcher polygon = Pattern.compile("Sides (-?\\d+) Radius (-?\\d+)").matcher(definition);
            if (polygon.find()) {
                sides.setText(polygon.group(1));
                radius.setText(polygon.group(2));
            }
            Matcher ellipse = Pattern.compile("Width (-?\\d+) Height (-?\\d+)").matcher(definition);
            if ("Ellipse".equals(shape) && ellipse.find()) {
                width.setText(ellipse.group(1));
                height.setText(ellipse.group(2));
            }
            if ("Text".equals(shape)) {
                Matcher text = Pattern.compile("\\\"(.*)\\\";").matcher(definition);
                if (text.find()) {
                    textValue.setText(text.group(1));
                }
            }

            editingIndex = selected;
            addShapeButton.setText("Update selected");
            fields.revalidate();
            fields.repaint();
        } catch (IllegalArgumentException error) {
            showError("I couldn’t load that shape into the editor.");
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
            saveUndoSnapshot();
            objectListModel.remove(selected);
            sceneObjects.remove(selected);
            refreshSceneCode();
        }
    }

    // Saves the current object list before a change so it can be undone.
    private void saveUndoSnapshot() {
        ArrayList<String> labels = new ArrayList<>();
        for (int i = 0; i < objectListModel.size(); i++) {
            labels.add(objectListModel.getElementAt(i));
        }
        undoHistory.push(new SceneSnapshot(new ArrayList<>(sceneObjects), labels));
        redoHistory.clear();
        updateHistoryButtons();
    }

    // Restores an earlier or later version of the scene object list.
    private void restoreSnapshot(SceneSnapshot snapshot) {
        sceneObjects.clear();
        sceneObjects.addAll(snapshot.definitions);
        objectListModel.clear();
        for (String label : snapshot.labels) {
            objectListModel.addElement(label);
        }
        editingIndex = -1;
        objectList.clearSelection();
        addShapeButton.setText("+ Add shape");
        refreshSceneCode();
        updateHistoryButtons();
    }

    private SceneSnapshot currentSnapshot() {
        ArrayList<String> labels = new ArrayList<>();
        for (int i = 0; i < objectListModel.size(); i++) {
            labels.add(objectListModel.getElementAt(i));
        }
        return new SceneSnapshot(new ArrayList<>(sceneObjects), labels);
    }

    private void undo() {
        if (!undoHistory.isEmpty()) {
            redoHistory.push(currentSnapshot());
            restoreSnapshot(undoHistory.pop());
        }
    }

    private void redo() {
        if (!redoHistory.isEmpty()) {
            undoHistory.push(currentSnapshot());
            restoreSnapshot(redoHistory.pop());
        }
    }

    private void updateHistoryButtons() {
        if (undoButton != null) {
            undoButton.setEnabled(!undoHistory.isEmpty());
            redoButton.setEnabled(!redoHistory.isEmpty());
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

    // Updates the code preview as the scene name or canvas dimensions change.
    private void addPreviewListener(JTextField field) {
        field.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent event) {
                refreshSceneCode();
            }

            @Override
            public void removeUpdate(DocumentEvent event) {
                refreshSceneCode();
            }

            @Override
            public void changedUpdate(DocumentEvent event) {
                refreshSceneCode();
            }
        });
    }

    // Saves the current scene definition as a reusable text file.
    private void saveScene() {
        refreshSceneCode();
        JFileChooser chooser = new JFileChooser(new File("."));
        chooser.setSelectedFile(new File(sceneName.getText().trim() + ".txt"));
        if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }

        try {
            Files.write(chooser.getSelectedFile().toPath(),
                    scenePreview.getText().getBytes(StandardCharsets.UTF_8));
        } catch (IOException error) {
            showError("Couldn’t save that scene: " + error.getMessage());
        }
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
