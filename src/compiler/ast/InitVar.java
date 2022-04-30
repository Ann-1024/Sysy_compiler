package compiler.ast;


import compiler.ast.Interface.Init;

import java.util.ArrayList;
import java.util.List;

public class InitVar extends Node implements Init {
    public List<Init> inits;
    public InitVar(){
        nodeType = NodeType.InitVar;
        inits = new ArrayList<Init>();
    }

}
