// CMSC 330 Advanced Programming Languages
// Parser test cases
// Jacob Supplee
// October 2026

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

// Runs parser tests without opening a scene window
class ParserTests {

    public static void main(String[] args) throws Exception {
        System.setProperty("java.awt.headless", "true");

        testValidScene();
        testMissingPeriod();
        testInvalidNumber();
        testMissingSemicolon();

        System.out.println("All parser tests passed.");
    }

    // Tests a valid scene with one rectangle
    private static void testValidScene() throws Exception {
        String input = "Scene TestScene (300, 300)\n"
                + "Rectangle Color (255, 0, 0) at (50, 50) Height 100 Width 100;\n"
                + "End.\n";

        parseScene("valid scene", input);
    }

    // Tests that a scene without a final period is rejected
    private static void testMissingPeriod() throws Exception {
        String input = "Scene TestScene (300, 300)\n"
                + "Rectangle Color (255, 0, 0) at (50, 50) Height 100 Width 100;\n"
                + "End\n";

        expectSyntaxError("missing period", input);
    }

    // Tests that a word is rejected where a number is expected
    private static void testInvalidNumber() throws Exception {
        String input = "Scene TestScene (300, 300)\n"
                + "RegularPolygon Color (255, 0, 255) at (150, 150) Sides six Radius 80;\n"
                + "End.\n";

        expectSyntaxError("invalid number", input);
    }

    // Tests that an image without a semicolon is rejected
    private static void testMissingSemicolon() throws Exception {
        String input = "Scene TestScene (300, 300)\n"
                + "Rectangle Color (255, 0, 0) at (50, 50) Height 100 Width 100\n"
                + "End.\n";

        expectSyntaxError("missing semicolon", input);
    }

    // Parses valid input from a temporary file
    private static void parseScene(String testName, String input) throws Exception {
        File file = createInputFile(input);
        try {
            Parser parser = new Parser(file);
            parser.parseScene();
            System.out.println("PASS: " + testName);
        } finally {
            Files.deleteIfExists(file.toPath());
        }
    }

    // Checks that malformed input produces a syntax error
    private static void expectSyntaxError(String testName, String input) throws Exception {
        File file = createInputFile(input);
        try {
            Parser parser = new Parser(file);
            try {
                parser.parseScene();
                throw new AssertionError("FAIL: " + testName + " was accepted");
            } catch (SyntaxError error) {
                System.out.println("PASS: " + testName + " was rejected");
            }
        } finally {
            Files.deleteIfExists(file.toPath());
        }
    }

    // Creates a temporary file for each test case
    private static File createInputFile(String input) throws IOException {
        Path file = Files.createTempFile("scene-parser-test-", ".txt");
        Files.write(file, input.getBytes(StandardCharsets.UTF_8));
        return file.toFile();
    }
}
