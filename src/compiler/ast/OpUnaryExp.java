package compiler.ast;

import compiler.ast.Interface.UnaryExp;

public class OpUnaryExp implements UnaryExp {
    public int line;
    public String op;
    public UnaryExp unaryExp;
}
