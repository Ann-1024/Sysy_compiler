package compiler.lexer;

public enum TokenKind {
    IntType , // basic type like 'int' | 'float' | 'void'
    FloatType,
    VoidType,
    Ident,     // [a-zA-Z][a-zA-Z0-9]*
    // keywords
    ConstKeyword, // 'const'
    IfKeyword,
    ElseKeyword,
    WhileKeyword,
    BreakKeyword,
    ContinueKeyword,
    ReturnKeyword,
    // punctuation
    Comma,     // ,
    Semicolon, // ;
    LParen,    // (
    RParen,    // )
    LCurly,    // {
    RCurly,    // }
    LBracket,   // [
    RBracket,   // ]
    EOF,  //End mark
    // Operator
    // unary op '+' '-' '!', '!' only appear in Cond
    // Operator, // * / % + - < > <= >= == != && || =
    OpAdd, // +
    OpSub, // -
    OpNot, // !
    OpMul, // *
    OpDiv, // /
    OpMod, // %
    OpLT,
    OpGT,
    OpGE,
    OpLE,
    OpEQ,
    OpNE,
    OpAnd,
    OpOr,
    OpAsg,
    // literal const
    DecIntConst,
    OctalIntConst,
    HexIntConst,
    FloatConst,

    // composite nodes
    /*
    CompUnit,
    Decl,
    ConstDecl,
    ConstDef,
    ConstInitVal,
    VarDecl,
    VarDef,
    InitVal,
    FuncDef,
    FuncType,
    FuncFParams,
    FuncFParam,
    Block,
    BlockItem,
    Statement,
    Expression,
    Condition,
    LeftValue,
    PrimaryExp,
    UnaryExp,
    FuncRParams,
    MulExp,
    AddExp,
    RelationExp,
    EqExp,
    LogicAndExp,
    LogicOrExp,
    ConstExp,
    // end here
    EndMark, // shoud not appear in token stream, purely use as a end mark*/
}
