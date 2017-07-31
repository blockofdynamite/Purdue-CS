package simplec;

import simplec.parse.Token;

import java.util.*;

public enum CType {
    VOID,
    CHAR,
    CHARSTAR,
    CHARSTARSTAR,
    LONG,
    LONGSTAR,
    DOUBLE,
    DOUBLESTAR,
    ERROR;

    public static HashMap<String, CType> stringMap =
            new HashMap<>();
    public static EnumMap<CType, String> enumMap =
            new EnumMap<>(CType.class);

    static {
        stringMap.put("void", CType.VOID);
        stringMap.put("char", CType.CHAR);
        stringMap.put("char*", CType.CHARSTAR);
        stringMap.put("char**", CType.CHARSTARSTAR);
        stringMap.put("long", CType.LONG);
        stringMap.put("long*", CType.LONGSTAR);
        stringMap.put("double", CType.DOUBLE);
        stringMap.put("double*", CType.DOUBLESTAR);
        for (Map.Entry<String, CType> entry : stringMap.entrySet()) {
            enumMap.put(entry.getValue(), entry.getKey());
        }
    }

    public static CType fromString(String str) {
        return stringMap.getOrDefault(str, CType.ERROR);
    }

    @Override
    public String toString() {
        assert(enumMap.containsKey(this));
        return enumMap.get(this);
    }

    public static boolean canAssign(Token t, CType a, CType b) {
        if (a == b) {
            return true;
        } else if ((a == CHAR || a == DOUBLE || a == LONG) && (b == CHAR || b == DOUBLE || b == LONG)) {
            return true;
        } else if (b == CHARSTAR && a == CHARSTARSTAR) {
            return true;
        } else if ((a == LONG || a == CHAR) && (b == CHARSTAR || b == CHARSTARSTAR || b == LONGSTAR || b == DOUBLESTAR)) {
            Error.AssignPointerToInt(t);
            return false;
        } else if ((b == LONG || b == CHAR) && (a == CHARSTAR || a == CHARSTARSTAR || a == LONGSTAR || a == DOUBLESTAR)) {
            Error.AssignPointerToInt(t);
            return false;
        } else if (a == DOUBLE && (b == CHARSTAR || b == CHARSTARSTAR)) {
            Error.IncompatibleAssignType(t, a, b);
            return false;
        } else if (b == DOUBLE && (a == CHARSTAR || a == CHARSTARSTAR)) {
            Error.IncompatibleAssignType(t, a, b);
            return false;
        }

        Error.IncompatibleAssignType(t, a, b);

        return false;
    }

    public static boolean canAssign(CType a, CType b) {
        if (a == b) {
            return true;
        } else if ((a == CHAR || a == DOUBLE || a == LONG) && (b == CHAR || b == DOUBLE || b == LONG)) {
            return true;
        } else if (b == CHARSTAR && a == CHARSTARSTAR) {
            return true;
        } else if ((a == LONG || a == DOUBLE || a == CHAR) && (b == CHARSTAR || b == CHARSTARSTAR || b == LONGSTAR || b == DOUBLESTAR)) {
            return false;
        } else if ((b == LONG || b == DOUBLE || b == CHAR) && (a == CHARSTAR || a == CHARSTARSTAR || a == LONGSTAR || a == DOUBLESTAR)) {
            return false;
        }

        return false;
    }
}
