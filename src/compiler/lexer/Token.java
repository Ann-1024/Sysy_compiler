package compiler.lexer;

public class Token {
    public int line;
    public TokenKind kind;
    public String name;
    public Token(int line,TokenKind kind,String name){
        this.line = line;
        this.kind = kind;
        this.name = name;
    }
    @Override
    public String toString() {
        return "Token{" +
                "line=" + line +
                ", kind=" + kind +
                ", name='" + name + '\'' +
                '}';
    }
}
