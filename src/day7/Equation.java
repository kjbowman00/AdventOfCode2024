package day7;

import java.math.BigInteger;

public class Equation {
    public final BigInteger total;
    public final BigInteger[] operands;

    public Equation(String equation) {
        String[] equalsSplit = equation.split(":");
        total = new BigInteger(equalsSplit[0].trim());

        String[] operandStrings = equalsSplit[1].trim().split(" ");
        int numOperands = operandStrings.length;
        operands = new BigInteger[numOperands];

        for (int i = 0; i < numOperands; i++) {
            operands[i] = new BigInteger(operandStrings[i].trim());
        }
    }
}
