package compiler.ast;

import compiler.ast.Interface.Exp;
import compiler.ast.Interface.UnaryExp;
import compiler.lexer.TokenKind;

public class UnaryExp1 extends Node implements UnaryExp {
    public TokenKind kind;
    public UnaryExp unaryExp;
    public UnaryExp1() {
        nodeType = NodeType.UnaryExp1;
    }
}
