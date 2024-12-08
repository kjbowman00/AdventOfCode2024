package day8;

import api.DayResult;
import day6.Vector2;

import java.util.*;

public class Day8 {

    public DayResult run(String input) {
        input = input.replace("\r", "");
        String[] lines = input.split("\n");
        int gridHeight = lines.length;
        int gridWidth = lines[0].length();

        // Create grid of chars
        char[][] grid = new char[gridWidth][gridHeight];
        for (int i = 0; i < gridHeight; i++) {
            for (int j = 0; j < gridWidth; j++) {
                grid[j][i] = lines[i].charAt(j);
            }
        }

        // Map each char to a list of coordinates
        HashMap<Character, List<Vector2>> charToPosition = new HashMap<>();
        for (int x = 0;  x < gridWidth; x++) {
            for (int y = 0; y < gridHeight; y++) {
                char charAt = grid[x][y];
                if (charAt == '.') continue; // Skip periods
                if (! charToPosition.containsKey(charAt)) {
                    charToPosition.put(charAt, new ArrayList<>());
                }

                charToPosition.get(charAt).add(new Vector2(x, y));
            }
        }

        // Create a grid of sets of chars. This will coresspond to what aantinode is at each xy coordinate
        ArrayList<ArrayList<Set<Character>>> antinodes = new ArrayList<>();
        for (int x = 0; x < gridWidth; x++) {
            antinodes.add(new ArrayList<>());
            for (int y = 0; y < gridHeight; y++) {
                Set<Character> characters = new HashSet<>();
                antinodes.get(x).add(characters);
            }
        }

        // Find all antinodes
        int uniqueAntinodeCount = 0;
        for (Map.Entry<Character, List<Vector2>> entry : charToPosition.entrySet()) {
            char c = entry.getKey();
            List<Vector2> positions = entry.getValue();

            for (int i = 0; i < positions.size(); i++) {
                Vector2 position = positions.get(i);
                for (int j = 0; j < positions.size(); j++) {
                    Vector2 position2 = positions.get(j);
                    if (j != i) {
                        List<Vector2> antinodePositions = getAntinodePositions(position, position2, gridWidth, gridHeight);

                        for (Vector2 antinodePos : antinodePositions) {
                            if (antinodes.get(antinodePos.x).get(antinodePos.y).isEmpty()) {
                                uniqueAntinodeCount++;
                            }
                            antinodes.get(antinodePos.x).get(antinodePos.y).add(c);
                        }
                    }
                }
            }
        }


        int finalUniqueAntinodeCount = uniqueAntinodeCount;
        return new DayResult() {
            @Override
            public void printResult() {
                System.out.println(finalUniqueAntinodeCount);

            }
        };
    }

    private boolean outOfBounds(int x, int y, int w, int h) {
        return x < 0 || x >= w || y < 0 || y >= h;
    }


    private List<Vector2> getAntinodePositions(Vector2 p1, Vector2 p2, int gridWidth, int gridHeight) {
        Vector2 diffVector = new Vector2(p1.x -p2.x, p1.y - p2.y);
        diffVector = lowestFactored(diffVector);

        ArrayList<Vector2> antinodes = new ArrayList<>();
        Vector2 antinodePos = new Vector2(p2.x + diffVector.x, p2.y + diffVector.y);
        int multiple = 1;
        while (! outOfBounds(antinodePos.x, antinodePos.y, gridWidth, gridHeight)) {
            antinodes.add(antinodePos);

            multiple++;
            antinodePos = new Vector2(p2.x + diffVector.x * multiple, p2.y + diffVector.y * multiple);
        }

        return antinodes;
    }

    private Vector2 lowestFactored(Vector2 v) {
        int gcd = gcd(v.x, v.y);
        return new Vector2(v.x / gcd, v.y / gcd);
    }

    private int gcd(int a, int b)
    {
        a = Math.abs(a);
        b = Math.abs(b);
        if (a == 0)
            return b;
        return gcd(b % a, a);
    }
}
