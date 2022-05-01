package compiler.ast;

import compiler.ast.Interface.Exp;
import compiler.ast.Interface.UnaryExp;

import java.util.List;

public class FuncExp extends Node implements UnaryExp {
    public String identifier;
    public List<FuncRParam> funcRParams;
    public FuncExp() {
        nodeType = NodeType.FuncExp;
    }
}
