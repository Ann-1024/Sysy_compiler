package compiler.parser;

import compiler.ast.*;
import compiler.ast.Interface.*;
import compiler.ast.type.Data_Type;
import compiler.ast.type.Func_Type;
import compiler.lexer.Lexer;
import compiler.lexer.Token;
import compiler.lexer.TokenKind;

import java.util.ArrayList;
import java.util.List;

public class Parser {
    Lexer lexer;
    CompUnit root;
    Parser(String code){
        lexer = new Lexer(code);
        root = new CompUnit();
    }
    public void parse_CompUnit() throws Exception {
        Token t = lexer.nextToken();
        while(t.kind!= TokenKind.EOF){
            if(t.kind == TokenKind.ConstKeyword){
                root.constDefs.addAll(parse_ConstDecl());
            } else if(t.kind == TokenKind.VoidType){
                parse_FuncDef(t,lexer.nextToken());
            } else{
                Token t1 = lexer.nextToken();
                if(lexer.LookAhead()==TokenKind.LParen){
                    root.funcDefs.add(parse_FuncDef(t,t1));
                }else{
                    root.varDefs.addAll(parse_VarDecl(t,t1));
                }
            }
        }
    }
    public List<ConstDef> parse_ConstDecl() throws Exception {
        Token t = lexer.nextToken();
        Data_Type type;
        if(t.kind==TokenKind.IntType){
            type = Data_Type.Int;
        } else if(t.kind==TokenKind.FloatType){
            type = Data_Type.Float;
        } else{
            throw new Exception("type error!");
        }
        List<ConstDef> constDefs = new ArrayList<>();
        constDefs.add(parse_ConstDef(type));
        while(lexer.LookAhead()==TokenKind.Comma){
            constDefs.add(parse_ConstDef(type));
        }
        lexer.TokenOfKind(TokenKind.Semicolon);
        return constDefs;
    }
    public List<VarDef> parse_VarDecl(Token t,Token t1) throws Exception {
        Data_Type type;
        if(t.kind==TokenKind.IntType){
            type = Data_Type.Int;
        } else if(t.kind==TokenKind.FloatType){
            type = Data_Type.Float;
        } else{
            throw new Exception("type error");
        }
        List<VarDef> varDefs = new ArrayList<>();
        varDefs.add(parse_VarDef(type,t1));
        while(lexer.LookAhead()==TokenKind.Comma){
            Token tmp = lexer.nextToken();
            varDefs.add(parse_VarDef(type,tmp));
        }
        lexer.TokenOfKind(TokenKind.Semicolon);
        return varDefs;
    }
    public ConstDef parse_ConstDef(Data_Type type) throws Exception {
        Token t = lexer.nextToken();
        if(lexer.LookAhead()==TokenKind.LBracket){
            ConstArrayDef constDef = new ConstArrayDef(t.line,type,t.name);
            constDef.dimension.add(parse_ConstExp());
            lexer.nextToken();
            while(lexer.LookAhead()!=TokenKind.RBracket){
                constDef.dimension.add(parse_ConstExp());
            }
            lexer.TokenOfKind(TokenKind.RBracket);
            lexer.TokenOfKind(TokenKind.OpAsg);
            lexer.TokenOfKind(TokenKind.LCurly);
            while(lexer.LookAhead()!=TokenKind.RCurly){
                constDef.values.add(parse_ConstExp());
            }
            lexer.TokenOfKind(TokenKind.RCurly);
            return constDef;
        }else{
            ConstValDef constDef = new ConstValDef(t.line,type,t.name);
            lexer.TokenOfKind(TokenKind.OpAsg);
            constDef.exp = parse_ConstExp();
            return constDef;
        }

    }
    public VarDef parse_VarDef(Data_Type type,Token t) throws Exception {
        if(lexer.LookAhead()==TokenKind.LBracket){
            ArrayDef arrayDef = new ArrayDef(t.line,type,t.name);
            arrayDef.dimension.add(parse_ConstExp());
            lexer.nextToken();
            while(lexer.LookAhead()!=TokenKind.RBracket){
                arrayDef.dimension.add(parse_ConstExp());
            }
            lexer.TokenOfKind(TokenKind.RBracket);
            if(lexer.LookAhead()==TokenKind.OpAsg){
                lexer.nextToken();
                lexer.TokenOfKind(TokenKind.LCurly);
                while(lexer.LookAhead()!=TokenKind.RCurly){
                    arrayDef.values.add(parse_Exp());
                }
                lexer.TokenOfKind(TokenKind.RCurly);
            }
            return arrayDef;
        }else{
            ValDef valDef = new ValDef(t.line,type,t.name);
            if(lexer.LookAhead()==TokenKind.OpAsg){
                valDef.exp = parse_Exp();
            }
            return valDef;
        }
    }
    public FuncDef parse_FuncDef(Token t,Token t1) throws Exception {
        Func_Type type;
        if(t.kind==TokenKind.VoidType){
            type = Func_Type.Void;
        } else if(t.kind==TokenKind.IntType){
            type = Func_Type.Int;
        } else if(t.kind==TokenKind.FloatType){
            type = Func_Type.Float;
        } else{
            throw new Exception("type error!");
        }
        FuncDef funcDef = new FuncDef(t.line,t1.name,type);
        lexer.TokenOfKind(TokenKind.LParen);
        while(lexer.LookAhead()==TokenKind.Comma){
            funcDef.params.add(parse_Param());
        }
        lexer.TokenOfKind(TokenKind.RParen);
        lexer.TokenOfKind(TokenKind.LCurly);
        funcDef.items = parse_Block();
        lexer.TokenOfKind(TokenKind.RCurly);
        Token tmp = lexer.nextToken();
        funcDef.lastLine = tmp.line;
        return funcDef;
    }
    public ConstExp parse_ConstExp(){
        return null;
    }
    public Exp parse_Exp() throws Exception {
        return (Exp)parse_AddExp();
    }
    public AddExp parse_AddExp() throws Exception {
        AddExp exp = new AddExp();
        exp.mulExp = parse_MulExp();
        exp.line = exp.mulExp.line;
        if(lexer.LookAhead()==TokenKind.OpAdd){
            exp.op = "+";
            Token t = lexer.nextToken();
            exp.line = t.line;
            exp.addExp = parse_AddExp();
        } else if(lexer.LookAhead()==TokenKind.OpSub){
            exp.op = "-";
            Token t = lexer.nextToken();
            exp.line = t.line;
            exp.addExp = parse_AddExp();
        }
        return exp;
    }
    public MulExp parse_MulExp() throws Exception {
        MulExp exp = new MulExp();
        exp.unaryExp = parse_UnaryExp();
        if(lexer.LookAhead()==TokenKind.OpMul){
            exp.op = "*";
            Token t = lexer.nextToken();
            exp.line = t.line;
            exp.mulExp = parse_MulExp();
        } else if(lexer.LookAhead()==TokenKind.OpDiv){
            exp.op = "/";
            Token t = lexer.nextToken();
            exp.line = t.line;
            exp.mulExp = parse_MulExp();
        } else if(lexer.LookAhead()==TokenKind.OpMod){
            exp.op = "%";
            Token t = lexer.nextToken();
            exp.line = t.line;
            exp.mulExp = parse_MulExp();
        }
        return exp;
    }
    public UnaryExp parse_UnaryExp() throws Exception {
        //单目运算符
        Token t;
        if(lexer.LookAhead()==TokenKind.OpAdd){
            OpUnaryExp opUnaryExp =  new OpUnaryExp();
            t = lexer.nextToken();
            opUnaryExp.line = t.line;
            opUnaryExp.op = "+";
            opUnaryExp.unaryExp = parse_UnaryExp();
            return opUnaryExp;
        } else if(lexer.LookAhead()==TokenKind.OpSub){
            OpUnaryExp opUnaryExp =  new OpUnaryExp();
            t = lexer.nextToken();
            opUnaryExp.line = t.line;
            opUnaryExp.op = "-";
            opUnaryExp.unaryExp = parse_UnaryExp();
            return opUnaryExp;
        } else if(lexer.LookAhead()==TokenKind.OpNot){
            OpUnaryExp opUnaryExp =  new OpUnaryExp();
            t = lexer.nextToken();
            opUnaryExp.line = t.line;
            opUnaryExp.op = "!";
            opUnaryExp.unaryExp = parse_UnaryExp();
            return opUnaryExp;
        }
        //函数调用
        if(lexer.LookAhead()==TokenKind.Ident){
            t = lexer.nextToken();
            if(lexer.LookAhead()==TokenKind.LParen){
                FuncCallExp funcCallExp = parse_FuncCallExp(t);
                lexer.TokenOfKind(TokenKind.RParen);
                return funcCallExp;
            }
            PrimaryExp primaryExp = parse_PrimaryExp(t);
        }
        return null;
    }
    public FuncCallExp parse_FuncCallExp(Token t) throws Exception {
        FuncCallExp funcCallExp = new FuncCallExp(t.line,t.name);
        lexer.TokenOfKind(TokenKind.LParen);
        while(lexer.LookAhead()==TokenKind.Comma){
            lexer.nextToken();
            funcCallExp.params.add(parse_FuncParam());
        }
        lexer.TokenOfKind(TokenKind.RParen);
        return funcCallExp;
    }
    public PrimaryExp parse_PrimaryExp(Token t) throws Exception {
        PrimaryExp primaryExp;
        if(t.kind==TokenKind.LBracket){
            ExpImp exp = new ExpImp(t.line,parse_Exp());
            return exp;
        } else if(t.kind==TokenKind.Ident){
            LocalVar localVar = new LocalVar(t.line,t.name);
            while(lexer.LookAhead()==TokenKind.LBracket){
                lexer.nextToken();
                localVar.exps.add(parse_Exp());
                lexer.TokenOfKind(TokenKind.RBracket);
            }
            return localVar;
        }
        NumExp numExp = parse_Number(t);
        return numExp;
    }
    public NumExp parse_Number(Token t){
        if (t.kind==TokenKind.FloatConst){
            return new NumExp(t.line,Data_Type.Float,t.name);
        }
        if(t.kind==TokenKind.DecIntConst||t.kind==TokenKind.HexIntConst||t.kind==TokenKind.OctalIntConst){
            return new NumExp(t.line,Data_Type.Int,t.name);
        }
        return null;
    }
    public List<BlockItem> parse_Block() throws Exception {
        List<BlockItem> items = new ArrayList<>();
        while(lexer.LookAhead()!=TokenKind.RCurly){
            if(lexer.LookAhead()==TokenKind.ConstKeyword){
                items.addAll(parse_ConstDecl());
            } else if(lexer.LookAhead()==TokenKind.IntType||lexer.LookAhead()==TokenKind.FloatType){
                Token t = lexer.nextToken();
                Token t1 = lexer.nextToken();
                items.addAll(parse_VarDecl(t,t1));
            }
            items.add(parse_Stmt());
        }
        return null;
    }
    public Stmt parse_Stmt() throws Exception {
        return null;
    }
    public Param parse_Param(){
        return null;
    }
    public FuncParam parse_FuncParam(){
        return null;
    }
    /*public Cond parse_Cond() throws Exception {
        return null;
    }*/
}
