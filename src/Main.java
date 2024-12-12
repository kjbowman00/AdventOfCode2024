import api.DayResult;
import day11.Day11;
import day6.Day6;
import day7.Day7;
import day8.Day8;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) throws IOException {
        // Select the input and day
        Day11 day = new Day11();
        String inputFilePath = "data/day11/day11.txt";

        // Read the input
        String inputString = Files.readString(Paths.get(inputFilePath));

        // Run the day's code
        DayResult result = day.run(inputString);
        result.printResult();
    }
}