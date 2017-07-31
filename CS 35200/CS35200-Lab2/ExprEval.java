class ExprEval {
    private static boolean badFormat = false;

    public static void main(String args[]) throws Exception {
        if (args.length < 2) {
            System.out.println("Usage: java expression xval");
            System.exit(1);
        }
        double val = evaluate(args[0], Double.parseDouble(args[1]));
        if (badFormat) {
            throw new Exception("Bad format");
        }
        System.out.println("y=" + val);
    }

    /* Grammar

    <expr>   → <term> {(+ | -) <term>}
    <term>   → <factor> {(* | /) <factor>}
    <factor> → id
               | int_constant
               | double_constant
               | ( <expr> )

    */

    private static String str;
    private static int pos = -1;
    private static int ch;

    private static double evaluate(String expr, double xval) {
        expr = expr.replace("x", Double.toString(xval));
        str = expr;
        nextChar();
        if (pos > str.length()) {
            badFormat = true;
        }

        return expression();
    }

    private static void nextChar() {
        pos += 1;
        if (pos < str.length()) {
            ch = str.charAt(pos);
        } else { // End of string and/or no string
            ch = -1;
        }
    }

    private static boolean consume(int charToConsume) {
        while (ch == ' ') { // nuke spaces
            nextChar();
        }
        if (ch == charToConsume) {
            nextChar();
            return true;
        } else {
            return false;
        }
    }

    private static double expression() {
        double x = term();
        while (true) {
            if (consume('+'))  {
                x += term();
            } else if (consume('-')) {
                x -= term();
            } else {
                return x;
            }
        }
    }

    private static double term() {
        double x = factor();
        while (true) {
            if (consume('*')) {
                x *= factor();
            } else if (consume('/')) {
                x /= factor();
            } else {
                return x;
            }
        }
    }

    private static double factor() {
        if (consume('+')) {
            return factor();
        }
        if (consume('-')) {
            return -factor();
        }

        double x;
        double x2 = 0;
        int startPos = pos;

        if (consume('(')) {
            x = expression();
            consume(')');
        } else if (Character.isDigit(ch) || ch == '.') {
            while (Character.isDigit(ch) || ch == '.') {
                nextChar();
            }
            x = Double.parseDouble(str.substring(startPos, pos));
        } else if (Character.isAlphabetic(ch)) {
            while (Character.isAlphabetic(ch)) {
                nextChar();
            }
            String func = str.substring(startPos, pos);
            if (str.substring(startPos, pos + 1).equals("atan2")) {
                func = "atan2";
                nextChar();
            }
            x = factor();
            if (func.equals("atan2") && ch == ',') {
                nextChar();
                x2 = expression();
                nextChar();
            }
            switch (func) {
                case "sin":
                    x = Math.sin(x);
                    break;
                case "cos":
                    x = Math.cos(x);
                    break;
                case "tan":
                    x = Math.tan(x);
                    break;
                case "ln":
                    x = Math.log(x);
                    break;
                case "atan":
                    x = Math.atan(x);
                    break;
                case "atan2":
                    x = Math.atan2(x, x2);
                    break;
                default:
                    badFormat = true;
                    return 0;
            }
        } else {
            badFormat = true;
            return 0;
        }

        if (consume('^')) {
            x = Math.pow(x, factor());
        }

        return x;
    }
}
