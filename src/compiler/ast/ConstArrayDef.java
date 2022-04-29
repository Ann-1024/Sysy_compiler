package compiler.ast;

import compiler.ast.Interface.ConstDef;
import compiler.ast.type.Data_Type;
import compiler.ast.type.Def_Type;

import java.util.ArrayList;
import java.util.List;

public class ConstArrayDef implements ConstDef {
    public int line;
    public Data_Type type;
    public String ident;
    public Def_Type def_type;
    public List<ConstExp> dimension;
    public List<ConstExp> values;
    public ConstArrayDef(int line, Data_Type type, String ident){
        this.def_type = Def_Type.Array;
        this.line = line;
        this.ident = ident;
        this.type = type;
        dimension =  new ArrayList<>();
        values = new ArrayList<>();
    }
    @Override
    public String toString() {
        return "ConstDef{" +
                "line=" + line +
                ", ident='" + ident + '\'' +
                ", type=" + type +
                ", dimension=" + dimension +
                ", values=" + values +
                '}';
    }
}
