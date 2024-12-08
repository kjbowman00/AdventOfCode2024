import api.DayResult;
import day6.Day6;
import day7.Day7;
import day8.Day8;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) throws IOException {
        // Select the input and day
        Day8 day = new Day8();
        String inputFilePath = "data/day8/day8.txt";

        // Read the input
        String inputString = Files.readString(Paths.get(inputFilePath));

        // Run the day's code
        DayResult result = day.run(inputString);
        result.printResult();
    }
}