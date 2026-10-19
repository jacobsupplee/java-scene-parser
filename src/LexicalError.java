// Java Scene Parser
// Jacob Supplee
// October 2026
// Academic project for UMGC CMSC 330; based on course-provided skeleton.

class LexicalError extends Exception
{
    // Constructor that creates a lexical error object given the line number and error

    public LexicalError(int line, String description)    {
        super("Lexical Error on Line: " + line + " " + description);
    }
}