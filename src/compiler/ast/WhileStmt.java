package compiler.ast;

import compiler.ast.Cond;
import compiler.ast.Interface.Stmt;

public class WhileStmt extends Node implements Stmt{
    public Cond cond;
    public Stmt stmt;

    public WhileStmt() {
        nodeType = NodeType.WhileStmt;
    }
}
