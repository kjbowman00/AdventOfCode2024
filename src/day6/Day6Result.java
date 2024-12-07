package day6;

import api.DayResult;

public class Day6Result implements DayResult {
    public int visitCount;
    public int loopableObstructionsCount;

    public Day6Result(int visitCount, int loopableObstructionsCount) {
        this.visitCount = visitCount;
        this.loopableObstructionsCount = loopableObstructionsCount;
    }

    @Override
    public void printResult() {
        System.out.println("Visit Count: " + visitCount);
        System.out.println("Time Loopable Obstructions Count: " + loopableObstructionsCount);
    }
}
