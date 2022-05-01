package compiler.ast;

import compiler.ast.Interface.Exp;
import compiler.ast.Interface.UnaryExp;

public class LValExp extends Node implements UnaryExp {
    Lval lval;
    public LValExp(Lval lval) {
        nodeType = NodeType.LvarExp;
        this.lval = lval;
    }
}
