package compiler.parser;

import compiler.ast.*;
import compiler.ast.Interface.*;
import compiler.ast.InitVar;
import compiler.lexer.Lexer;
import compiler.lexer.Token;
import compiler.lexer.TokenKind;

import java.util.ArrayList;

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

    private Decl parse_Decl() throws Exception {
        Token t = lexer.nextToken();
        if(t.kind==TokenKind.ConstKeyword){
           lexer.nextToken();
            return parse_ConstDecl(t.line);
        }
        else{
            return parse_VarDecl(t,lexer.nextToken());
        }
    }

    private ConstDecl parse_ConstDecl(int start) throws Exception {
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

    private VarDecl parse_VarDecl(Token t, Token t2) throws Exception {
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

    private Node parse_Func(Token t, Token t1) throws Exception {
        FuncDef funcDef = new FuncDef(t,t1);
        lexer.TokenOfKind(TokenKind.LParen);
        while(lexer.LookAhead()!=TokenKind.RParen){
            funcDef.funcFParams.add(parse_FuncFParam());
        }
        funcDef.block = parse_Block();
        return funcDef;
    }

    private FuncFParam parse_FuncFParam() throws Exception {
        Token t = lexer.nextToken();
        Token t1 = lexer.nextToken();
        FuncFParam funcFParam = new FuncFParam();
        funcFParam.start = t.line;
        funcFParam.identifier = t1.name;
        if(lexer.LookAhead()==TokenKind.LBracket){
            funcFParam.exps = new ArrayList<>();
            while(lexer.LookAhead()!=TokenKind.Comma&&lexer.LookAhead()!=TokenKind.LCurly){
                lexer.TokenOfKind(TokenKind.LBracket);
                funcFParam.exps.add(parse_Exp());
                lexer.TokenOfKind(TokenKind.RBracket);
            }
        }
        t = lexer.nextToken();
        funcFParam.end = t.line;
        if(t.kind==TokenKind.Comma||t.kind==TokenKind.RCurly){
            return funcFParam;
        }
        throw new Exception("FuncFParam parse error!");
    }

    private Block parse_Block() throws Exception {
        Block block = new Block();
        Token t = lexer.nextToken();
        if(t.kind!=TokenKind.LCurly){
            throw new Exception("except Lcurly at the beginning of the block!");
        }
        block.start = t.line;
        while(lexer.LookAhead()!=TokenKind.RCurly){
            block.blockItems.add(parse_BlockItem());
        }
        block.end = lexer.nextToken().line;
        return block;
    }

    private BlockItem parse_BlockItem() throws Exception {
        if(lexer.LookAhead()==TokenKind.ConstKeyword||lexer.LookAhead()==TokenKind.IntType||lexer.LookAhead()==TokenKind.FloatType){
            return parse_Decl();
        }
        return parse_Stmt();
    }

    private Stmt parse_Stmt() throws Exception {
        switch (lexer.LookAhead()){
            case IfKeyword:
                return parse_IfStmt();
            case WhileKeyword:
                return parse_WhileStmt();
            case BreakKeyword:
                return parse_BreakStmt();
            case ContinueKeyword:
                return parse_ContinueStmt();
            case ReturnKeyword:
                return parse_ReturnStmt();
            case Ident:
                Lval lval = parse_Lval();
                if(lexer.LookAhead()==TokenKind.OpAsg){
                    return parse_AssignStmt(lval);
                }
                return parse_Exp(lval);
            default:
                return parse_Exp();
        }
    }
    // 很多Stmt的结束位置没写
    private IfStmt parse_IfStmt() throws Exception {
        IfStmt ifStmt =  new IfStmt();
        ifStmt.start = lexer.nextToken().line;
        ifStmt.cond = parse_Cond();
        ifStmt.stmt1 = parse_Stmt();
        if(lexer.LookAhead()==TokenKind.ElseKeyword){
           lexer.nextToken();
           ifStmt.stmt2 = parse_Stmt();
        }
        return ifStmt;
    }

    private WhileStmt parse_WhileStmt() throws Exception {
        WhileStmt whileStmt = new WhileStmt();
        whileStmt.start = lexer.nextToken().line;
        whileStmt.cond = parse_Cond();
        whileStmt.stmt = parse_Stmt();
        return null;
    }

    private BreakStmt parse_BreakStmt() throws Exception {
        BreakStmt breakStmt = new BreakStmt();
        breakStmt.start = lexer.nextToken().line;
        Token t = lexer.nextToken();
        if(t.kind!=TokenKind.Semicolon){
            throw new Exception("except Semicolon!");
        }
        breakStmt.end = t.line;
        return breakStmt;
    }

    private ContinueStmt parse_ContinueStmt() throws Exception {
        ContinueStmt continueStmt = new ContinueStmt();
        continueStmt.start = lexer.nextToken().line;
        Token t = lexer.nextToken();
        if(t.kind!=TokenKind.Semicolon){
            throw new Exception("except Semicolon!");
        }
        continueStmt.end = t.line;
        return continueStmt;
    }

    private ReturnStmt parse_ReturnStmt() throws Exception {
        ReturnStmt returnStmt = new ReturnStmt();
        returnStmt.start = lexer.nextToken().line;
        returnStmt.exp = parse_Exp();
        return returnStmt;
    }

    private Lval parse_Lval() throws Exception {
        Lval l = new Lval(lexer.nextToken());
        while(lexer.LookAhead()==TokenKind.LBracket){
            lexer.TokenOfKind(TokenKind.LBracket);
            l.exps.add(parse_Exp());
            lexer.TokenOfKind(TokenKind.RBracket);
        }
        return l;
    }

    private AssignStmt parse_AssignStmt(Lval l) throws Exception {
        AssignStmt assignStmt = new AssignStmt(l);
        lexer.TokenOfKind(TokenKind.OpAsg);
        assignStmt.exp = parse_Exp();
        return assignStmt;
    }

    private Exp parse_Exp() {

        return null;
    }
    private Exp parse_Exp(Lval l) {
        return null;
    }

    private Cond parse_Cond() {

    }
}
