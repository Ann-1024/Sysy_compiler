package compiler.ast;

import compiler.ast.Interface.ConstDef;
import compiler.ast.Interface.Decl;

import java.util.ArrayList;
import java.util.List;


public class ConstDecl extends Node implements Decl {
    public List<ConstDef> children;
    public ConstDecl(int start) {
        this.start = start;
        nodeType = NodeType.ConstDecl;
        children = new ArrayList<>();
    }
}
