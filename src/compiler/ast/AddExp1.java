package compiler.ast;

import compiler.ast.Interface.Exp;
import compiler.lexer.TokenKind;

public class AddExp1 extends Node implements Exp {
    public TokenKind kind;
    public MulExp mulExp;
    public AddExp1() {
        nodeType = NodeType.AddExp1;
    }
}
