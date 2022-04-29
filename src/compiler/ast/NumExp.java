package compiler.ast;

import compiler.ast.Interface.PrimaryExp;
import compiler.ast.type.Data_Type;

public class NumExp implements PrimaryExp {
    int line;
    Data_Type type;
    String name;

    public NumExp(int line, Data_Type type, String name) {
        this.line = line;
        this.type = type;
        this.name = name;
    }
}
