package compiler.ast;

import compiler.ast.Interface.Exp;
import compiler.ast.Interface.VarDef;
import compiler.ast.type.Data_Type;
import compiler.ast.type.Def_Type;

import java.util.ArrayList;
import java.util.List;

public class ArrayDef implements VarDef {
    public int line;
    public Data_Type type;
    public String ident;
    public Def_Type def_type;
    public List<ConstExp> dimension;
    public List<Exp> values;
    public ArrayDef(int line, Data_Type type, String ident){
        this.def_type = Def_Type.Array;
        this.line = line;
        this.ident = ident;
        this.type = type;
        dimension =  new ArrayList<>();
        values = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "ArrayDef{" +
                "line=" + line +
                ", type=" + type +
                ", ident='" + ident + '\'' +
                ", def_type=" + def_type +
                ", dimension=" + dimension +
                ", values=" + values +
                '}';
    }
}
