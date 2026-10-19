// Java Scene Parser
// Jacob Supplee
// October 2026

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;

// Runs parser tests without opening a scene window
class ParserTests {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) throws Exception {
        System.setProperty("java.awt.headless", "true");

        testSceneFiles(new File("examples"), false);
        testSceneFiles(new File("tests/invalid"), true);
        testMissingPeriod();
        testInvalidNumber();
        testMissingSemicolon();
        testUnexpectedCharacter();

        System.out.println();
        System.out.println("Test results: " + passed + " passed, " + failed + " failed.");
        if (failed > 0)
            throw new AssertionError("Some parser tests failed.");
    }

    // Tests scene files in the selected folder; invalid files should be rejected
    private static void testSceneFiles(File directory, boolean shouldFail) {
        File[] sceneFiles = directory.listFiles((dir, name) -> name.toLowerCase().endsWith(".txt"));

        if (sceneFiles == null || sceneFiles.length == 0) {
            fail(directory.getPath(), "No .txt files found. Run tests from the project directory.");
            return;
        }

        Arrays.sort(sceneFiles, Comparator.comparing(File::getName));
        for (File file : sceneFiles) {
            try (Parser parser = new Parser(file)) {
                parser.parseScene();
                if (shouldFail)
                    fail(file.getName(), "Expected invalid input to be rejected");
                else
                    pass(file.getName());
            } catch (SyntaxError | LexicalError error) {
                if (shouldFail)
                    pass(file.getName() + " rejected");
                else
                    fail(file.getName(), error.getMessage());
            } catch (IOException error) {
                fail(file.getName(), "Could not read file: " + error.getMessage());
            } catch (RuntimeException error) {
                fail(file.getName(), "Unexpected error: " + error.getMessage());
            }
        }
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

    // Tests that an unexpected character produces a lexical error
    private static void testUnexpectedCharacter() throws Exception {
        String input = "Scene TestScene (300, 300)\n@";

        File file = createInputFile(input);
        try {
            try (Parser parser = new Parser(file)) {
                parser.parseScene();
                fail("unexpected character", "Unexpected character was accepted");
            } catch (LexicalError error) {
                pass("unexpected character rejected");
            } catch (SyntaxError error) {
                fail("unexpected character", "Expected a lexical error: " + error.getMessage());
            }
        } finally {
            Files.deleteIfExists(file.toPath());
        }
    }

    // Checks that malformed input produces a syntax error
    private static void expectSyntaxError(String testName, String input) throws Exception {
        File file = createInputFile(input);
        try {
            try (Parser parser = new Parser(file)) {
                try {
                    parser.parseScene();
                    fail(testName, "Malformed input was accepted");
                } catch (SyntaxError error) {
                    pass(testName + " rejected");
                } catch (LexicalError error) {
                    pass(testName + " rejected");
                }
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

    // Reports a successful test
    private static void pass(String testName) {
        passed++;
        System.out.println("PASS: " + testName);
    }

    // Reports a failed test and continues with the remaining cases
    private static void fail(String testName, String reason) {
        failed++;
        System.out.println("FAIL: " + testName + " - " + reason);
    }
}
