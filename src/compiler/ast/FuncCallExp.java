package compiler.ast;

import compiler.ast.Interface.UnaryExp;

import java.util.ArrayList;
import java.util.List;

public class FuncCallExp implements UnaryExp {
    int line;
    String name;
    public List<FuncParam> params;

    public FuncCallExp(int line, String name) {
        this.line = line;
        this.name = name;
        this.params = new ArrayList<>();
    }
}
