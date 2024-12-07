import api.DayResult;
import day6.Day6;
import day7.Day7;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) throws IOException {
        // Select the input and day
        Day7 day = new Day7();
        String inputFilePath = "data/day7/day7.txt";

        // Read the input
        String inputString = Files.readString(Paths.get(inputFilePath));

        // Run the day's code
        DayResult result = day.run(inputString);
        result.printResult();
    }
}