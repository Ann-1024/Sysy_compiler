package compiler.ast;

import compiler.ast.Interface.Exp;
import compiler.ast.Interface.UnaryExp;
import compiler.lexer.TokenKind;

public class MulExp1 extends Node implements Exp {
    public TokenKind kind;
    public UnaryExp unaryExp;
    public MulExp1() {
        nodeType = NodeType.MulExp1;
    }
}
