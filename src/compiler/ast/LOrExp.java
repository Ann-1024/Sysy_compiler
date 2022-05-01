package compiler.ast;

import compiler.ast.Node;

import java.util.ArrayList;
import java.util.List;

public class LOrExp extends Node {
    public List<LAndExp> lAndExps;
    public LOrExp() {
        nodeType = NodeType.LOrExp;
        lAndExps = new ArrayList<>();
    }
}
