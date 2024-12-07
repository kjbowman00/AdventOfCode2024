package day7;

public class Equation {
    public final long total;
    public final long[] operands;

    public Equation(String equation) {
        String[] equalsSplit = equation.split(":");
        total = Long.parseLong(equalsSplit[0].trim());

        String[] operandStrings = equalsSplit[1].trim().split(" ");
        int numOperands = operandStrings.length;
        operands = new long[numOperands];

        for (int i = 0; i < numOperands; i++) {
            operands[i] = Long.parseLong(operandStrings[i].trim());
        }
    }
}
