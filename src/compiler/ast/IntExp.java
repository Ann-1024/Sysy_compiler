package compiler.ast;

import compiler.ast.Interface.UnaryExp;
import compiler.lexer.Token;

public class IntExp extends Node implements UnaryExp {
    public int num;
    public IntExp() {
        nodeType = NodeType.IntExp;
    }
}
