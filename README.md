# Java Scene Parser

This project reads a scene description from a text file and uses it to draw shapes in a Java Swing window. The input describes the scene, and the lexer and parser turn that text into objects the program can render.

## What it does

- Reads scene definitions from text files
- Breaks the input into tokens and checks that the syntax is valid
- Reports lexical and syntax errors for invalid input
- Creates and draws shapes in a Swing window
- Supports rectangles, triangles, parallelograms, regular polygons, and text
- Uses separate classes to represent the different scene objects

## Built with

- Java
- Swing and AWT
- File I/O
- Lexical analysis and parsing
- Object-oriented programming

## How it works

1. The program loads a scene definition file.
2. The lexer identifies the tokens in the input.
3. The parser checks the structure and determines which objects to create.
4. The application renders the resulting scene in a Swing window.

## Running the project

The project includes Eclipse metadata, so the easiest way to run it is to import it into Eclipse as an existing Java project. Make sure a compatible JDK is configured, then run the `Main` class. The application lets you select a scene file, and sample `.txt` files are included to try different inputs.

## Notes

This project started as coursework for a Java programming class. I extended the parser to handle additional shapes, including isosceles triangles, parallelograms, regular polygons, and text. It gave me practical experience with parsing, input validation, and turning text-based input into a graphical output.
