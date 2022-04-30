package compiler.ast;

import java.util.List;

public class Node {
    public int start;
    public int end;
    public NodeType nodeType;
    public List<Node> children;
    public Object data;
}
