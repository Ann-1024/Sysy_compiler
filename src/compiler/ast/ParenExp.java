package compiler.ast;

import compiler.ast.Interface.Exp;
import compiler.ast.Interface.UnaryExp;

public class ParenExp extends Node implements UnaryExp {
    public Exp exp;
    public ParenExp() {
        nodeType = NodeType.ParenExp;
    }
}
