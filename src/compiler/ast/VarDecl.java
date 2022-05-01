package compiler.ast;

import compiler.ast.Interface.ConstDef;
import compiler.ast.Interface.Decl;
import compiler.ast.Interface.VarDef;

import java.util.ArrayList;
import java.util.List;

public class VarDecl extends Node implements Decl {
    public List<VarDef> children;
    public VarDecl(int start) {
        this.start = start;
        nodeType = NodeType.VarDecl;
        children = new ArrayList<VarDef>();
    }
}
