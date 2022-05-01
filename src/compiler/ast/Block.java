package compiler.ast;

import compiler.ast.Interface.BlockItem;

import java.util.ArrayList;
import java.util.List;

public class Block extends Node{
    public List<BlockItem> blockItems;

    public Block() {
        nodeType = NodeType.Block;
        blockItems = new ArrayList<>();
    }
}
