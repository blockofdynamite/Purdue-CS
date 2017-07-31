package simplec;

import simplec.parse.*;

import java.util.HashMap;

public class Scope {

    public static Scope rootScope = null;
    public static Scope topScope = null;

    public Scope parent;

    public HashMap<String, AST.Value> vars;

    public boolean inLoop;

    public Scope(Scope par) {
        this.parent = par;
        vars = new HashMap<>();
        inLoop = false;
    }

    public void put(String varName, AST.Value value) {
        if (vars.containsKey(varName)) {
            if (vars.get(varName) instanceof AST.Value.Function && value instanceof AST.Value.Function) {
                Error.DuplicateVariable(value.token);
                return;
            }
            else if (vars.get(varName) instanceof AST.Value.Variable && value instanceof AST.Value.Variable) {
                Error.DuplicateVariable(value.token);
                return;
            }
        }
        vars.put(varName, value);
    }

    public AST.Value pull(String varName) {
        for (Scope scope = this; scope != null; scope = scope.parent) {
            AST.Value value = scope.vars.get(varName);
            if (value != null) {
                return value;
            }
        }
        return null;
    }
}
