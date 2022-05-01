package compiler.ast;

import compiler.ast.Interface.Exp;
import compiler.ast.Node;
import compiler.lexer.Token;

import java.util.ArrayList;
import java.util.List;

public class Lval extends Node {
    public String identifier;
    public List<Exp> exps;
    public Lval(Token t){
        nodeType = NodeType.Lval;
        start = t.line;
        identifier = t.name;
        exps = new ArrayList<>();
    }
}
