package compiler.parser;

import compiler.ast.FloatExp;
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
            lexer.TokenOfKind(TokenKind.LBracket);
            lexer.TokenOfKind(TokenKind.RBracket);
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
                UnaryExp unaryExp = parse_Exp(lval);
                lexer.TokenOfKind(TokenKind.Semicolon);
                return unaryExp;
            default:
                Exp exp = parse_Exp();
                lexer.TokenOfKind(TokenKind.Semicolon);
                return exp;
        }
    }
    // 很多Stmt的结束位置没写
    private IfStmt parse_IfStmt() throws Exception {
        IfStmt ifStmt =  new IfStmt();
        ifStmt.start = lexer.nextToken().line;
        ifStmt.lOrExp = parse_LOrExp();
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
        whileStmt.lOrExp = parse_LOrExp();
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
        returnStmt.end = lexer.TokenOfKind(TokenKind.Semicolon);
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

    private AddExp parse_Exp() throws Exception {
        AddExp addExp = new AddExp();
        addExp.mulExp = parse_MulExp();
        while(lexer.LookAhead()==TokenKind.OpAdd||lexer.LookAhead()==TokenKind.OpSub){
            addExp.addExp1s.add(parse_AddExp1());
        }
        return addExp;
    }

    private MulExp parse_MulExp() throws Exception {
        MulExp mulExp = new MulExp();
        mulExp.unaryExp = parse_UnaryExp();
        while(lexer.LookAhead()==TokenKind.OpMul||lexer.LookAhead()==TokenKind.OpDiv||lexer.LookAhead()==TokenKind.OpMod){
            mulExp.mulExp1s.add(parse_MulExp1());
        }
        return mulExp;
    }


    private AddExp1 parse_AddExp1() throws Exception {
        Token t = lexer.nextToken();
        AddExp1 addExp1 = new AddExp1();
        addExp1.kind = t.kind;
        addExp1.start = t.line;
        addExp1.mulExp = parse_MulExp();
        return addExp1;
    }
    private MulExp1 parse_MulExp1() throws Exception {
        Token t = lexer.nextToken();
        MulExp1 mulExp1 = new MulExp1();
        mulExp1.kind = t.kind;
        mulExp1.start = t.line;
        mulExp1.unaryExp = parse_UnaryExp();
        return mulExp1;
    }

    private UnaryExp parse_UnaryExp() throws Exception {
        switch (lexer.LookAhead()){
            case LParen:
                ParenExp parenExp = new ParenExp();
                parenExp.start = lexer.nextToken().line;
                parenExp.exp = parenExp;
                parenExp.end = lexer.TokenOfKind(TokenKind.RParen);
                return parenExp;
            case Ident:
                Lval lval = parse_Lval();
                return parse_Exp(lval);
            case OpAdd:
                return parse_UnaryExp1();
            case OpSub:
                return parse_UnaryExp1();
            case OpNE:
                return parse_UnaryExp1();
            default:
                return parse_NumberExp();
        }
    }

    private UnaryExp parse_UnaryExp1() throws Exception {
        Token t = lexer.nextToken();
        UnaryExp1 unaryExp1 = new UnaryExp1();
        unaryExp1.start = t.line;
        unaryExp1.kind = t.kind;
        unaryExp1.unaryExp = parse_UnaryExp();
        return unaryExp1;
    }

    private UnaryExp parse_NumberExp() throws Exception {
        Token t = lexer.nextToken();
        IntExp intExp;
        switch (t.kind){
            case DecIntConst:
                intExp = new IntExp();
                intExp.start = t.line;
                intExp.num = Integer.parseUnsignedInt(t.name,10);
                return intExp;
            case HexIntConst:
                intExp = new IntExp();
                intExp.start = t.line;
                intExp.num = Integer.parseInt(t.name.substring(2),16);
                return intExp;
            case OctalIntConst:
                intExp = new IntExp();
                intExp.start = t.line;
                if(t.name.length()==1){
                    intExp.num = 0;
                }
                else{
                    intExp.num = Integer.parseInt(t.name.substring(1),8);
                }
                return intExp;
            case FloatType:
                FloatExp floatExp = new FloatExp();
                floatExp.start = t.line;
                floatExp.num = Float.parseFloat(t.name);
            default:
                throw new Exception("expect a number here!");
        }
    }


    private UnaryExp parse_Exp(Lval l) throws Exception {
        if(lexer.LookAhead()==TokenKind.Semicolon){
            return new LValExp(l);
        }
        if(lexer.LookAhead()==TokenKind.LCurly){
            FuncExp funcExp = new FuncExp();
            funcExp.start = l.start;
            funcExp.identifier = l.identifier;
            lexer.nextToken();
            while(lexer.LookAhead()!=TokenKind.RCurly){
                funcExp.funcRParams.add(new FuncRParam(parse_Exp()));
                lexer.TokenOfKind(TokenKind.Comma);
            }
            funcExp.end = lexer.nextToken().line;
            return funcExp;
        }
        throw new Exception("invalid token after Lval");
    }

    private LOrExp parse_LOrExp() throws Exception {
        LOrExp lOrExp = new LOrExp();
        lOrExp.lAndExps.add(parse_LAndExps());
        while(lexer.LookAhead()==TokenKind.OpOr){
            lexer.nextToken();
            lOrExp.lAndExps.add(parse_LAndExps());
        }
        return lOrExp;
    }

    private LAndExp parse_LAndExps() throws Exception {
        LAndExp lAndExp = new LAndExp();
        lAndExp.eqExps.add(parse_EqExp());
        while(lexer.LookAhead()==TokenKind.OpAnd){
            lexer.nextToken();
            lAndExp.eqExps.add(parse_EqExp());
        }
        return lAndExp;        
    }

    private EqExp parse_EqExp() throws Exception {
        EqExp eqExp = new EqExp();
        eqExp.relExp = parse_relExp();
        Token t;
        while(lexer.LookAhead()==TokenKind.OpEQ||lexer.LookAhead()==TokenKind.OpNE){
            t = lexer.nextToken();
            eqExp.relExp_eqs.add(new RelExp_Eq(t.kind,parse_relExp()));
        }
        return eqExp;
    }

    private RelExp parse_relExp() throws Exception {
        RelExp relExp = new RelExp();
        relExp.addExp = parse_Exp();
        Token t;
        while(lexer.LookAhead()==TokenKind.OpLE||lexer.LookAhead()==TokenKind.OpLT||lexer.LookAhead()==TokenKind.OpGE||lexer.LookAhead()==TokenKind.OpGT){
            t = lexer.nextToken();
            relExp.relExp_adds.add(new RelExp_Add(t.kind,parse_Exp()));
        }
        return relExp;
    }

}
