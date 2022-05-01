package compiler.ast;

import compiler.ast.Interface.Stmt;

public class WhileStmt extends Node implements Stmt{
    public LOrExp lOrExp;
    public Stmt stmt;

    public WhileStmt() {
        nodeType = NodeType.WhileStmt;
    }
}
