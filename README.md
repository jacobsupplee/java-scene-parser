# Java Scene Parser

A Java Swing application for creating geometric scenes with a graphical builder or a small text-based scene language. The lexer and parser validate each scene definition, then construct the corresponding Java drawing objects.

This project started as a UMGC CMSC 330 academic parser project and was extended with a graphical scene editor, additional drawing primitives, file support, and regression tests.

## Features

### Scene Studio
- Choose from 10 shape types: rectangle, right triangle, isosceles triangle, parallelogram, regular polygon, circle, ellipse, line, rounded rectangle, and text
- Shape-specific controls that show only the properties relevant to the selected shape
- RGB color picker and filled/outline styles where supported
- Add, select, edit, and remove scene objects
- Undo and redo object-list changes
- Live scene-code preview
- Render the current scene, load an existing `.txt` scene, and save a scene definition for reuse

### Parser and renderer
- Lexer and recursive-descent parser for the scene-description language
- Syntax and lexical error reporting for malformed scene files
- Separate drawing classes for supported shapes
- Example scenes and invalid fixtures for regression testing

## Built With

- Java
- Swing and AWT
- Object-oriented programming and inheritance
- File I/O
- Lexical analysis and recursive-descent parsing

## Project Structure

- `src/` — Java source files, parser, drawing classes, Scene Studio, and tests
- `examples/` — valid scene files, including `everything_demo.txt` which demonstrates every supported shape
- `tests/invalid/` — malformed scene files that should be rejected
- `src/ParserTests.java` — regression test runner for valid and invalid scenes

## Getting Started

### Requirements
- A Java JDK installed
- Eclipse IDE (or another Java IDE), if you prefer to run the project in an IDE

### Run in Eclipse
1. Clone or download this repository.
2. Import it into Eclipse as an existing Java project.
3. Run `Main.java` to open Scene Studio. `SceneBuilder.java` also provides a launcher entry point.
4. Choose a shape, set its properties and color, then click **+ Add shape**.
5. Use the Objects panel to select, edit, or remove objects. Undo and redo are available in the same panel.
6. Click **Render scene** to parse and display the scene.
7. Use **Scene code** to inspect the generated definition, **Save scene as...** to save it, or **Load a .txt scene** to open an existing file.

The file chooser starts in the application's working directory. From the project root, the examples are in `examples/`.

### Run the parser tests

Run these commands from the project root with a JDK installed:

```bash
mkdir -p bin
javac -d bin src/*.java
java -cp bin ParserTests
```

The tests run in headless mode and do not need to open the drawing window. They process valid example files, reject invalid fixtures, and check malformed input such as missing punctuation, invalid numbers, unexpected characters, invalid RGB values, missing shape properties, and trailing input.

## Scene File Syntax

A scene begins with `Scene`, a name, and the canvas dimensions. Each shape definition ends with a semicolon. The scene ends with `End.`.

```text
Scene Example (500, 400)
    Rectangle Color (0, 128, 255) at (40, 50) Height 120 Width 180 Filled;
    RegularPolygon Color (255, 0, 255) at (300, 200) Sides 6 Radius 70 Outline;
    Text Color (0, 0, 0) at (40, 220) "Hello World";
End.
```

### Supported shapes

In the table below, `style?` means the style is optional and can be either `Filled` or `Outline`. Lines and text do not use the fill-style option.

| Shape | Syntax after the shape name |
| --- | --- |
| Rectangle | `Color (r, g, b) at (x, y) Height h Width w style?;` |
| Right triangle | `Color (r, g, b) at (x, y) Height h Width w style?;` |
| Isosceles triangle | `Color (r, g, b) at (x, y) Height h Width w style?;` |
| Parallelogram | `Color (r, g, b) at (x1, y1) (x2, y2) Offset n style?;` |
| Regular polygon | `Color (r, g, b) at (x, y) Sides n Radius r style?;` |
| Circle | `Color (r, g, b) at (x, y) Radius r style?;` |
| Ellipse | `Color (r, g, b) at (x, y) Width w Height h style?;` |
| Line | `Color (r, g, b) at (x1, y1) to (x2, y2);` |
| Rounded rectangle | `Color (r, g, b) at (x, y) Height h Width w Radius r style?;` |
| Text | `Color (r, g, b) at (x, y) "your text";` |

Coordinates are measured from the canvas's upper-left corner. RGB components are integers from 0 through 255. Text is enclosed in double quotes. Shape keywords are case-insensitive; names such as `RightTriangle` and `RegularPolygon` are written without spaces.

### Grammar overview

This is a compact overview of the scene language. The notation `|` means “or,” and `?` marks an optional style.

```text
scene             := "Scene" identifier number-list image-list "End" "."
number-list       := "(" integer "," integer ")"
image-list        := (rectangle | right-triangle | isosceles | parallelogram
                    | regular-polygon | circle | ellipse | line
                    | rounded-rectangle | text-image)*
rectangle         := "Rectangle" color at-point dimensions style? ";"
right-triangle    := "RightTriangle" color at-point dimensions style? ";"
isosceles         := "Isosceles" color at-point dimensions style? ";"
parallelogram     := "Parallelogram" color at-point point "Offset" integer style? ";"
regular-polygon   := "RegularPolygon" color at-point "Sides" integer "Radius" integer style? ";"
circle            := "Circle" color at-point "Radius" integer style? ";"
ellipse           := "Ellipse" color at-point "Width" integer "Height" integer style? ";"
line              := "Line" color at-point "to" point ";"
rounded-rectangle := "RoundedRectangle" color at-point dimensions "Radius" integer style? ";"
text-image        := "Text" color at-point quoted-string ";"
style             := "Filled" | "Outline"
color             := "Color" "(" integer "," integer "," integer ")"
at-point          := "at" point
point             := "(" integer "," integer ")"
dimensions        := "Height" integer "Width" integer
```

## Example Scenes

- `examples/everything_demo.txt` — one scene demonstrating all supported shape types, including filled and outline examples
- `examples/extended_shapes.txt` — ellipse, line, and rounded rectangle examples
- Other files in `examples/` demonstrate individual shapes and composed scenes

To try the complete demo, run Scene Studio and choose **Load a .txt scene**, then open `examples/everything_demo.txt`.

## Screenshots

Screenshots of the finished Scene Studio interface and the all-shapes demo will be added here.

## Notes

This repository retains the academic-project context of the original CMSC 330 work. The added builder and drawing features extend the parser rather than replacing the text-based scene language.
