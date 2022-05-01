package compiler.ast;

import compiler.ast.Interface.Stmt;

public class ContinueStmt extends Node implements Stmt {
    public ContinueStmt(){
        nodeType = NodeType.ContinueStmt;
    }
}
