# Scene Definition Parser and Graphics Renderer

A Java desktop application that reads a text-based scene description, parses its syntax, constructs graphic objects, and renders the resulting scene in a Swing window.

The project demonstrates how a small domain-specific language can drive a graphical application: the input file describes a scene and its objects, while the lexer and parser interpret that description and create the corresponding shapes.

## Capabilities

- Read a scene definition from a selected file
- Tokenize and parse structured scene syntax
- Validate expected tokens and report lexical or syntax errors
- Construct and render graphic objects in a Swing window
- Support multiple shape types, including triangles, rectangles, parallelograms, regular polygons, and text
- Represent scene elements with separate classes

## Technology

- Java
- Java Swing and AWT
- File I/O
- Lexical analysis and parsing
- Object-oriented design
- Custom exceptions

## Run locally

This repository is organized as a Java project and includes Eclipse project metadata. Open it in Eclipse as an existing project, ensure a compatible JDK is configured, compile the source files, and run the `Main` class. The application prompts for a scene file through a file chooser; sample `.txt` scene descriptions are included in the repository.

If running outside Eclipse, compile all Java source files together and launch `Main` from the compiled output directory. Exact commands can vary by JDK version and project layout.

## Input and output

- **Input:** A text file describing the scene window and graphic objects
- **Processing:** Lexer tokenizes the input; parser validates structure and creates shape instances
- **Output:** A Swing window displaying the parsed scene

## Engineering concepts demonstrated

- Separating input parsing from rendering
- Working with file-based structured input
- Mapping tokens and grammar rules to application objects
- Handling invalid input with explicit error types
- Extending a parser to recognize additional shape definitions

## Project context

This repository originated as coursework and is presented as a technical project showcase. The parser and rendering workflow is the focus; it is not a production graphics editor.