
class FloatParser {
    public static void main(String args[]) throws Exception {
        if (args.length == 0) {
            System.out.println("Usage: java FloatParser value");
            System.exit(1);
        }
        double val = MyParseFloat(args[0]);
        System.out.println("Value=" + val);
    }

    enum StateDecimal {START, SSIGN, SINTEGER, SDECIMAL, DOT, EEXPON, ESIGN, EINTEGER, END}

    public static double MyParseFloat(String s) throws Exception {
        // Using the code in DecimalParser.java write a finite state
        // machine that parses a floating point number of the form
        //             [-+]?[0-9]*\.?[0-9]+([eE][-+]?[0-9]+)?

        StateDecimal state;

        state = StateDecimal.START;

        boolean positive = true;
        boolean ePositive = true;
        int divider = 10;
        double integerValue = 0;
        double decimalValue = 0;
        double exponentValue = 0;

        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            switch (state) {
                case START:
                    if (Character.isDigit(ch)) {
                        state = StateDecimal.SINTEGER;
                        integerValue = Character.getNumericValue(ch);
                    } else if (ch == '+') {
                        state = StateDecimal.SSIGN;
                        positive = true;
                    } else if (ch == '-') {
                        state = StateDecimal.SSIGN;
                        positive = false;
                    } else if (ch == '.') {
                        state = StateDecimal.DOT;
                    } else {
                        throw new Exception("Bad format");
                    }
                    break;
                case SSIGN:
                    if (Character.isDigit(ch)) {
                        state = StateDecimal.SINTEGER;
                        integerValue = Character.getNumericValue(ch);
                    } else if (ch == '.') {
                        state = StateDecimal.DOT;
                    } else {
                        throw new Exception("Bad format");
                    }
                    break;
                case SINTEGER:
                    if (Character.isDigit(ch)) {
                        integerValue = 10.0 * integerValue + Character.getNumericValue(ch);
                    } else if (ch == '.') {
                        state = StateDecimal.DOT;
                    } else if (ch == 'e' || ch == 'E') {
                        state = StateDecimal.EEXPON;
                    } else {
                        throw new Exception("Bad format");
                    }
                    break;
                case DOT:
                    if (Character.isDigit(ch)) {
                        state = StateDecimal.SDECIMAL;
                        decimalValue = (double) Character.getNumericValue(ch) / divider;
                        divider *= 10;
                    } else if (ch == 'e' || ch == 'E') {
                        state = StateDecimal.EEXPON;
                    } else {
                        throw new Exception("Bad format");
                    }
                    break;
                case SDECIMAL:
                    if (Character.isDigit(ch)) {
                        decimalValue = decimalValue + (double) Character.getNumericValue(ch) / divider;
                        divider = divider * 10;
                    } else if (ch == 'e' || ch == 'E') {
                        state = StateDecimal.EEXPON;
                    } else {
                        throw new Exception("Bad format");
                    }
                    break;
                case EEXPON:
                    if (ch == '+') {
                        state = StateDecimal.ESIGN;
                        ePositive = true;
                    } else if (ch == '-') {
                        state = StateDecimal.ESIGN;
                        ePositive = false;
                    } else if (Character.isDigit(ch)) {
                        exponentValue = Character.getNumericValue(ch);
                        state = StateDecimal.EINTEGER;
                    } else {
                        throw new Exception("Bad format");
                    }
                    break;
                case ESIGN:
                    if (Character.isDigit(ch)) {
                        state = StateDecimal.EINTEGER;
                        exponentValue = Character.getNumericValue(ch);
                    } else {
                        throw new Exception("Bad format");
                    }
                    break;
                case EINTEGER:
                    if (Character.isDigit(ch)) {
                        state = StateDecimal.EINTEGER;
                        exponentValue = exponentValue * 10 + Character.getNumericValue(ch);
                    }
                    break;
                default:
                    throw new Exception("Bad format");
            }
        }

        double valueToReturn;
        if (positive) {
            valueToReturn = integerValue + decimalValue;
        } else {
            valueToReturn = (integerValue + decimalValue) * -1;
        }

        if (ePositive) {
            for (int i = 0; i < exponentValue; i++) {
                valueToReturn *= 10;
            }
        } else {
            for (int i = 0; i < exponentValue; i++) {
                valueToReturn /= 10;
            }
        }

        return valueToReturn;
    }

}
