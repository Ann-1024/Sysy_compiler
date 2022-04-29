package compiler.ast;

import compiler.ast.Interface.Exp;
import compiler.ast.Interface.PrimaryExp;

public class ExpImp implements PrimaryExp {
    int line;
    Exp exp;

    public ExpImp(int line, Exp exp) {
        this.line = line;
        this.exp = exp;
    }
}
