package compiler.ast;

import compiler.ast.Interface.ConstDef;
import compiler.ast.type.Data_Type;
import compiler.ast.type.Def_Type;

public class ConstValDef implements ConstDef {
    public int line;
    public Def_Type def_type;
    public Data_Type type;
    public String ident;
    public ConstExp exp;

    public ConstValDef(int line, Data_Type type, String ident) {
        this.def_type = Def_Type.Scalar;
        this.line = line;
        this.type = type;
        this.ident = ident;
    }

    @Override
    public String toString() {
        return "ConstValDef{" +
                "line=" + line +
                ", def_type=" + def_type +
                ", type=" + type +
                ", ident='" + ident + '\'' +
                ", exp=" + exp +
                '}';
    }
}
