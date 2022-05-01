package compiler.ast;

import compiler.ast.Interface.UnaryExp;


public class FloatExp extends Node implements UnaryExp {
    public float num;
    public FloatExp() {
        nodeType = NodeType.FloatExp;
    }
}
