package compiler.ast;

import java.security.PublicKey;
import java.util.List;

public class Node {
    static int cnt=0;
    public int id;
    public int start;
    public int end;
    public NodeType nodeType;

    public Node() {
        this.id = cnt;
        cnt += 1;
    }
}
