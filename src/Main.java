import day6.Day6;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) throws IOException {
        // Select the input and day
        Day6 day = new Day6();
        String inputFilePath = "data/day6.txt";

        // Read the input
        String inputString = Files.readString(Paths.get(inputFilePath));

        // Run the day's code
        day.run(inputString);
    }
}