package compiler.ast;

import compiler.ast.Interface.Exp;
import compiler.ast.Interface.PrimaryExp;

import java.util.ArrayList;
import java.util.List;

public class LocalVar implements PrimaryExp {
    int line;
    String name;
    public List<Exp> exps;

    public LocalVar(int line, String name) {
        this.line = line;
        this.name = name;
        this.exps = new ArrayList<>();
    }
}
