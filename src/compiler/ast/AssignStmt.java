package compiler.ast;

import compiler.ast.Interface.*;

public class AssignStmt extends Node implements Stmt {
    public Lval lval;
    public Exp exp;
    public AssignStmt(Lval lval){
        nodeType = NodeType.AssignStmt;
        this.lval = lval;
        this.start = lval.start;
    }

}
