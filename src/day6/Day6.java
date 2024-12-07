package day6;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Day6 {
    public Day6Result run(String input) {
        input = input.replaceAll("\r", "");
        // First new line is the width of the grid
        int gridWidth = input.indexOf('\n');

        // Height is entire string length divided by width of each line.
        // Add one because last line doesn't have a newline
        int gridHeight = (input.length() + 1) / (gridWidth + 1);

        // Find x, y of guard.
        int index = input.indexOf('^');
        int guardY = index / (gridWidth + 1);
        int guardX = index % (gridWidth + 1);
        final int startingGuardX = guardX;
        final int startingGuardY = guardY;

        // Create the grid
        PositionType[][] grid = new PositionType[gridWidth][gridHeight];
        boolean[][] loopableConstructionSpotsGrid = new boolean[gridWidth][gridHeight];
        for (int y = 0; y < gridHeight; y++) {
            int rowStartIndex = y * (gridWidth + 1);
            for (int x = 0; x < gridWidth; x++) {
                char charAt = input.charAt(rowStartIndex + x);
                PositionType type;
                if (charAt == '#') {
                    type = PositionType.OBSTRUCTED;
                } else if (charAt == '.') {
                    type = PositionType.EMPTY;
                } else if (charAt == '^') {
                    type = PositionType.GUARD;
                } else {
                    throw new RuntimeException("Unexpected symbol: " + charAt + " at x:" + x + " y:" + y);
                }

                grid[x][y] = type;
                loopableConstructionSpotsGrid[x][y] = false;
            }
        }

        // Defaults to this
        Direction guardDirection = Direction.UP;

        // Run until guard goes out of bounds
        int visitCount = 1;
        int wouldCauseLoopCount = 0;
        while (! outOfBounds(guardX, guardY, gridWidth, gridHeight)) {
            // Mark as visisted
            if (grid[guardX][guardY] != PositionType.GUARD) {
                visitCount++;
            }
            grid[guardX][guardY] = PositionType.GUARD;

            Vector2 inFrontOf = getPositionInFrontOf(guardX, guardY, guardDirection);
            if (isObstructed(inFrontOf, grid)) {
                guardDirection = rotate(guardDirection);
            } else {
                // Determine if an obstruction added in front of would cause a loop
                if (! outOfBounds(inFrontOf.x, inFrontOf.y, gridWidth, gridHeight)) {
                    if (!(inFrontOf.x == startingGuardX && inFrontOf.y == startingGuardY) &&
                            wouldCauseLoop(inFrontOf.x, inFrontOf.y,
                            grid, gridWidth, gridHeight, guardX, guardY, guardDirection)) {
                        if (! wouldCutOffEntry(inFrontOf.x, inFrontOf.y, grid, guardX, guardY,
                                guardDirection, startingGuardX, startingGuardY, gridWidth, gridHeight)) {
                            // Check if we've already counted this one
                            if (!loopableConstructionSpotsGrid[inFrontOf.x][inFrontOf.y]) {
                                wouldCauseLoopCount++;
                                loopableConstructionSpotsGrid[inFrontOf.x][inFrontOf.y] = true;
                            }
                        }
                    }
                }

                // Move forwards
                guardX = inFrontOf.x;
                guardY = inFrontOf.y;
            }
        }
        return new Day6Result(visitCount, wouldCauseLoopCount);
    }

    private Direction rotate(Direction direction) {
        if (direction == Direction.UP) return Direction.RIGHT;
        if (direction == Direction.RIGHT) return Direction.DOWN;
        if (direction == Direction.DOWN) return Direction.LEFT;
        if (direction == Direction.LEFT) return Direction.UP;
        throw new RuntimeException("unexpected diretction");
    }

    private boolean outOfBounds(int x, int y, int w, int h) {
        return x < 0 || x >= w || y < 0 || y >= h;
    }

    private Vector2 getPositionInFrontOf(int x, int y, Direction dir) {
        if (dir == Direction.DOWN) {
            return new Vector2(x, y + 1);
        }
        if (dir == Direction.RIGHT) {
            return new Vector2(x + 1, y);
        }
        if (dir == Direction.UP) {
            return new Vector2(x, y - 1);
        }
        if (dir == Direction.LEFT) {
            return new Vector2(x - 1, y);
        }
        throw new RuntimeException("invalid direction");
    }

    private boolean isObstructed(Vector2 position, PositionType[][] grid) {
        // Never obstructed to go out of bounds
        if (outOfBounds(position.x, position.y, grid.length, grid[0].length)) {
            return false;
        }

        return grid[position.x][position.y] == PositionType.OBSTRUCTED;
    }
    private boolean wouldCutOffEntry(int obstructionX, int obstructionY,
                                     PositionType[][] grid, int guardIntendedLocationX,
                                     int guardIntendedLocationY, Direction guardIntendedDirection,
                                     int startGuardX, int startGuardY,
                                     int gridWidth, int gridHeight) {
        // Clone array
        PositionType[][] copy = new PositionType[gridWidth][gridHeight];
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                copy[i][j] = grid[i][j];
            }
        }
        grid = copy; // Don't accidentally reference old grid

        // Setup memoery for where guard was facing in the grid
        ArrayList<ArrayList<Set<Direction>>> alreadyGoneDirectionsGrid = new ArrayList<>();
        for (int i = 0; i < gridWidth; i++) {
            alreadyGoneDirectionsGrid.add(new ArrayList<>());
            for (int j = 0; j < gridHeight; j++) {
                alreadyGoneDirectionsGrid.get(i).add(new HashSet<>());
            }
        }

        grid[obstructionX][obstructionY] = PositionType.OBSTRUCTED;

        int guardX = startGuardX;
        int guardY = startGuardY;
        Direction guardDirection = Direction.UP;

        while (! outOfBounds(guardX, guardY, gridWidth, gridHeight)) {
            if (guardX == guardIntendedLocationX && guardY == guardIntendedLocationY &&
                guardDirection == guardIntendedDirection) {
                return false;
            }

            // Check if looped
            if (grid[guardX][guardY] == PositionType.GUARD &&
                    alreadyGoneDirectionsGrid.get(guardX).get(guardY).contains(guardDirection)) {
                return true;
            }
            grid[guardX][guardY] = PositionType.GUARD;
            alreadyGoneDirectionsGrid.get(guardX).get(guardY).add(guardDirection);

            Vector2 inFrontOf = getPositionInFrontOf(guardX, guardY, guardDirection);
            if (isObstructed(inFrontOf, grid)) {
                guardDirection = rotate(guardDirection);
            } else {
                // Move forwards
                guardX = inFrontOf.x;
                guardY = inFrontOf.y;
            }
        }

        return true;
    }

    private boolean wouldCauseLoop(int obstructionX, int obstructionY,
                                   PositionType[][] grid, int gridWidth,
                                   int gridHeight, int guardX, int guardY, Direction guardDirection) {
        // Clone array
        PositionType[][] copy = new PositionType[gridWidth][gridHeight];
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                copy[i][j] = grid[i][j];
            }
        }
        grid = copy; // Don't accidentally reference old grid


        // Setup memoery for where guard was facing in the grid
        ArrayList<ArrayList<Set<Direction>>> alreadyGoneDirectionsGrid = new ArrayList<>();
        for (int i = 0; i < gridWidth; i++) {
            alreadyGoneDirectionsGrid.add(new ArrayList<>());
            for (int j = 0; j < gridHeight; j++) {
                alreadyGoneDirectionsGrid.get(i).add(new HashSet<>());
            }
        }
        // Set an obstruction in place for the test
        grid[obstructionX][obstructionY] = PositionType.OBSTRUCTED;


        while (! outOfBounds(guardX, guardY, gridWidth, gridHeight)) {
            // Check if looped
            if (grid[guardX][guardY] == PositionType.GUARD &&
                alreadyGoneDirectionsGrid.get(guardX).get(guardY).contains(guardDirection)) {
                return true;
            }
            grid[guardX][guardY] = PositionType.GUARD;
            alreadyGoneDirectionsGrid.get(guardX).get(guardY).add(guardDirection);

            // Turn or move forward
            Vector2 inFrontOf = getPositionInFrontOf(guardX, guardY, guardDirection);
            if (isObstructed(inFrontOf, grid)) {
                guardDirection = rotate(guardDirection);
            } else {
                guardX = inFrontOf.x;
                guardY = inFrontOf.y;
            }
        }
        return false;
    }
}

