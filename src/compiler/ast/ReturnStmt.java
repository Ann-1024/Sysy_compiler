package compiler.ast;

import compiler.ast.Interface.Exp;
import compiler.ast.Interface.Stmt;

public class ReturnStmt extends Node implements Stmt {
    public Exp exp;
    public ReturnStmt(){
        nodeType = NodeType.ReturnStmt;
    }
}
