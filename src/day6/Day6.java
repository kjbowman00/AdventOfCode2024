package day6;

public class Day6 {
    public void run(String input) {
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

        // Create the grid
        PositionType[][] grid = new PositionType[gridWidth][gridHeight];
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
            }
        }

        // Defaults to this
        Direction guardDirection = Direction.UP;

        // Run until guard goes out of bounds
        int visitCount = 1;
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
                guardX = inFrontOf.x;
                guardY = inFrontOf.y;
            }
        }
        System.out.println("COUNT: " + visitCount);
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
        if (position.x < 0 || position.x >= grid.length || position.y < 0 || position.y >= grid[0].length) {
            return false;
        }

        return grid[position.x][position.y] == PositionType.OBSTRUCTED;
    }
}

