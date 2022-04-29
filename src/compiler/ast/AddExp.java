package compiler.ast;

import compiler.ast.Interface.Exp;

public class AddExp implements Exp {
    public int line;
    public MulExp mulExp;
    public String op;
    public AddExp addExp;

}
