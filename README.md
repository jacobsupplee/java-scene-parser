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

## How It Works

1. Load a scene definition file.
2. The lexer identifies tokens in the input.
3. The parser validates the structure and creates scene objects.
4. Swing renders the scene in a window.

## Run

Import the project into Eclipse as an existing Java project, configure a compatible JDK, and run the `Main` class. Use the included sample `.txt` files to try different scenes.

## Tests

The `ParserTests` class automatically checks every `.txt` file in the project directory. Files beginning with `INVALID_` are expected to be rejected; all other scene files are expected to parse successfully. It also tests missing punctuation and invalid numbers using temporary files.

From the project directory, compile the Java files and run the tests:

```bash
javac *.java
java ParserTests
```

The tests run without opening a Swing window and report a final pass/fail count. Run them from the project directory so the sample `.txt` files can be found.
