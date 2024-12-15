package day14;

import api.DayResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Day14 {
    public DayResult run(String input) {
        input = input.trim().replace("\r", "");

        final int width = 101;
        final int height = 103;
        final int seconds = 100;

        int quad1 = 0, quad2 = 0, quad3 = 0, quad4 = 0;
        String[] lines = input.split("\n");

        Robot[] robots = new Robot[lines.length];
        int i = 0;
        for (String line : lines) {
            // ---- PARSING -----
            String pos = line.split(" ")[0];
            pos = pos.split("=")[1];
            int x = Integer.parseInt(pos.split(",")[0]);
            int y = Integer.parseInt(pos.split(",")[1]);

            String vel = line.split(" ")[1];
            vel = vel.split("=")[1];
            int velX = Integer.parseInt(vel.split(",")[0]);
            int velY = Integer.parseInt(vel.split(",")[1]);
            // ---- END PARSING ----

            robots[i] = new Robot(x, y, velX, velY);
            i++;



            int endX = x + velX * seconds;
            endX = endX % width;
            if (endX < 0) endX = endX + width;
            int endY = y + velY * seconds;
            endY = endY % height;
            if (endY < 0) endY = endY + height;

            if (endY < height / 2) {
                // Upper quads
                if (endX < width / 2) {
                    // up left
                    quad1++;
                } else if (endX > width / 2){
                    // up right
                    quad2++;
                }

            } else if (endY > height / 2){
                // lower quads
                if (endX < width / 2) {
                    // lower left
                    quad3++;
                } else if (endX > width / 2){
                    // lower right
                    quad4++;
                }

            }


        }

        final int startSeconds = 0;
        final int endSeconds = 10000;
        ArrayList<GridClustering> clusteringArrayList = new ArrayList<>();
        for (int s = startSeconds; s < endSeconds; s++) {
            int[][] grid = createGridAt(s, width, height, robots);

            // Calculate clustering
            int clustering = 0;
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    clustering += calculateClustering(x, y, grid, width, height);
                }
            }

            clusteringArrayList.add(new GridClustering(s, clustering));
        }
        clusteringArrayList.sort((o1, o2) -> Integer.compare(o1.clustering, o2.clustering));

        System.out.println(clusteringArrayList.reversed().get(0));
        System.out.println(clusteringArrayList.reversed().get(1));
        // Look at the best
        List<GridClustering> clusteringList = clusteringArrayList.reversed();
        for (int j = 0; j < 5; j++) {
            GridClustering gridClustering = clusteringList.get(j);
            System.out.println(gridClustering);
            printGrid(width, height, createGridAt(gridClustering.secondsGridOccursAt, width, height, robots));
        }


        final int safetyFactor = quad1 * quad2 * quad3 * quad4;


        return new DayResult() {
            @Override
            public void printResult() {
                System.out.println(safetyFactor);
            }
        };
    }

    private int[][] createGridAt(int seconds, int width, int height, Robot[] robots) {
        int[][] grid = new int[width][height];

        // Create grid at this seconds
        for (Robot robot : robots) {
            int endX = robot.x + robot.velX * seconds;
            endX = endX % width;
            if (endX < 0) endX = endX + width;
            int endY = robot.y + robot.velY * seconds;
            endY = endY % height;
            if (endY < 0) endY = endY + height;

            grid[endX][endY]++;
        }
        return grid;
    }

    private void printGrid(int width, int height, int[][] grid) {
        StringBuilder builder = new StringBuilder();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (grid[x][y] > 0) {
                    builder.append("#");
                } else {
                    builder.append("_");
                }
            }
            builder.append("\n");
        }
        builder.append("\n------");

        System.out.println(builder.toString());
    }

    private int calculateClustering(int x, int y, int[][] grid, int width, int height) {
        if (grid[x][y] == 0) return 0;

        Robot left = new Robot(x-1,0,0,0);
        Robot up = new Robot(x, y-1, 0,0);
        Robot right = new Robot(x+1, y, 0 ,0);
        Robot down = new Robot(x, y+1, 0,0);

        int clustering = 0;
        if (inBounds(left, width, height)) {
            if (grid[left.x][left.y] != 0) clustering++;
        }
        if (inBounds(right, width, height)) {
            if (grid[right.x][right.y] != 0) clustering++;
        }
        if (inBounds(up, width, height)) {
            if (grid[up.x][up.y] != 0) clustering++;
        }
        if (inBounds(down, width, height)) {
            if (grid[down.x][down.y] != 0) clustering++;
        }

        return clustering;

    }

    private boolean inBounds(Robot robot, int width, int height) {
        return robot.x >= 0 && robot.x < width && robot.y >= 0 && robot.y < height;
    }

    private class GridClustering {
        public GridClustering(int seconds, int clustering) {
            this.secondsGridOccursAt = seconds;
            this.clustering = clustering;
        }
        int secondsGridOccursAt;
        int clustering;
        @Override
        public String toString() {
            return "seconds: " + secondsGridOccursAt + " clustering: " + clustering;
        }
    }

    private class Robot {
        public Robot(int x, int y, int velX, int velY) {
            this.x = x;
            this.y = y;
            this.velX = velX;
            this.velY = velY;
        }
        int x;
        int y;
        int velX;
        int velY;
    }
}
