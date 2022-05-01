package compiler.ast;

import compiler.ast.Interface.Exp;

public class FuncRParam extends Node {
    public Exp exp;

    public FuncRParam(Exp exp) {
        nodeType = NodeType.FuncRParam;
        this.exp = exp;
    }
}
