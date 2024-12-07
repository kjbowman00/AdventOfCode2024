package day7;

import api.DayResult;

public class Day7 {
    public DayResult run(String input) {
        input = input.replace("\r", "");
        String[] equationStrings = input.split("\n");

        int numEquations = equationStrings.length;
        Equation[] equations = new Equation[numEquations];
        for (int i = 0; i < numEquations; i++) {
            equations[i] = new Equation(equationStrings[i].trim());
        }


        long calibrationTotal = 0;
        for (Equation equation : equations) {
            ValueHolder value = new ValueHolder(equation.operands[0]);

            if (check(0, equation, new ValueHolder(equation.operands[0]), Operator.ADD) ||
                    check(0, equation, new ValueHolder(equation.operands[0]), Operator.MULTIPLY)) {

                calibrationTotal += equation.total;
            }
        }


        long finalCalibrationTotal = calibrationTotal;
        return new DayResult() {
            @Override
            public void printResult() {
                System.out.println("Calibration Total: " + finalCalibrationTotal);
            }
        };
    }

    private boolean check(int i, Equation equation, ValueHolder value, Operator operator) {
        if (i + 1 >= equation.operands.length) return value.value == equation.total;

        if (operator == Operator.ADD) {
            value.value = value.value + equation.operands[i + 1];
        } else if (operator == Operator.MULTIPLY) {
            value.value = value.value * equation.operands[i + 1];
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
            value.value = addHolder.value;
            return true;
        }

        return false;
    }

    private class ValueHolder {
        long value;
        public ValueHolder(long value) {
            this.value = value;
        }
    }
}
