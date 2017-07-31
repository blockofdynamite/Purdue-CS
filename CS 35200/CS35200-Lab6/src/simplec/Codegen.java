package simplec;

import java.util.*;

import simplec.parse.*;
import simplec.parse.SimpleC;

import static simplec.AST.*;
import static simplec.Reg.*;
import static simplec.Assem.*;

public class Codegen {
    private int MAX_LOCALS = 32;
    private int nlocals = 0;
    private Map<String, Integer> localVarMap =
            new LinkedHashMap<String, Integer>();
    private Map<String, Integer> localVarType =
            new LinkedHashMap<String, Integer>();

    private int MAX_GLOBALS = 100;
    private int nglobals = 0;
    private Map<String, Integer> globalVarMap =
            new LinkedHashMap<String, Integer>();
    private Map<String, Integer> globalVarType =
            new LinkedHashMap<String, Integer>();

    private Map<String, Label> stringMap;
    private Map<Double, Label> doubleMap;
    private Map<Label, String> labelMap;

    private List<String> funcList;

    public static LinkedList<ASM> instrs = new LinkedList<ASM>();

    public static class RegStack {
        public static int top = 0;

        public static Reg[] regStk = { RBX, R10, R13, R14, R15 };
        public static int size = regStk.length;
        public static Reg pop() {
            if (top >= size) {
                // TODO implement spilling
                // Otherwise this will just crash
                // if you need more than 5 registers
            }
            if (top == 0) {
                return null;
            }
            Reg reg = regStk[top - 1];
            top--;
            return reg;
        }

        public static Reg peek() {
            if (top >= size) {
                // TODO implement spilling
                // Otherwise this will just crash
                // if you need more than 5 registers
            }
            Reg reg = regStk[top - 1];
            return reg;
        }

        public static Reg push() {
            if (top >= size) {
                // TODO implement spilling
                // Otherwise this will just crash
                // if you need more than 5 registers
            }
            Reg reg = regStk[top];
            top++;
            return reg;
        }
    }

    public String convertToByteRegister(Reg reg) {
        switch (reg.toString()) {
            case "%rax":
                return "%al";
            case "%rbx":
                return "%bl";
            case "%rcx":
                return "%cl";
            case "rdx":
                return "%dl";
            case "%rsi":
                return "%sil";
            case "%rdi":
                return "%dil";
            case "%rbp":
                return "%bpl";
            case "%rsp":
                return "%spl";
            case "%r8":
                return "%r8b";
            case "%r9":
                return "%r9b";
            case "%r10":
                return "%r10b";
            case "%r11":
                return "%r11b";
            case "%r12":
                return "%r12b";
            case "%r13":
                return "%r13b";
            case "%r14":
                return "%r14b";
            case "%r15":
                return "%r8b";
            default:
                return null;
        }
    }

    // In case you need to get the type of something
    public CType getType(Value v) {
        return Semant.getType(v);
    }

    public CType getType(Expression expr) {
        return Semant.getType(expr);
    }

    // Assume all of our types are 64-bit/8-byte
    // Except for chars, which are 1 byte
    public int sizeof(CType type) {
        if (type == CType.CHAR)
            return 1;
        else
            return 8;
    }

    public void addLocalVar(String id, CType type) {
        localVarMap.put(id, nlocals);
        localVarType.put(id, sizeof(type));
        nlocals++;
    }

    public int lookupLocalVar(String id) {
        if (localVarMap.containsKey(id))
            return localVarMap.get(id);
        return -1;
    }

    public void addGlobalVar(String id, CType type) {
        globalVarMap.put(id, nglobals);
        globalVarType.put(id, sizeof(type));
        nglobals++;
    }

    public int lookupGlobalVar(String id) {
        if (globalVarMap.containsKey(id))
            return globalVarMap.get(id);
        return -1;
    }

