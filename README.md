# Java Scene Parser

A Java application that reads scene definitions from a text file, parses the input, and renders the resulting shapes in a Swing window.

## Features

- Reads scene definitions from text files
- Tokenizes input and validates syntax
- Reports lexical and syntax errors
- Renders rectangles, triangles, parallelograms, regular polygons, and text
- Uses separate classes to represent scene objects

## Built With

- Java
- Swing and AWT
- File I/O
- Lexical analysis and parsing
- Object-oriented programming

## Project Structure

- Java source files are in the project root.
- `examples/` contains valid scene files you can open in the application.
- `tests/invalid/` contains scene files that should be rejected by the parser.
- `ParserTests.java` runs the example and invalid-input tests.

## Run

Import the project into Eclipse as an existing Java project, configure a compatible JDK, and run the `Main` class. When prompted, choose a scene file from the `examples/` folder.

## Tests

From the project root, compile the Java files and run the test suite:

```bash
javac *.java
java ParserTests
```

The tests run without opening a Swing window. They check every scene file in `examples/`, verify that files in `tests/invalid/` are rejected, and test missing punctuation and invalid numbers. The runner reports a final pass/fail count.
