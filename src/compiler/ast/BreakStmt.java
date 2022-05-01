package compiler.ast;

import compiler.ast.Interface.Stmt;

public class BreakStmt extends Node implements Stmt {
    public BreakStmt() {
        nodeType = NodeType.BreakStmt;
    }
}
