package compiler.ast;

import compiler.ast.Interface.Exp;

import java.util.ArrayList;
import java.util.List;


public class AddExp extends Node implements Exp {
    public MulExp mulExp;
    public List<AddExp1> addExp1s;
    public AddExp() {
        nodeType = NodeType.AddExp;
        addExp1s = new ArrayList<>();
    }
}
