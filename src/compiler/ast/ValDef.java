package compiler.ast;

import compiler.ast.Interface.ConstDef;
import compiler.ast.Interface.Exp;
import compiler.ast.Interface.VarDef;
import compiler.lexer.Token;

public class ValDef extends Node implements ConstDef, VarDef {
    public String identifier;
    public Exp exp;
    public ValDef(Token t){
        start = t.line;
        identifier = t.name;
    }
}
