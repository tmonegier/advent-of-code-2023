package com.richtirix.adventofcode.days;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


public class Day16 {

    private static final Direction RIGHT = new Direction(0, 1);
    private static final Direction LEFT = new Direction(0, -1);
    private static final Direction UP = new Direction(-1, 0);
    private static final Direction DOWN = new Direction(1, 0);

    public static long solve(Path filePath, boolean isPart2) throws IOException {

        List<String> lines = Files.readAllLines(filePath);
        char[][] block = transformV2(lines);

        if(!isPart2) {
            Set<VisitedPoint> visitedPoints = new HashSet<>();
            goTroughBlock(block, new Point(0,0), RIGHT, visitedPoints);
            return visitedPoints.stream().collect(Collectors.groupingBy(VisitedPoint::point)).entrySet().size();
        }

        long res = 0;
        for(int i = 0; i< block.length; i++) {
            Set<VisitedPoint> visitedPoints = new HashSet<>();
            goTroughBlock(block, new Point(i,0), RIGHT, visitedPoints);
            res = Math.max(res, visitedPoints.stream().collect(Collectors.groupingBy(VisitedPoint::point)).entrySet().size());

            visitedPoints = new HashSet<>();
            goTroughBlock(block, new Point(i,block[0].length-1), LEFT, visitedPoints);
            res = Math.max(res, visitedPoints.stream().collect(Collectors.groupingBy(VisitedPoint::point)).entrySet().size());
        }

        for(int i = 0; i< block[0].length; i++) {
            Set<VisitedPoint> visitedPoints = new HashSet<>();
            goTroughBlock(block, new Point(0,i), DOWN, visitedPoints);
            res = Math.max(res, visitedPoints.stream().collect(Collectors.groupingBy(VisitedPoint::point)).entrySet().size());

            visitedPoints = new HashSet<>();
            goTroughBlock(block, new Point(block.length-1,i), UP, visitedPoints);
            res = Math.max(res, visitedPoints.stream().collect(Collectors.groupingBy(VisitedPoint::point)).entrySet().size());
        }
        return res;
    }

    private static char[][] transformV2(List<String> lines) {
        char[][] block = new char[lines.size()][];
        for (int i = 0; i < lines.size(); i++) {
            block[i] = new char[lines.get(i).length()];
            for (int i1 = 0; i1 < lines.get(i).length(); i1++) {
                block[i][i1] = lines.get(i).charAt(i1);
            }
        }
        return block;
    }

    private static void goTroughBlock(char[][] block, Point currentPoint, Direction direction, Set<VisitedPoint> visitedPoints) {
        if(currentPoint.x >= 0 && currentPoint.x < block.length && currentPoint.y >= 0 && currentPoint.y < block[0].length && visitedPoints.add(new VisitedPoint(currentPoint, direction))) {
            Direction[] nextDirections = calculateNextDirections(block[currentPoint.x][currentPoint.y], direction);
            for (Direction nextDirection : nextDirections) {
                goTroughBlock(block, new Point(currentPoint.x + nextDirection.x, currentPoint.y + nextDirection.y), nextDirection, visitedPoints);
            }
        }
    }

    private static Direction[] calculateNextDirections(char character, Direction direction) {
        return switch(character) {
            case '-' -> {
                if (direction.equals(RIGHT) || direction.equals(LEFT)) {
                    yield new Direction[] {direction};
                }
                yield new Direction[] {RIGHT, LEFT};
            }
            case '|' -> {
                if (direction.equals(UP) || direction.equals(DOWN)) {
                    yield new Direction[] {direction};
                }
                yield new Direction[] {UP, DOWN};
            }
            case '/' -> {
                if (direction.equals(UP)) {
                    yield new Direction[] {RIGHT};
                }
                if (direction.equals(DOWN)) {
                    yield new Direction[] {LEFT};
                }
                if (direction.equals(LEFT)) {
                    yield new Direction[] {DOWN};
                }
                if (direction.equals(RIGHT)) {
                    yield new Direction[] {UP};
                }
                yield  new Direction[] {direction};
            }
            case '\\' -> {
                if (direction.equals(UP)) {
                    yield new Direction[] {LEFT};
                }
                if (direction.equals(DOWN)) {
                    yield new Direction[] {RIGHT};
                }
                if (direction.equals(LEFT)) {
                    yield new Direction[] {UP};
                }
                if (direction.equals(RIGHT)) {
                    yield new Direction[] {DOWN};
                }
                yield  new Direction[] {direction};
            }
            default -> new Direction[] {direction};
        };
    }

    private record Point(int x, int y){}
    private record Direction(int x, int y) {}
    private record VisitedPoint(Point point, Direction direction){}
}
