package compiler.ast;

import compiler.ast.Interface.Exp;
import compiler.lexer.Token;

import java.util.List;

public class FuncFParam extends Node{
    public String identifier;
    public List<Exp> exps;
    public FuncFParam(){
        nodeType = NodeType.FuncFParam;
    }
}
