package compiler.parser;

import compiler.ast.*;
import compiler.ast.Interface.ConstDef;
import compiler.ast.Interface.Exp;
import compiler.ast.InitVar;
import compiler.ast.Interface.Init;
import compiler.ast.Interface.VarDef;
import compiler.lexer.Lexer;
import compiler.lexer.Token;
import compiler.lexer.TokenKind;

public class Parser {
    Lexer lexer;
    public Parser (String str){
        lexer = new Lexer(str);
    }
    public CompUnit parse() throws Exception {
        CompUnit compUnit = new CompUnit();
        compUnit.start = 0;
        while(lexer.LookAhead()!= TokenKind.EOF){
            compUnit.children.add(parse_DeclOrFunc());
        }
        compUnit.end = lexer.nextToken().line;
        return compUnit;
    }

    private Node parse_DeclOrFunc() throws Exception {
        Token t = lexer.nextToken();
        if(t.kind == TokenKind.ConstKeyword){
            return parse_ConstDecl(t.line);
        } else{
            Token t1 = lexer.nextToken();
            if(lexer.LookAhead()==TokenKind.LParen){
                return parse_Func(t,t1);
            } else {
                return parse_VarDecl(t,t1);
            }
        }
    }

    private Node parse_Decl() throws Exception {
        Token t = lexer.nextToken();
        if(t.kind==TokenKind.ConstKeyword){
           lexer.nextToken();
            return parse_ConstDecl(t.line);
        }
        else{
            return parse_VarDecl(t,lexer.nextToken());
        }
    }

    private Node parse_ConstDecl(int start) throws Exception {
        ConstDecl constDecl = new ConstDecl(start);
        Token t = lexer.nextToken();
        if(t.kind==TokenKind.IntType){
            while(lexer.LookAhead()!=TokenKind.Semicolon){
                constDecl.children.add(parse_ConstIntDef());
                lexer.TokenOfKind(TokenKind.Comma);
            }
        }
        else if(t.kind==TokenKind.FloatType){
            while(lexer.LookAhead()!=TokenKind.Semicolon){
                constDecl.children.add(parse_ConstFloatDef());
                lexer.TokenOfKind(TokenKind.Comma);
            }
        }
        else{
            throw new Exception("invalid type!");
        }
        t = lexer.nextToken();
        constDecl.end = t.line;
        return constDecl;
    }

    private ConstDef parse_ConstIntDef() throws Exception {
        Token t = lexer.nextToken();
        if(lexer.LookAhead()==TokenKind.LBracket){
            ArrayDef arrayDef = new ArrayDef(t);
            arrayDef.nodeType = NodeType.IntArrayConstDef;
            arrayDef.values = parse_InitVar();
            return arrayDef;
        }
        lexer.TokenOfKind(TokenKind.OpAsg);
        ValDef valDef = new ValDef(t);
        valDef.nodeType = NodeType.IntConstDef;
        valDef.exp = parse_Exp();
        t = lexer.nextToken();
        valDef.end = t.line;
        if(t.kind!=TokenKind.Semicolon){
            throw new Exception("expect semicolon");
        }
        return valDef;
    }

    private ConstDef parse_ConstFloatDef() throws Exception {
        Token t = lexer.nextToken();
        if(lexer.LookAhead()==TokenKind.LBracket){
            ArrayDef arrayDef =  new ArrayDef(t);
            arrayDef.nodeType = NodeType.FloatArrayConstDef;
            parse_ArrayDef(arrayDef,NodeType.FloatArrayConstDef);
            t = lexer.nextToken();
            arrayDef.end = t.line;
            return arrayDef;
        }
        lexer.TokenOfKind(TokenKind.OpAsg);
        ValDef valDef = new ValDef(t);
        valDef.nodeType = NodeType.FloatConstDef;
        valDef.exp = parse_Exp();
        t = lexer.nextToken();
        valDef.end = t.line;
        if(t.kind!=TokenKind.Semicolon){
            throw new Exception("expect semicolon");
        }
        return valDef;
    }

