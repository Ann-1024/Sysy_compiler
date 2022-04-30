package compiler.lexer;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {
    String chunk;
    int line;
    TokenKind kind;
    String name;
    int nextLine;
    TokenKind nextKind;
    String nextName;
    HashMap<String,TokenKind> kw;

    //regex
    Pattern dec_int = Pattern.compile("[1-9][0-9]*");
    Pattern octal_int = Pattern.compile("0[0-7]*");
    Pattern hex_int = Pattern.compile("(0x|0X)[0-9a-fA-F]*");
    Pattern reg_float = Pattern.compile("((0|[1-9][0-9]*)\\.(0|[1-9][0-9]*)?((e|E)(\\+|-)?[0-9]*)?)|(0|([1-9][0-9]*))(e|E)(((\\+|-)?[0-9]*))?");
    Pattern reg_id = Pattern.compile("[_a-zA-Z][_a-zA-Z0-9]*");
    public Lexer(String code){
        chunk = code+"$";
        line = 1;
        name = "";
        nextLine = 0;
        kw = new HashMap<>();
        kw.put("const",TokenKind.ConstKeyword);
        kw.put("if",TokenKind.IfKeyword);
        kw.put("else",TokenKind.ElseKeyword);
        kw.put("while",TokenKind.WhileKeyword);
        kw.put("break",TokenKind.BreakKeyword);
        kw.put("continue",TokenKind.ContinueKeyword);
        kw.put("return",TokenKind.ReturnKeyword);
        kw.put("int",TokenKind.IntType);
        kw.put("float",TokenKind.FloatType);
        kw.put("void",TokenKind.VoidType);
    }
    public void skipWhiteSpace(){
        int i = 0;
        while(true){
            if(chunk.charAt(i)==' '||chunk.charAt(i)=='\t'||chunk.charAt(i)=='\r'){
                i++;
            }else if(chunk.charAt(i)=='\n'){
                i++;
                line++;
            }
            else {
                break;
            }
        }
        chunk = chunk.substring(i);
    }
    private void next(int i){
        chunk = chunk.substring(i);
    }
    public Token nextToken() throws Exception {
        if(nextLine>0){
            Token t = new Token(nextLine,nextKind,nextName);
            nextLine = 0;
            return t;
        }
        skipWhiteSpace();
        //match punctuations and operators
        char c = chunk.charAt(0);
        switch(c){
            case '，':
                kind = TokenKind.Comma;
                name = ",";
                next(1);
                return getToken();
            case ';':
                kind = TokenKind.Semicolon;
                name = ";";
                next(1);
                return getToken();
            case '(':
                kind = TokenKind.LParen;
                name = "(";
                next(1);
                return getToken();
            case ')':
                kind = TokenKind.RParen;
                name = ")";
                next(1);
                return getToken();
            case '[':
                kind = TokenKind.LBracket;
                name = "[";
                next(1);
                return getToken();
            case ']':
                kind = TokenKind.RBracket;
                name = "]";
                next(1);
                return getToken();
            case '{':
                kind = TokenKind.LCurly;
                name = "{";
                next(1);
                return getToken();
            case '}':
                kind = TokenKind.RCurly;
                name = "}";
                next(1);
                return getToken();
            case '+':
                kind = TokenKind.OpAdd;
                name = "+";
                next(1);
                return getToken();
            case '-':
                kind = TokenKind.OpSub;
                name = "-";
                next(1);
                return getToken();
            case '*':
                kind = TokenKind.OpMul;
                name = "*";
                next(1);
                return getToken();
            case '/':
                if(chunk.charAt(1)=='/') {
                    skipShortComment();
                    return nextToken();
                } else if(chunk.charAt(1)=='*'){
                    skipLongComment();
                    return nextToken();
                }
                kind = TokenKind.OpDiv;
                name = "/";
                next(1);
                return getToken();
            case '%':
                kind = TokenKind.OpMod;
                name = "%";
                next(1);
                return getToken();
            case '&':
                if(chunk.charAt(1)=='&'){
                    kind = TokenKind.OpAnd;
                    name = "&&";
                    next(2);
                } else{
                    throw new Exception("symbols can not resolve");
                }
                return getToken();
            case '|':
                if(chunk.charAt(1)=='|'){
                    kind = TokenKind.OpOr;
                    name = "||";
                    next(2);
                } else{
                    throw new Exception("symbols can not resolve");
                }
                return getToken();
            case '=':
                if(chunk.charAt(1)=='='){
                    kind = TokenKind.OpEQ;
                    name = "==";
                    next(2);
                } else{
                    kind = TokenKind.OpAsg;
                    name = "=";
                    next(1);
                }
                return getToken();
            case '>':
                if(chunk.charAt(1)=='='){
                    kind = TokenKind.OpGE;
                    name = ">=";
                    next(2);
                } else{
                    kind = TokenKind.OpGT;
                    name = ">";
                    next(1);
                }
                return getToken();
            case '<':
                if(chunk.charAt(1)=='='){
                    kind = TokenKind.OpLE;
                    name = "<=";
                    next(2);
                } else{
                    kind = TokenKind.OpLT;
                    name = "<";
                    next(1);
                }
                return getToken();
            case '!':
                if(chunk.charAt(1)=='='){
                    kind = TokenKind.OpNE;
                    name = "!=";
                    next(2);
                } else{
                    kind = TokenKind.OpNot;
                    name = "!";
                    next(1);
                }
                return getToken();
            case '$':
                kind = TokenKind.EOF;
                name = "$";
                return getToken();
            default:
        }
        if(c>='0'&&c<='9'){
            scanNumber();
            return getToken();
        }
        else if(c=='_'||isDigit(c)){
            scanIdentifier();
            return getToken();
        }
        else{
            throw new Exception("Symbol can not resolve!");
        }
    }
    public void skipShortComment() {
        int i = 2;
        while(chunk.charAt(i)!='\n'){
            i++;
        }
        chunk = chunk.substring(i);
    }
    public void skipLongComment() throws Exception {
        int i =  chunk.indexOf("*\\",2);
        if (i==-1){
            throw new Exception("symbols can not resolve");
        }
        chunk = chunk.substring(i+2);
    }
    public boolean isDigit(char c) {
        if(c>='a'&&c<='z'||c>='A'&&c<='Z'){
            return true;
        }
        return false;
    }
    public void scanNumber() throws Exception {
        Matcher m = reg_float.matcher(chunk);
        if(m.lookingAt()){
            kind = TokenKind.FloatConst;
            name = chunk.substring(0,m.end());
            next(m.end());
            return;
        }
        m = dec_int.matcher(chunk);
        if(m.lookingAt()){
            kind = TokenKind.DecIntConst;
            name = chunk.substring(0,m.end());
            next(m.end());
            return;
        }
        m = hex_int.matcher(chunk);
        if(m.lookingAt()){
            kind = TokenKind.HexIntConst;
            name = chunk.substring(0,m.end());
            next(m.end());
            return;
        }
        m = octal_int.matcher(chunk);
        if(m.lookingAt()){
            kind = TokenKind.OctalIntConst;
            name = chunk.substring(0,m.end());
            next(m.end());
            return;
        }
        throw new Exception("number that can not resolve!");
    }
    private Token getToken(){
        return new Token(line,kind,name);
    }
    public void scanIdentifier(){
        Matcher m = reg_id.matcher(chunk);
        if(m.lookingAt()){
            name = chunk.substring(0,m.end());
            if(kw.containsKey(name)){
                kind = kw.get(name);
            }
            else{
                kind = TokenKind.Ident;
            }
            chunk = chunk.substring(m.end());
        }
    }
    public void TokenOfKind(TokenKind tokenKind) throws Exception {
        Token t = nextToken();
        if(t.kind!=tokenKind){
            throw new Exception("expect token"+tokenKind);
        }

    }
    public TokenKind LookAhead() throws Exception {
        if(nextLine > 0){
            return nextKind;
        }
        Token t = nextToken();
        nextLine = t.line;
        nextKind = t.kind;
        nextName = t.name;
        return nextKind;
    }
    public void lex() throws Exception {
        while(kind != TokenKind.EOF){
            Token token = nextToken();
            System.out.println(token);
            TokenKind kind = LookAhead();
            System.out.println(kind);
        }
    }
    public static void main(String[] args) throws Exception {
        String a = "for i = 1 == while \n 1. 1E+";
        Lexer lexer = new Lexer(a);
        lexer.lex();
    }
}