    private Codegen() {
        this.stringMap = new LinkedHashMap<String, Label>();
        this.doubleMap = new LinkedHashMap<Double, Label>();
        this.labelMap  = new LinkedHashMap<Label, String>();
        this.funcList  = new ArrayList<String>();
    }

    public static void main(String... args) {
        try {
            // Build AST
            Value.Unit goal = new SimpleC(System.in).goal();

            // Perform typechecking on our AST
            Semant.typeCheck(goal);

            // Print scopes
            //for (Scope s : Scope.allScopes)
            //System.err.println(s);

            // Run tree optimizer 150 times
            // Feel free to change this number to whatever
            for (int i = 0; i < 150; i++) {
                Optimize.optimize(goal);
            }

            // If no errors reported, run Codegen on our AST
            if (Error.nErrors() == 0) {
                Codegen codegen = new Codegen();
                codegen.codeGen(goal);
                System.out.println(codegen);
            } else {
                // Exit and don't generate code if we have errors
                System.exit(-1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Set this to true if you don't want comments in the output
    // It doesn't matter for testing either way
    public static boolean disableComments = false;
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (ASM asm : instrs) {
            String str = asm.toString().trim();
            if (disableComments &&
                    str.length() > 0 &&
                    str.charAt(0) != '#')
                sb.append(asm + "\n");
            else if (!disableComments)
                sb.append(asm + "\n");
        }
        sb.setLength(sb.length() - 1);
        return sb.toString();
    }

    void codeGen(Statement stmt) {
        if (stmt == null) return;
        stmt.accept(new Statement.Visitor<Void>() {
            public Void visit(Statement.CompoundStatement stmt) {
                for (Statement childStmt : stmt.stmtList) {
                    codeGen(childStmt);
                }
                return null;
            }

            public Void visit(Statement.VariableDecls decls) {
                for (Value.Variable var : decls.vars) {
                    codeGen(var);
                }
                return null;
            }

            public Void visit(Statement.AssignStatement stmt) {
                Value.VarAccess var = stmt.var;
                Expression expr = stmt.expression;

                String id = var.id.image;
                // If i is negative, then its a global variable
                // otherwise we have its index in our call stack
                int i = lookupLocalVar(id);

                if (stmt.index == null) { // Assigning to a variable
                    codeGen(expr);
                    if (i >= 0) {
                        instrs.add(COMMENT("assign to local var " + id));
                        instrs.add(INSTR("movq",
                                RegStack.pop(),
                                OFFSET(RSP, 8*(i))));
                    } else {
                        instrs.add(COMMENT("assign to global var " + id));
                        instrs.add(INSTR("movq",
                                RegStack.pop(),
                                id
                        ));
                    }
                } else { // Assigning to an array
                    codeGen(expr);

                    int len = sizeof(getType(var).deref());

                    Reg index;
                    if (stmt.index != null)
                        codeGen(stmt.index);
                        index = RegStack.pop();
                    if (i >= 0) {
                        instrs.add(INSTR("imulq", CONST(len), index));
                        instrs.add(INSTR("addq", OFFSET(RSP, 8*(i)), index));
                    } else {
                        instrs.add(INSTR("imulq", CONST(len), index));
                        instrs.add(INSTR("addq", id, index));
                    }

                    // deref char*/long* and get type of resulting type (char/long)
                    Reg toAssign = RegStack.pop();

                    instrs.add(INSTR("movq", toAssign, "(" + index + ")"));
                }

                return null;
            }

            public Void visit(Statement.ForStatement stmt) {
                instrs.add(COMMENT("START OF FOR LOOP"));

                codeGen(stmt.init);
                instrs.add(LABEL("start_" + stmt.label.name));

                codeGen(stmt.cond);
                instrs.add(COMMENT("for condition"));

                instrs.add(INSTR("cmpq", CONST(0), RegStack.pop()));
                instrs.add(INSTR("je", "end_" + stmt.label.name));

                codeGen(stmt.body);

                codeGen(stmt.update);

                instrs.add(INSTR("jmp", "start_" + stmt.label.name));
                instrs.add(LABEL("end_" + stmt.label.name));

                instrs.add(COMMENT("END OF FOR LOOP"));

                return null;
            }

            public Void visit(Statement.WhileStatement stmt) {
                instrs.add(COMMENT("START OF WHILE LOOP"));

                instrs.add(LABEL("start_" + stmt.label.name));
                codeGen(stmt.cond);

                instrs.add(COMMENT("while condition"));
                instrs.add(INSTR("cmpq", CONST(0), RegStack.pop()));
                instrs.add(INSTR("je", "end_" + stmt.label.name));

                codeGen(stmt.body);
                instrs.add(INSTR("jmp", "start_" + stmt.label.name));
                instrs.add(LABEL("end_" + stmt.label.name));

                instrs.add(COMMENT("END OF WHILE LOOP"));

                return null;
            }

            public Void visit(Statement.DoWhileStatement stmt) {
                instrs.add(COMMENT("START OF DO WHILE LOOP"));

                instrs.add(LABEL("start_" + stmt.label.name));
                codeGen(stmt.body);

                codeGen(stmt.cond);
                instrs.add(COMMENT("while condition"));
                instrs.add(INSTR("cmpq", CONST(0), RegStack.pop()));
                instrs.add(INSTR("je", "end_" + stmt.label.name));

                instrs.add(INSTR("jmp", "start_" + stmt.label.name));

                instrs.add(LABEL("end_" + stmt.label.name));

                instrs.add(COMMENT("END OF DO WHILE LOOP"));
                return null;
            }

            public Void visit(Statement.IfStatement stmt) {
                instrs.add(COMMENT("START OF IF STATEMENT"));

                instrs.add(LABEL("start_" + stmt.label.name));

                codeGen(stmt.cond);

                instrs.add(COMMENT("if condition"));
                instrs.add(INSTR("cmpq", CONST(0), RegStack.pop()));

                if (stmt.elseStmt != null) {
                    instrs.add(INSTR("je", "start_else_" + stmt.label.name));
                } else {
                    instrs.add(INSTR("je", "end_" + stmt.label.name));
                }

                codeGen(stmt.body);

                instrs.add(INSTR("jmp", "end_" + stmt.label.name));

                if (stmt.elseStmt != null) {
                    instrs.add(LABEL("start_else_" + stmt.label.name));
                    codeGen(stmt.elseStmt);
                    instrs.add(LABEL("end_else_" + stmt.label.name));
                }

                instrs.add(LABEL("end_" + stmt.label.name));

                instrs.add(COMMENT("END OF IF STATEMENT"));

                return null;
            }

            public Void visit(Statement.ElseStatement stmt) {
                codeGen(stmt.body);
                return null;
            }

            public Void visit(Statement.CallStatement stmt) {
                codeGen(stmt.callExpr);
                RegStack.top = 0;
                return null;
            }

            public Void visit(Statement.ContinueStatement stmt) {
                // This will get the loop (for/while/etc) that
                // this continue statement is a part of
                Statement loop = stmt.scope.getLoop();

                instrs.add(INSTR("jmp", "start_" + loop.label.name));

                return null;
            }

            public Void visit(Statement.BreakStatement stmt) {
                // This will get the loop (for/while/etc) that
                // this break statement is a part of
                Statement loop = stmt.scope.getLoop();

                instrs.add(INSTR("jmp", "end_" + loop.label.name));

                return null;
            }

            public Void visit(Statement.ReturnStatement stmt) {
                // This will get the function that
                // this return statement is a part of
                // or null if there isn't one
                Value.Function func = stmt.scope.getFunc();

                if (stmt.retVal != null) {
                    codeGen(stmt.retVal);
                    instrs.add(INSTR("movq", RegStack.pop(), RAX));
                }

                instrs.add(INSTR("jmp", func.label.name + "_end"));

                return null;
            }
        });
    }

    public Void codeGen(Value var) {
        if (var == null) return null;
        return var.accept(new Value.Visitor<Void>() {
            public Void visit(Value.Unit v) {
                for (Value fov : v.fovList) {
                    codeGen(fov);
                }

                // Strings
                for (String stringConst : stringMap.keySet()) {
                    Label label = stringMap.get(stringConst);
                    instrs.add(label);
                    instrs.add(STRING(stringConst));
                }

                // COMPILER DIRECTIVES

                // Functions
                if (funcList.size() > 0)
                    instrs.add(DIRECTIVE("text"));
                for (String func : funcList) {
                    instrs.add(DIRECTIVE("globl", func));
                }

                // Doubles
                if (doubleMap.size() > 0)
                    instrs.add(DIRECTIVE("section .data"));
                for (Double d : doubleMap.keySet()) {
                    Label label = doubleMap.get(d);
                    String id = label.name;
                    instrs.add(label);
                    instrs.add(DIRECTIVE("double", d.toString()));
                }

                // Globals
                if (nglobals > 0)
                    instrs.add(DIRECTIVE("bss"));
                for (String id : globalVarMap.keySet()) {
                    int len = globalVarType.get(id);
                    instrs.add(DIRECTIVE("globl", id));
                    instrs.add(LABEL(id));
                    instrs.add(DIRECTIVE("quad", "0"));
                    instrs.add(DIRECTIVE("size", id, len + ""));
                }

                return null;
            }

            public Void visit(Value.Variable v) {
                if (v.scope == Scope.rootScope) {
                    addGlobalVar(v.id.image, v.type.type);
                    instrs.add(COMMENT("adding global var " + v.id.image + " of type " + v.type.type));
                } else {
                    addLocalVar(v.id.image, v.type.type);
                    instrs.add(COMMENT("adding local var " + v.id.image + " of type " + v.type.type));
                    instrs.add(COMMENT("adding local var " + v.id.image + " of type " + v.type.type));
                }
                return null;
            }

            public Void visit(Value.Argument v) {
                // Saves this argument + its type so we can put it in the stack later
                addLocalVar(v.id.image, v.type.type);
                instrs.add(COMMENT("adding argument var " + v.id.image + " of type " + v.type.type));
                return null;
            }

            public Void visit(Value.Type v) {
                return null;
            }

            public Void visit(Value.VarAccess v) {
                return null;
            }

            public Void visit(Value.Function v) {
                // Add this function name to our function list
                // This will be used to print .globl funcName
                // at the end of the file
                funcList.add(v.id.image);

                // Add our function label
                instrs.add(LABEL(v.id.image));

                // Push our virtual stack registers
                instrs.add(INSTR("pushq", RBX));
                instrs.add(INSTR("pushq", R10));
                instrs.add(INSTR("pushq", R13));
                instrs.add(INSTR("pushq", R14));
                instrs.add(INSTR("pushq", R15));
                instrs.add(INSTR("subq", CONST(256), RSP));

                // Save locals/arguments
                instrs.add(COMMENT("save argument"));
                List<Value.Argument> argList = v.argList;

                // Generate code for our arguments
                for (int i = 0; i < argList.size(); i++) {
                    codeGen(v.argList.get(i));
                }

                // Move each argument into a local place on the stack
                for (int i = argList.size() - 1; i >= 0; i--) {
                    instrs.add(INSTR("movq",
                            regArgs[i],
                            OFFSET(RSP, 8*(lookupLocalVar(v.argList.get(i).id.image)))));
                }

                // Generate code for anything in our function compound stmt
                codeGen(v.cStmt);

                instrs.add(LABEL(v.label.name + "_end"));

                // Restore the stack, then the registers
                instrs.add(INSTR("addq", CONST(256), RSP));
                instrs.add(COMMENT("restore registers"));
                instrs.add(INSTR("popq", R15));
                instrs.add(INSTR("popq", R14));
                instrs.add(INSTR("popq", R13));
                instrs.add(INSTR("popq", R10));
                instrs.add(INSTR("popq", RBX));
                instrs.add(INSTR("retq"));

                return null;
            }

            public Void visit(Value.VariableList v) {
                for (Value.Variable var : v.vars) {
                    codeGen(var);
                }
                return null;
            }
        });

    }

    public Void codeGen(Expression expr) {
        return expr.accept(new Expression.Visitor<Void>() {
            public Void visit(Expression.Or expr) {
                codeGen(expr.left);
                codeGen(expr.right);
                instrs.add(COMMENT(expr.id.image));

                Reg reg = RegStack.pop();
                Reg reg2 = RegStack.pop();

                instrs.add(INSTR("orq", reg, reg2));

                RegStack.push();

                return null;
            }

            public Void visit(Expression.And expr) {
                codeGen(expr.left);
                codeGen(expr.right);
                instrs.add(COMMENT(expr.id.image));

                Reg reg = RegStack.pop();
                Reg reg2 = RegStack.pop();

                instrs.add(INSTR("andq", reg, reg2));

                RegStack.push();

                return null;
            }

            public Void visit(Expression.Eq expr) {
                codeGen(expr.left);
                codeGen(expr.right);
                instrs.add(COMMENT(expr.id.image));

                Reg reg = RegStack.pop();
                Reg reg2 = RegStack.pop();

                instrs.add(INSTR("cmp", reg, reg2));
                instrs.add(INSTR("movq", "$0", reg2));

                if (expr.id.image.equals("==")) {
                    instrs.add(INSTR("sete", convertToByteRegister(reg2)));
                } else if (expr.id.image.equals("!=")) {
                    instrs.add(INSTR("setne", convertToByteRegister(reg2)));
                }

                RegStack.push();

                return null;
            }

            public Void visit(Expression.Rel expr) {
                codeGen(expr.left);
                codeGen(expr.right);
                instrs.add(COMMENT(expr.id.image));

                Reg reg = RegStack.pop();
                Reg reg2 = RegStack.pop();
                instrs.add(INSTR("cmp", reg, reg2));
                instrs.add(INSTR("movq", "$0", reg2));

                if (expr.id.image.equals(">")) {
                    instrs.add(INSTR("setg", convertToByteRegister(reg2)));
                } else if (expr.id.image.equals("<")) {
                    instrs.add(INSTR("setl", convertToByteRegister(reg2)));
                } else if (expr.id.image.equals(">=")) {
                    instrs.add(INSTR("setge", convertToByteRegister(reg2)));
                } else if (expr.id.image.equals("<=")) {
                    instrs.add(INSTR("setle", convertToByteRegister(reg2)));
                }

                RegStack.push();

                return null;
            }

            public Void visit(Expression.Add expr) {
                codeGen(expr.left);
                codeGen(expr.right);
                instrs.add(COMMENT(expr.id.image));

                Reg reg = RegStack.pop();
                Reg reg2 = RegStack.pop();

                if (expr.token.image.equals("+")) {
                    instrs.add(INSTR("addq", reg, reg2));
                } else if (expr.token.image.equals("-")) {
                    instrs.add(INSTR("subq", reg, reg2));
                }

                RegStack.push();

                return null;
            }

            public Void visit(Expression.Mul expr) {
                codeGen(expr.left);
                codeGen(expr.right);
                instrs.add(COMMENT(expr.id.image));

                Reg reg = RegStack.pop();
                Reg reg2 = RegStack.pop();

                if (expr.token.image.equals("*")) {
                    instrs.add(INSTR("imulq", reg, reg2));
                } else if (expr.token.image.equals("/")) {
                    instrs.add(INSTR("movq", CONST(0), RDX));
                    instrs.add(INSTR("movq", reg2, RAX));
                    instrs.add(INSTR("idivq", reg));
                    instrs.add(INSTR("movq", RAX, reg2));
                }

                RegStack.push();

                return null;
            }

            public Void visit(Expression.Ref expr) {
                codeGen(expr.expr);
                String id = expr.expr.id.image;
                int i = lookupLocalVar(id);
                instrs.add(COMMENT("push &" + id));
                if (i >= 0) { // Local
                    instrs.add(INSTR("leaq",
                            OFFSET(RSP, 8*(i)),
                            RegStack.push()));
                } else { // Global
                    instrs.add(INSTR("leaq",
                            id,
                            RegStack.push()));
                }
                return null;
            }

            public Void visit(Expression.Deref expr) {
                codeGen(expr.expr);

                String id = expr.expr.id.image;
                int i = lookupLocalVar(id);

                Reg reg = RegStack.pop();

                instrs.add(INSTR("movq", "(" + reg + ")", RegStack.push()));

                return null;
            }

            public Void visit(Expression.Negative expr) {
                codeGen(expr.expr);
                instrs.add(INSTR("negq", RegStack.peek()));
                return null;
            }

            public Void visit(Expression.Positive expr) {
                codeGen(expr.expr);
                return null;
            }

            public Void visit(Expression.Char expr) {
                instrs.add(COMMENT("push char " + expr.ch + " top=" + RegStack.top));
                instrs.add(INSTR("movq", CONST(expr.ch), RegStack.push()));
                return null;
            }

            public Void visit(Expression.Text expr) {
                Label label;
                if (!stringMap.containsKey(expr.text)) {
                    label = new Label();
                    labelMap.put(label, expr.text);
                    stringMap.put(expr.text, label);
                } else {
                    label = stringMap.get(expr.text);
                }

                // Pushing our string label
                instrs.add(INSTR("movq", CONST(label), RegStack.push()));

                return null;
            }

            public Void visit(Expression.Int expr)   {
                instrs.add(COMMENT("push num " + expr.value + " top=" + RegStack.top));
                instrs.add(INSTR("movq", CONST(expr.value), RegStack.push()));
                return null;
            }

            public Void visit(Expression.Double expr) {
                Double value = expr.value;
                Label label = new Label('d');
                doubleMap.put(value, label);
                // TODO BONUS - implement doubles

                instrs.add(INSTR("movq", CONST((int)expr.value), RegStack.push()));

                return null;
            }

            public Void visit(Expression.ID id) {
                int i = lookupLocalVar(id.id.image);
                if (i >= 0) {
                    instrs.add(INSTR("movq", OFFSET(RSP, 8*(i)), RegStack.push()));
                } else {
                    instrs.add(COMMENT("push global var " + id.id.image));
                    instrs.add(INSTR("movq", id.id.image, RegStack.push()));
                }
                return null;
            }

            public Void visit(Expression.Call expr) {
                List<Expression> args = expr.args;
                // Generate code for our arguments
                for (Expression arg : args) {
                    codeGen(arg);
                }

                // Pop values into the argument registers
                for (int i = args.size() - 1; i >= 0; i--) {
                    instrs.add(INSTR("movq", RegStack.pop(), regArgs[i]));
                }

                // Move the # of var args into RAX
                // TODO BONUS - change this so it doesn't just hardcode 0
                if (expr.id.image.equals("printf")) {
                    instrs.add(INSTR("movq", CONST(0), RAX));
                }

                instrs.add(INSTR("call", expr.id));

                // Move the result of our function call into a stack register
                instrs.add(INSTR("movq", RAX, RegStack.push()));

                return null;
            }

            public Void visit(Expression.Array expr) {
                codeGen(expr.index);
                String id = expr.id.image;
                int i = lookupLocalVar(id);
                int len = sizeof(getType(expr));

                Reg index = RegStack.pop();

                if (i >= 0) {
                    instrs.add(INSTR("imulq", CONST(len), index));
                    instrs.add(INSTR("addq", OFFSET(RSP, 8*(i)), index));
                } else {
                    instrs.add(INSTR("imulq", CONST(len), index));
                    instrs.add(INSTR("addq", id, index));
                }

                instrs.add(INSTR("movq", "(" + index +")", RegStack.push()));
                return null;
            }
        });
    }
}