    private void parse_ArrayDef(ArrayDef arrayDef,NodeType nodeType) throws Exception {
        while(lexer.LookAhead()!=TokenKind.OpAsg){
            lexer.TokenOfKind(TokenKind.LBracket);
            arrayDef.dimensions.add(parse_Exp());
            lexer.TokenOfKind(TokenKind.RBracket);
        }
        if(nodeType==NodeType.FloatArrayConstDef||nodeType==NodeType.IntArrayConstDef){
            lexer.TokenOfKind(TokenKind.OpAsg);
            arrayDef.values = parse_InitVar();
            Token t = lexer.nextToken();
            arrayDef.end = t.line;
        }else if (lexer.LookAhead()==TokenKind.OpAsg){
            lexer.TokenOfKind(TokenKind.OpAsg);
            arrayDef.values = parse_InitVar();
            Token t = lexer.nextToken();
            arrayDef.end = t.line;
        }
        return;
    }

    private InitVar parse_InitVar() throws Exception {
        Token t = lexer.nextToken();
        if(t.kind!=TokenKind.LCurly){
            throw new Exception("except LCurly!");
        }
        InitVar initVar = new InitVar();
        while(lexer.LookAhead()!=TokenKind.RCurly){
            initVar.inits.add(parse_Init());
            lexer.TokenOfKind(TokenKind.Comma);
        }
        initVar.end = lexer.nextToken().line;
        lexer.TokenOfKind(TokenKind.Semicolon);
        return initVar;
    }

    private Init parse_Init() throws Exception {
        Token t = lexer.nextToken();
        if(t.kind==TokenKind.LCurly){
            InitVar initVar = new InitVar();
            while(lexer.LookAhead()!=TokenKind.RCurly){
                initVar.inits.add(parse_Init());
                lexer.TokenOfKind(TokenKind.Comma);
            }
            initVar.end = lexer.nextToken().line;
            return initVar;
        }else{
            return parse_Exp();
        }

    }

    private Node parse_VarDecl(Token t, Token t2) throws Exception {
        VarDecl varDecl = new VarDecl(t.line);
        if(t2.kind==TokenKind.IntType){
            while(lexer.LookAhead()!=TokenKind.Semicolon){
                varDecl.children.add(parse_VarIntDef());
                lexer.TokenOfKind(TokenKind.Comma);
            }
        }
        else if(t2.kind==TokenKind.FloatType){
            while(lexer.LookAhead()!=TokenKind.Semicolon){
                varDecl.children.add(parse_VarFloatDef());
                lexer.TokenOfKind(TokenKind.Comma);
            }
        }
        else{
            throw new Exception("invalid type!");
        }
        t = lexer.nextToken();
        varDecl.end = t.line;
        return varDecl;
    }

    private VarDef parse_VarIntDef() throws Exception {
        Token t = lexer.nextToken();
        if(lexer.LookAhead()==TokenKind.LBracket){
            ArrayDef arrayDef =  new ArrayDef(t);
            arrayDef.nodeType = NodeType.IntArrayDef;
            parse_ArrayDef(arrayDef,NodeType.IntArrayDef);
            t = lexer.nextToken();
            arrayDef.end = t.line;
            return arrayDef;
        }
        lexer.TokenOfKind(TokenKind.OpAsg);
        ValDef valDef = new ValDef(t);
        valDef.nodeType = NodeType.IntDef;
        valDef.exp = parse_Exp();
        t = lexer.nextToken();
        valDef.end = t.line;
        if(t.kind!=TokenKind.Semicolon){
            throw new Exception("expect semicolon");
        }
        return valDef;
    }

    private VarDef parse_VarFloatDef() throws Exception {
        Token t = lexer.nextToken();
        if(lexer.LookAhead()==TokenKind.LBracket){
            ArrayDef arrayDef =  new ArrayDef(t);
            arrayDef.nodeType = NodeType.FloatArrayDef;
            parse_ArrayDef(arrayDef,NodeType.FloatArrayDef);
            t = lexer.nextToken();
            arrayDef.end = t.line;
            return arrayDef;
        }
        lexer.TokenOfKind(TokenKind.OpAsg);
        ValDef valDef = new ValDef(t);
        valDef.nodeType = NodeType.FloatDef;
        valDef.exp = parse_Exp();
        t = lexer.nextToken();
        valDef.end = t.line;
        if(t.kind!=TokenKind.Semicolon){
            throw new Exception("expect semicolon");
        }
        return valDef;
    }



    private Node parse_Func(Token t, Token t1) {
        return null;
    }

    private Exp parse_Exp() {
        return null;
    }
}
