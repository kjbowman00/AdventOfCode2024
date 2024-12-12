package day11;

import api.DayResult;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

public class Day11 {
    public DayResult run(String input) {
        input = input.trim();
        String[] numStrings = input.split(" ");

        // Generate the graves list
        LinkedList<Long> graves = new LinkedList<>();
        for (String numString : numStrings) {
            graves.addLast(Long.parseLong(numString));
        }

        // Loop 25 times
        int N = 25;
        for (int i = 0; i < N; i++) {
            ListIterator<Long> it = graves.listIterator();
            while (it.hasNext()) {
                long graveValue = it.next();
                String graveValueString = String.valueOf(graveValue);

                if (graveValue == 0) {
                    it.set(1L);
                } else if (graveValueString.length() % 2 == 0) {
                    int half = graveValueString.length() / 2;
                    long leftValue = Long.valueOf(graveValueString.substring(0, half));
                    long rightValue = Long.valueOf(graveValueString.substring(half, graveValueString.length()));

                    it.set(leftValue);
                    it.add(rightValue);
                } else {
                    it.set(graveValue * 2024);
                }
            }
        }


        return new DayResult() {
            @Override
            public void printResult() {
                System.out.println(graves.size());
            }
        };
    }
}
