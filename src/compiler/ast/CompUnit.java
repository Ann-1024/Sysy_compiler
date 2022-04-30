package compiler.ast;

import java.util.ArrayList;
import java.util.List;

public class CompUnit extends Node{
    public List<Node> children;
    public CompUnit() {
        nodeType = NodeType.CompUnit;
        children = new ArrayList<Node>();
    }
}
