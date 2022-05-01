package compiler.ast;

import compiler.ast.Interface.Stmt;

public class IfStmt extends Node implements Stmt {
    public Cond cond;
    public Stmt stmt1;
    public Stmt stmt2;

    public IfStmt() {
        nodeType = NodeType.IfStmt;
    }
}
