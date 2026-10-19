// Java Scene Parser
// Jacob Supplee
// October 2026
// Academic project for UMGC CMSC 330; based on course-provided skeleton.

import java.io.*;

// This class provides the lexical analyzer for project 1

class Lexer {

    private FileReader reader;
    private StreamTokenizer tokenizer;
    private String punctuation = ",;.()";
    private Token[] punctuationTokens = {Token.COMMA, Token.SEMICOLON, Token.PERIOD, Token.LEFT_PAREN, Token.RIGHT_PAREN };

    // Constructor that creates a lexical analyzer object given the source file

    public Lexer(File file) throws FileNotFoundException {
        reader = new FileReader(file);
        tokenizer = new StreamTokenizer(reader);
        tokenizer.ordinaryChar('.');
        tokenizer.quoteChar('"');
    }

    // Returns the next token in the input stream
    

    public Token getNextToken() throws LexicalError, IOException {
        int token = tokenizer.nextToken();
        switch (token) {
            case StreamTokenizer.TT_NUMBER:
                return Token.NUMBER;
            case StreamTokenizer.TT_WORD:
                for (Token aToken : Token.values())
                    if (aToken.name().replace("_","").equals(tokenizer.sval.toUpperCase()))
                        return aToken;
                return Token.IDENTIFIER;
            case StreamTokenizer.TT_EOF:
                return Token.EOF;
            case '"':
                return Token.STRING; //Added case to handle STRING token w/ quotation mark
            default:
                for (int i = 0; i < punctuation.length(); i++)
                    if (token == punctuation.charAt(i))
                        return punctuationTokens[i];
        }
        throw new LexicalError(tokenizer.lineno(), "Unexpected character '" + (char) token + "'");
    }

    // Closes the input file
    public void close() throws IOException {
        reader.close();
    }

    // Returns the lexeme associated with the current token

    public String getLexeme() {
        return tokenizer.sval;
    }

    // Returns the numeric value of the current token for numeric tokens

    public int getNumber() {
        return (int) tokenizer.nval;
    }

    // Returns the current line of the input file

    public int getLineNo() {
        return tokenizer.lineno();
    }
}
