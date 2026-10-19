// Java Scene Parser
// Jacob Supplee
// October 2026

// Class that defines a lexical error

class LexicalError extends Exception
{
    // Constructor that creates a lexical error object given the line number and error

    public LexicalError(int line, String description)    {
        super("Lexical Error on Line: " + line + " " + description);
    }
}