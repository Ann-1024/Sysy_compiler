package compiler.ast;

import compiler.ast.Interface.ConstDef;
import compiler.ast.Interface.VarDef;

import java.util.ArrayList;
import java.util.List;

public class CompUnit {
    public List<ConstDef> constDefs;
    public List<VarDef> varDefs;
    public List<FuncDef> funcDefs;
    public CompUnit(){
        constDefs =  new ArrayList<>();
        varDefs = new ArrayList<>();
        funcDefs = new ArrayList<>();
    }
}
