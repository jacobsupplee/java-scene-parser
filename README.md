# Java Scene Parser

A Java Swing application for building geometric scenes through a graphical interface or loading scene definitions from text files. A lexer and parser validate the scene language before the shapes are rendered.

I built this project around a small scene-description language. Instead of drawing each shape directly in Java code, you can describe a scene in a text file and let the parser turn it into a drawing.

## Features

- Interactive Scene Builder with shape selection, RGB color picking, and shape-specific properties
- Add and remove shapes before rendering a scene
- Open existing scene-definition files
- Lexical analysis and syntax validation for scene files
- Clear errors for malformed input, unexpected characters, and invalid RGB color values
- Drawing support for rectangles, right triangles, isosceles triangles, parallelograms, regular polygons, circles, ellipses, lines, rounded rectangles, and text
- Filled or outline styles for geometric shapes
- Separate classes for scene objects and drawing behavior
- A regression test suite covering valid examples and malformed input

## Built With

- Java
- Swing and AWT
- File I/O
- Lexical analysis and recursive-descent parsing
- Object-oriented programming

## Project Structure

- `src/` — Java source files, including `SceneBuilder.java` for the interactive launcher
- `examples/` — valid scene files demonstrating the supported shapes
- `tests/invalid/` — scene files that should be rejected
- `src/ParserTests.java` — regression tests for valid and invalid input

## Scene File Syntax

Every scene starts with `Scene`, a name, and the canvas dimensions. Each shape is written on its own line and ends with a semicolon. The file ends with `End.`.

```text
Scene Example (500, 400)
    Rectangle Color (0, 128, 255) at (40, 50) Height 120 Width 180;
    RegularPolygon Color (255, 0, 255) at (300, 200) Sides 6 Radius 70;
    Text Color (0, 0, 0) at (40, 220) "Hello World";
End.
```

### Supported shapes

| Shape | Syntax after the shape name |
| --- | --- |
| Rectangle | `Color (r, g, b) at (x, y) Height h Width w;` |
| Right triangle | `Color (r, g, b) at (x, y) Height h Width w;` |
| Isosceles triangle | `Color (r, g, b) at (x, y) Height h Width w;` |
| Parallelogram | `Color (r, g, b) at (x1, y1) (x2, y2) Offset n;` |
| Regular polygon | `Color (r, g, b) at (x, y) Sides n Radius r Filled;` |
| Circle | `Color (r, g, b) at (x, y) Radius r Filled;` |
| Ellipse | `Color (r, g, b) at (x, y) Width w Height h Filled;` |
| Line | `Color (r, g, b) at (x1, y1) to (x2, y2);` |
| Rounded rectangle | `Color (r, g, b) at (x, y) Height h Width w Radius r Filled;` |
| Text | `Color (r, g, b) at (x, y) "your text";` |

Coordinates are measured from the canvas's upper-left corner. RGB components must be integers from 0 through 255. Text content must be enclosed in double quotes. Shape keywords are recognized without regard to capitalization, and names such as `RightTriangle` and `RegularPolygon` are accepted without underscores.

### Grammar overview

This is a compact overview of the language recognized by the parser. The notation `|` means “or,” and brackets indicate a repeated or optional structure in this documentation.

```text
scene          := "Scene" identifier number-list image-list "End" "."
number-list    := "(" integer ("," integer)* ")"
image-list     := (rectangle | right-triangle | isosceles | parallelogram
                   | regular-polygon | circle | ellipse | line | rounded-rectangle | text-image)*
rectangle      := "Rectangle" color at-point dimensions ";"
right-triangle := "RightTriangle" color at-point dimensions ";"
isosceles      := "Isosceles" color at-point dimensions ";"
parallelogram  := "Parallelogram" color point point "Offset" integer ";"
regular-polygon:= "RegularPolygon" color at-point "Sides" integer
                   "Radius" integer ";"
circle         := "Circle" color at-point "Radius" integer style ";"
ellipse        := "Ellipse" color at-point "Width" integer "Height" integer style ";"
line           := "Line" color at-point "to" point ";"
rounded-rectangle := "RoundedRectangle" color at-point dimensions "Radius" integer style ";"
text-image     := "Text" color at-point quoted-string ";"
style          := "Filled" | "Outline"
color          := "Color" "(" integer "," integer "," integer ")"
at-point       := "at" point
point          := "(" integer "," integer ")"
dimensions     := "Height" integer "Width" integer
```

## Run

1. Clone or download the repository.
2. Import it into Eclipse as an existing Java project.
3. Run `Main` to open Scene Studio. Pick a shape, tweak its details and color, then hit **Render scene**. Use **Load a .txt scene** to open one of the examples. Geometric shapes can be filled or outline-only, and circles have their own shape option.

The project is configured to use `src/` for source files and `bin/` for compiled output. The file chooser starts in the project working directory, so choosing an example from `examples/` is the simplest way to try it.

## Tests

From the project root, compile the Java files and run the test suite:

```bash
mkdir -p bin
javac -d bin src/*.java
java -cp bin ParserTests
```

The tests run without opening a Swing window. They check the valid examples, confirm that invalid fixtures are rejected, and exercise malformed input such as missing punctuation, invalid numbers, unexpected characters, invalid RGB values, missing shape properties, and trailing input.

Latest local run: **18 passed, 0 failed**.
