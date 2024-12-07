package day6;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day6Test {
    @Test
    public void simpleLoop() throws IOException {
        Day6 day = new Day6();
        String inputFilePath = "data/day6/simpleLoop.txt";

        // Read the input
        String inputString = Files.readString(Paths.get(inputFilePath));

        Day6Result result = day.run(inputString);
        assertEquals(6, result.visitCount);
        assertEquals(1, result.loopableObstructionsCount);
    }


    @Test
    public void simpleLoopNoEntry() throws IOException {
        Day6 day = new Day6();
        String inputFilePath = "data/day6/simpleLoopNoEntry.txt";

        // Read the input
        String inputString = Files.readString(Paths.get(inputFilePath));

        Day6Result result = day.run(inputString);
        assertEquals(7, result.visitCount);
        assertEquals(0, result.loopableObstructionsCount);
    }

    @Test
    public void simpleLoopOnGuardCantPlace() throws IOException {
        Day6 day = new Day6();
        String inputFilePath = "data/day6/simpleLoopOnGuardCantPlace.txt";

        // Read the input
        String inputString = Files.readString(Paths.get(inputFilePath));

        Day6Result result = day.run(inputString);
        assertEquals(6, result.visitCount);
        assertEquals(0, result.loopableObstructionsCount);
    }
}
