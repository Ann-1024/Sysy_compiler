package compiler.ast;

import compiler.lexer.Token;

import java.util.ArrayList;
import java.util.List;

public class FuncDef extends Node{
    public FuncType funcType;
    public String identifier;
    public List<FuncFParam> funcFParams;
    public Block block;
    public FuncDef(Token t,Token t1) throws Exception {
        start = t.line;
        identifier = t1.name;
        funcFParams =  new ArrayList<>();
        switch (t.kind){
            case VoidType:
                funcType = FuncType.Void;
                break;
            case IntType:
                funcType = FuncType.Int;
                break;
            case FloatType:
                funcType = FuncType.Float;
                break;
            default:
                throw new Exception("invalid function type");
        }
    }
}
