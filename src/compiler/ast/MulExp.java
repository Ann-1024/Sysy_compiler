package compiler.ast;

import compiler.ast.Interface.Exp;
import compiler.ast.Interface.UnaryExp;

import java.util.ArrayList;
import java.util.List;

public class MulExp extends Node implements Exp {
    public UnaryExp unaryExp;
    public List<MulExp1> mulExp1s;
    public MulExp() {
        mulExp1s = new ArrayList<>();
        nodeType = NodeType.MulExp;
    }
}
