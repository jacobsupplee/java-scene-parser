// Java Scene Parser
// Jacob Supplee
// October 2026
// Academic project for UMGC CMSC 330; based on course-provided skeleton.

import java.io.*;
import java.awt.*;
import java.util.*;
import javax.swing.*;
// This class provides the skeleton parser for project 1

class Parser implements AutoCloseable {
    private JFrame window;
    private Token token;
    private Lexer lexer;

    // Constructor to create new lexical analyzer from input file

    public Parser(File file) throws IOException {
        lexer = new Lexer(file);
    }

    // Closes the parser input file
    @Override
    public void close() throws IOException {
        lexer.close();
    }

    // Parses the production
    // scene -> SCENE IDENTIFIER number_list images END '.'

    public Scene parseScene() throws LexicalError, SyntaxError, IOException {
        verifyNextToken(Token.SCENE);
        verifyNextToken(Token.IDENTIFIER);
        String window = lexer.getLexeme();
        int[] dimensions = getNumberList(2);
        Scene scene = new Scene(window, dimensions[0], dimensions[1]);
        parseImages(scene, lexer.getNextToken());
        verifyNextToken(Token.PERIOD);
        token = lexer.getNextToken();
        verifyCurrentToken(Token.EOF);
        return scene;
    }

    // Parses the following productions

    // images -> image images | image
    // image -> right_triangle | rectangle
    // right_triangle -> RIGHT_TRIANGLE COLOR number_list AT number_list HEIGHT NUMBER WIDTH NUMBER ';'
    // rectangle -> RECTANGLE_ COLOR number_list AT number_list HEIGHT NUMBER WIDTH NUMBER ';'

    private void parseImages(Scene scene, Token imageToken) throws LexicalError, SyntaxError, IOException {
        int height, width, offset, radius;
        verifyNextToken(Token.COLOR);
        int[] colors = getNumberList(3);
        Color color;
        try {
            color = new Color(colors[0], colors[1], colors[2]);
        } catch (IllegalArgumentException error) {
            throw new SyntaxError(lexer.getLineNo(), "Color values must be between 0 and 255");
        }
        verifyNextToken(Token.AT);
        int[] location = getNumberList(2);
        Point point = new Point(location[0], location[1]);
        if (imageToken == Token.RIGHT_TRIANGLE) {
            verifyNextToken(Token.HEIGHT);
            verifyNextToken(Token.NUMBER);
            height = lexer.getNumber();
            verifyNextToken(Token.WIDTH);
            verifyNextToken(Token.NUMBER);
            width = lexer.getNumber();
            RightTriangle triangle = new RightTriangle(color, point, height, width);
            scene.addImage(triangle);
        } else if (imageToken == Token.RECTANGLE) {
            verifyNextToken(Token.HEIGHT);
            verifyNextToken(Token.NUMBER);
            height = lexer.getNumber();
            verifyNextToken(Token.WIDTH);
            verifyNextToken(Token.NUMBER);
            width = lexer.getNumber();
            Rectangle rectangle = new Rectangle(color, point, height, width);
            scene.addImage(rectangle);
        } else if (imageToken == Token.ISOSCELES) { //Added ISOSCELES parser
            verifyNextToken(Token.HEIGHT);
            verifyNextToken(Token.NUMBER);
            height = lexer.getNumber();
            verifyNextToken(Token.WIDTH);
            verifyNextToken(Token.NUMBER);
            width = lexer.getNumber();
            IsoscelesTriangle triangle = new IsoscelesTriangle(color, point, height, width); //Constructor
            scene.addImage(triangle); 
        } else if (imageToken == Token.PARALLELOGRAM) { //Added PARALLELOGRAM parser 
            int[] lowerRight = getNumberList(2); 
            Point lowerRightPoint = new Point(lowerRight[0], lowerRight[1]); //Parse the lower-right point           
            verifyNextToken(Token.OFFSET);
            verifyNextToken(Token.NUMBER);
            offset = lexer.getNumber(); //Parse the horizontal offset of the parallelogram
            Parallelogram parallelogram = new Parallelogram(color, point, lowerRightPoint, offset); //Constructor
            scene.addImage(parallelogram); 
        } else if (imageToken == Token.REGULAR_POLYGON) { //Added REGULAR_POLYGON parser   
            verifyNextToken(Token.SIDES); //Parses number of sides
            verifyNextToken(Token.NUMBER);
            int sides = lexer.getNumber();
            verifyNextToken(Token.RADIUS); //Parses radius
            verifyNextToken(Token.NUMBER);
            radius = lexer.getNumber();
            RegularPolygon polygon = new RegularPolygon(color, sides, point, radius); //Constructor
            scene.addImage(polygon); //Adds polygon to scene      
        } else if (imageToken == Token.CIRCLE) {
            verifyNextToken(Token.RADIUS);
            verifyNextToken(Token.NUMBER);
            radius = lexer.getNumber();
            scene.addImage(new Circle(color, point, radius, true));
        } else if (imageToken == Token.TEXT) { //Added TEXT parser
            verifyNextToken(Token.STRING); //Parses text
            String textString = lexer.getLexeme();
            Text text = new Text(color, point, textString); //Constructor
            scene.addImage(text);
        } else {
             throw new SyntaxError(lexer.getLineNo(), "Unexpected image name " + imageToken);
        }
        token = lexer.getNextToken();
        if (token == Token.FILLED || token == Token.OUTLINE) {
            boolean filled = token == Token.FILLED;
            // Apply the style to the shape just added to the scene.
            scene.setLastImageFilled(filled);
            token = lexer.getNextToken();
        }
        verifyCurrentToken(Token.SEMICOLON);
        token = lexer.getNextToken();
        if (token != Token.END)
            parseImages(scene, token);
    }

    // Parses the following productions

    // number_list -> '(' numbers ')'
    // numbers -> NUMBER | NUMBER ',' numbers

    // Returns an array of the numbers in the number list

    private int[]  getNumberList(int count) throws LexicalError, SyntaxError, IOException {
        int[] list = new int[count];
        verifyNextToken(Token.LEFT_PAREN);
        for (int i = 0; i < count; i++) {
            verifyNextToken(Token.NUMBER);
            list[i] = lexer.getNumber();
            token = lexer.getNextToken();
            if (i < count - 1)
                verifyCurrentToken(Token.COMMA);
            else
                verifyCurrentToken(Token.RIGHT_PAREN);
        }
        return list;
    }

    // Returns a list of numbers

    private int[] getNumberList() throws LexicalError, SyntaxError, IOException {
        ArrayList<Integer> list = new ArrayList<Integer>();
        verifyNextToken(Token.LEFT_PAREN);
        do {
            verifyNextToken(Token.NUMBER);
            list.add((int) lexer.getNumber());
            token = lexer.getNextToken();
        }
        while (token == Token.COMMA);
        verifyCurrentToken(Token.RIGHT_PAREN);
        int[] values = new int[list.size()];
        for (int i = 0; i < values.length; i++)
            values[i] = list.get(i);
        return values;
    }

    // Verifies that the next token is the expected token

    private void verifyNextToken(Token expectedToken) throws LexicalError, SyntaxError, IOException {
        token = lexer.getNextToken();
        verifyCurrentToken(expectedToken);
    }

    // Verifies that the current token is the expected token

    private void verifyCurrentToken(Token expectedToken) throws SyntaxError {
        if (token != expectedToken)
            throw new SyntaxError(lexer.getLineNo(), "Expecting token " + expectedToken + " not " + token);
    }
}