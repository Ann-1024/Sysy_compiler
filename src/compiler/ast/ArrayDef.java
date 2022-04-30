package compiler.ast;

import compiler.ast.Interface.ConstDef;
import compiler.ast.Interface.Exp;
import compiler.ast.Interface.VarDef;
import compiler.lexer.Token;

import java.util.ArrayList;
import java.util.List;

public class ArrayDef extends Def implements ConstDef, VarDef {
    public String identifier;
    public List<Exp> dimensions;
    public InitVar values;
    public ArrayDef(Token t) {
        start = t.line;
        identifier = t.name;
        dimensions = new ArrayList<>();
    }
}
