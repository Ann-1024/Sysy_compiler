package compiler.ast;

import compiler.ast.Interface.BlockItem;
import compiler.ast.type.Func_Type;
import compiler.lexer.TokenKind;

import java.util.ArrayList;
import java.util.List;

public class FuncDef {
    public int firstLine;
    public int lastLine;
    public String funcName;
    public List<Param> params;
    public List<BlockItem> items;
    public Func_Type type;

    public FuncDef(int firstLine, String funcName, Func_Type type) {
        this.firstLine = firstLine;
        this.funcName = funcName;
        this.type = type;
        this.params = new ArrayList<>();
        this.items = new ArrayList<>();
    }
}
