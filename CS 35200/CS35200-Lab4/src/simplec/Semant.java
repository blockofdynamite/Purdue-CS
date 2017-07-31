package simplec;

import java.math.BigInteger;
import java.util.*;

import simplec.parse.*;
import static simplec.AST.*;

public class Semant {

    private static void usage() {
        throw new java.lang.Error("Usage: java simplec.Semant <source>.c");
    }

    private Semant() { }

    public static void main(String... args) {
        try {
            Value.Unit goal = new SimpleC(System.in).goal();
            //Uncomment this if you want the AST to print
            //new Print(goal);
            Semant semant = new Semant();
            semant.typeCheck(goal);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void typeCheck(Value v) {
        if (v == null) return;
        v.accept(new Value.Visitor<Void>() {
            public Void visit(Value.Unit v) {
                for (Value fov : v.fovList) {
                    typeCheck(fov);
                }
                return null;
            }

            public Void visit(Value.Variable v) {
                typeCheck(v.type);
                return null;
            }

            public Void visit(Value.Argument v) {
                typeCheck(v.type);
                return null;
            }

            public Void visit(Value.Type v) {
                return null;
            }

            public Void visit(Value.ScalarAccess v) {
                return null;
            }

            public Void visit(Value.Function v) {
                typeCheck(v.varType);
                for (Value arg : v.argList) {
                    typeCheck(arg);
                }
                typeCheck(v.cStmt);
                return null;
            }

            public Void visit(Value.VariableList v) {
                for (Value value : v.vars) {
                    typeCheck(value);
                }
                return null;
            }
        });
    }

    void typeCheck(Statement stmt) {
        if (stmt == null) return;
        stmt.accept(new Statement.Visitor<Void>() {
            public Void visit(Statement.CompoundStatement stmt) {
                // SimpleC forbids mixed declarations and code (just like C90)
                // So we need to set a flag once we've seen code, and make sure
                // no variable declarations follow
                boolean seenNonDecl = false;
                for (Statement s : stmt.stmtList) {
                    if (s instanceof Statement.VariableDecls) {
                        if (seenNonDecl) {
                            Error.MixedDeclarations(stmt.id);
                        }
                    } else {
                        seenNonDecl = true;
                    }
                    typeCheck(s);
                }
                return null;
            }

            public Void visit(Statement.VariableDecls decls) {
                for (Value.Variable var : decls.vars) {
                    typeCheck(var);
                }
                return null;
            }

            public Void visit(Statement.AssignStatement stmt) {
                CType lhs = getType(stmt.scope.pull(stmt.var.token.toString()));
                CType rhs = getType(stmt.expression);

                if (rhs == null && stmt.expression != null) {
                    rhs = getType(stmt.scope.pull(stmt.expression.token.toString()));
                }

                if (stmt.scope.pull(stmt.var.token.toString()) == null) {
                    // Note that until you add scope, this might
                    // say every variable is undeclared
                    Error.UndeclaredAssign(stmt.id, stmt.var);
                    return null;
                }

                if (stmt.index != null) {
                    if (getType(stmt.index) != CType.LONG || getType(stmt.index) != CType.CHAR) {
                        Error.NonIntSubscript(stmt.index.token);
                        return null;
                    }
                }

                if (!CType.canAssign(stmt.var.token, lhs, rhs)) {
                    return null;
                }

                return null;
            }

            public Void visit(Statement.ForStatement stmt) {
                stmt.body.scope.inLoop = true;
                typeCheck(stmt.init);
                CType condType = getType(stmt.cond);
                typeCheck(stmt.update);
                typeCheck(stmt.body);
                return null;
            }

            public Void visit(Statement.WhileStatement stmt) {
                CType condType = getType(stmt.cond);
                typeCheck(stmt.body);
                return null;
            }

            public Void visit(Statement.DoWhileStatement stmt) {
                CType condType = getType(stmt.cond);
                typeCheck(stmt.body);
                return null;
            }

            public Void visit(Statement.IfStatement stmt) {
                CType condType = getType(stmt.cond);
                typeCheck(stmt.body);
                if (stmt.elseStmt != null)
                    typeCheck(stmt.body);
                return null;
            }

            public Void visit(Statement.ElseStatement stmt) {
                typeCheck(stmt.body);
                return null;
            }

            public Void visit(Statement.CallStatement stmt) {
                CType exprType = getType(stmt.callExpr);
                return null;
            }

            public boolean inLoop(Scope scope)  {
                for (Scope newScope = scope; newScope != null; newScope = newScope.parent) {
                    if (newScope.inLoop) {
                        return true;
                    }
                }
                return false;
            }

            public Void visit(Statement.ContinueStatement stmt) {
                if (!inLoop(stmt.scope)) {
                    Error.ContinueNotInLoop(stmt.id);
                }
                return null;
            }

            public Void visit(Statement.BreakStatement stmt) {
                if (!inLoop(stmt.scope)) {
                    Error.BreakNotInLoop(stmt.id);
                }
                return null;
            }

            public Void visit(Statement.ReturnStatement stmt) {
                if (stmt.retVal == null) {
                    return null;
                }
                CType type = getType(stmt.retVal);
                return null;
            }
        });

    }

    public CType getType(Value var) {
        if (var == null) return null;
        return var.accept(new Value.Visitor<CType>() {
            public CType visit(Value.Unit v) {
                return null;
            }

            public CType visit(Value.Variable v) {
                return getType(v.type);
            }

            public CType visit(Value.Argument v) {
                return getType(v.type);
            }

            public CType visit(Value.Type v) {
                return v.type;
            }

            public CType visit(Value.ScalarAccess v) {
                return null;
            }

            public CType visit(Value.Function v) {
                return getType(v.varType);
            }

            public CType visit(Value.VariableList v) {
                if (v.vars.size() > 0)
                    return getType(v.vars.get(0));
                return null;
            }
        });

    }

    public CType getType(Expression expr) {
        return expr.accept(new Expression.Visitor<CType>() {
            public CType visit(Expression.Or expr) {
                CType lhs = getType(expr.left);
                CType rhs = getType(expr.right);

                if (lhs == rhs)
                    return lhs;
                return null;
            }

            public CType visit(Expression.And expr) {
                CType lhs = getType(expr.left);
                CType rhs = getType(expr.right);

                if (lhs == rhs)
                    return lhs;
                return null;
            }

            public CType visit(Expression.Eq expr) {
                CType lhs = getType(expr.left);
                CType rhs = getType(expr.right);

                if (lhs == rhs)
                    return lhs;
                return null;
            }

            public CType visit(Expression.Rel expr) {
                CType lhs = getType(expr.left);
                CType rhs = getType(expr.right);

                if (lhs == CType.CHARSTARSTAR || lhs == CType.CHARSTAR || lhs == CType.DOUBLESTAR || lhs == CType.LONGSTAR) {
                    Error.WrongTypeBinary(expr.token, expr.id.toString(), lhs, rhs);
                    return CType.LONG;
                }
                if (rhs == CType.CHARSTARSTAR || rhs == CType.CHARSTAR || rhs == CType.DOUBLESTAR || rhs == CType.LONGSTAR) {
                    Error.AssignPointerToInt(expr.token);
                    return CType.LONG;
                }

                if (lhs == rhs)
                    return lhs;
                return null;
            }

            public CType visit(Expression.Add expr) {
                CType lhs = getType(expr.left);
                CType rhs = getType(expr.right);

                if (lhs == null) {
                    lhs = getType(expr.scope.pull(expr.left.id.toString()));
                }
                if (rhs == null) {
                    rhs = getType(expr.scope.pull(expr.right.id.toString()));
                }

                if (lhs == null) {
                    Error.UndeclaredVariable(expr.left.token);
                    return CType.LONG;
                }
                if (rhs == null) {
                    Error.UndeclaredVariable(expr.right.token);
                    return CType.LONG;
                }

                if ((lhs == CType.CHARSTAR || lhs == CType.DOUBLESTAR || lhs == CType.LONGSTAR || lhs == CType.CHARSTARSTAR)
                        && (rhs == CType.CHAR || rhs == CType.LONG)) {
                    return lhs;
                }

                if (lhs == rhs)
                    return lhs;

                return null;
            }

            public CType visit(Expression.Mul expr) {
                CType lhs = getType(expr.left);
                CType rhs = getType(expr.right);

                if (expr.token.toString().equals("/")) {
                    if (expr.right.id.toString().equals("0")) {
                        Error.DivideByZero(expr.token);
                    }
                }

                if (lhs == null) {
                    lhs = getType(expr.scope.pull(expr.left.id.toString()));
                }
                if (rhs == null) {
                    rhs = getType(expr.scope.pull(expr.right.id.toString()));
                }

                if (lhs == null) {
                    Error.UndeclaredVariable(expr.left.token);
                    return CType.LONG;
                }
                if (rhs == null) {
                    Error.UndeclaredVariable(expr.right.token);
                    return CType.LONG;
                }

                if (lhs == CType.CHARSTARSTAR || lhs == CType.CHARSTAR || lhs == CType.DOUBLESTAR || lhs == CType.LONGSTAR) {
                    Error.AssignPointerToInt(expr.token);
                    return CType.LONG;
                }
                if (rhs == CType.CHARSTARSTAR || rhs == CType.CHARSTAR || rhs == CType.DOUBLESTAR || rhs == CType.LONGSTAR) {
                    Error.AssignPointerToInt(expr.token);
                    return CType.LONG;
                }

                if (lhs == rhs)
                    return lhs;

                return null;
            }

            public CType visit(Expression.Ref expr) {
                CType inside = getType(expr.scope.pull(expr.expr.id.toString()));
                if (inside != CType.CHARSTARSTAR && inside != CType.LONGSTAR && inside != CType.DOUBLESTAR) {
                    if (inside == CType.CHAR) {
                        return CType.CHARSTAR;
                    } else if (inside == CType.DOUBLE) {
                        return  CType.DOUBLESTAR;
                    } else if (inside == CType.LONG) {
                        return CType.LONGSTAR;
                    } else {
                        return null;
                    }
                }
                return null;
            }

            public CType visit(Expression.Deref expr) {
                CType inside = getType(expr.scope.pull(expr.expr.id.toString()));
                if (inside == CType.CHARSTAR || inside == CType.CHARSTARSTAR || inside == CType.DOUBLESTAR || inside == CType.LONGSTAR) {
                    return inside;
                }
                Error.WrongTypeUnary(expr.expr.token, expr.id.toString());
                return CType.LONG;
            }

            public CType visit(Expression.Negative expr) {
                CType inside = getType(expr.scope.pull(expr.expr.id.toString()));
                if (inside == null) {
                    inside = getType(expr.expr);
                }
                if (inside == CType.CHAR || inside == CType.DOUBLE || inside == CType.LONG) {
                    return inside;
                }
                Error.WrongTypeUnary(expr.id, expr.token.toString());
                System.err.println(inside);
                return inside;
            }

            public CType visit(Expression.Positive expr) {
                CType inside = getType(expr.scope.pull(expr.expr.id.toString()));
                if (inside == null) {
                    inside = getType(expr.expr);
                }
                if (inside == CType.CHAR || inside == CType.DOUBLE || inside == CType.LONG) {
                    return inside;
                }
                Error.WrongTypeUnary(expr.id, expr.token.toString());
                return inside;
            }

            public CType visit(Expression.Char expr) { return CType.CHAR; }
            public CType visit(Expression.Text expr) { return CType.CHARSTAR; }
            public CType visit(Expression.Int expr)  { return CType.LONG; }
            public CType visit(Expression.Double expr) { return CType.DOUBLE; }

            public CType visit(Expression.ID id) {
                return null;
            }

            public CType visit(Expression.Call expr) {
                AST.Value val = expr.scope.pull(expr.id.toString());
                if (val instanceof Value.Function) {
                    Value.Function call = (Value.Function) val;
                    if (expr.args.size() > call.argList.size())
                        Error.TooManyArguments(call.id, call);
                    else if (call.argList.size() > expr.args.size())
                        Error.TooFewArguments(call.id, call);
                    else {
                        int i = 0;
                        for (Expression e : expr.args) {
                            if (!CType.canAssign(getType(expr.args.get(i)), getType(call.argList.get(i)))) {
                                Error.IncompatibleArgs(call.id, i, call);
                            }
                            i++;
                        }
                    }
                }
                return null;
            }

            public CType visit(Expression.Array expr) {
                return null;
            }
        });
    }
}