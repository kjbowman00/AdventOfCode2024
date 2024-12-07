package day7;

import api.DayResult;

import java.math.BigInteger;

public class Day7 {
    public DayResult run(String input) {
        input = input.replace("\r", "");
        String[] equationStrings = input.split("\n");

        int numEquations = equationStrings.length;
        Equation[] equations = new Equation[numEquations];
        for (int i = 0; i < numEquations; i++) {
            equations[i] = new Equation(equationStrings[i].trim());
        }


        BigInteger calibrationTotal = new BigInteger("0");
        for (Equation equation : equations) {
            ValueHolder value = new ValueHolder(equation.operands[0]);

            if (check(0, equation, new ValueHolder(equation.operands[0]), Operator.ADD) ||
                    check(0, equation, new ValueHolder(equation.operands[0]), Operator.MULTIPLY) ||
                    check(0, equation, new ValueHolder(equation.operands[0]), Operator.CONCAT)) {

                calibrationTotal = calibrationTotal.add(equation.total);
            }
        }


        BigInteger finalCalibrationTotal = calibrationTotal;
        return new DayResult() {
            @Override
            public void printResult() {
                System.out.println("Calibration Total: " + finalCalibrationTotal);
            }
        };
    }

    private boolean check(int i, Equation equation, ValueHolder value, Operator operator) {
        if (i + 1 >= equation.operands.length) return value.value.equals(equation.total);

        if (operator == Operator.ADD) {
            value.value = value.value.add(equation.operands[i + 1]);
        } else if (operator == Operator.MULTIPLY) {
            value.value = value.value.multiply(equation.operands[i + 1]);
        } else if (operator == Operator.CONCAT) {
            String valueString = String.valueOf(value.value);
            String nextString = String.valueOf(equation.operands[i+1]);
            value.value = new BigInteger(valueString + nextString);
        }

        ValueHolder addHolder = new ValueHolder(value.value);
        boolean addWorks = check(i + 1, equation, addHolder, Operator.ADD);
        if (addWorks) {
            value.value = addHolder.value;
            return true;
        }

        ValueHolder multiplyHolder = new ValueHolder(value.value);
        boolean multiplyWorks = check(i + 1, equation, multiplyHolder, Operator.MULTIPLY);
        if (multiplyWorks) {
            value.value = multiplyHolder.value;
            return true;
        }

        ValueHolder concatHolder = new ValueHolder(value.value);
        boolean concatWorks = check(i + 1, equation, concatHolder, Operator.CONCAT);
        if (concatWorks) {
            value.value = concatHolder.value;
            return true;
        }

        return false;
    }

    private class ValueHolder {
        BigInteger value;
        public ValueHolder(BigInteger value) {
            this.value = value;
        }
    }
}
