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

- `src/` contains the Java source files.
- `examples/` contains valid scene files you can open in the application.
- `tests/invalid/` contains scene files that should be rejected by the parser.
- `ParserTests.java` is included in `src/` and runs the parser regression tests.

## Run

Import the repository into Eclipse as an existing Java project. The project is configured to use `src/` for source files and `bin/` for compiled output. Run the `Main` class and choose a scene file from the `examples/` folder.

## Tests

From the project root, compile the Java files and run the test suite:

```bash
mkdir -p bin
javac -d bin src/*.java
java -cp bin ParserTests
```

The tests run without opening a Swing window. They check every scene file in `examples/`, verify that files in `tests/invalid/` are rejected, and test missing punctuation and invalid numbers. Run the commands from the project root so the example files can be found.
