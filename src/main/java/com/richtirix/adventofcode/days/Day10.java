package com.richtirix.adventofcode.days;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

public class Day10 {

    public static long solve(Path filePath, boolean isPart2) throws IOException {
        List<String> lines = Files.readAllLines(filePath);
        Point startingPoint = getStartingPoint(lines);
        replaceStartingPointByActualCharacter(lines, startingPoint);

        Set<Point> pathPoints = new HashSet<>();
        buildPipePath(startingPoint, lines, pathPoints);

        if(!isPart2) {
            return pathPoints.size() / 2;
        }

        return getNumberOfInsiders(lines, pathPoints);
    }

    private static Point getStartingPoint(
            List<String> lines
    ) {
        return IntStream.range(0, lines.size())
                .boxed()
                .filter(lineIndex -> lines.get(lineIndex).contains("S"))
                .map(lineIndex -> new Point(lineIndex, lines.get(lineIndex).indexOf('S')))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No starting point"));
    }

    private static void replaceStartingPointByActualCharacter(List<String> lines, Point startingPoint) {
        List<Character> possibleCharacters = new ArrayList<>(List.of('-', 'F', 'L', '7', 'J', '|'));
        var sameLine = lines.get(startingPoint.x);
        if(startingPoint.y > 0 && List.of('-', 'F', 'L').contains(sameLine.charAt(startingPoint.y-1))){
            possibleCharacters.removeAll(List.of('|', 'L', 'F'));
        }
        if(startingPoint.y < sameLine.length()-1 && List.of('-', 'J', '7').contains(sameLine.charAt(startingPoint.y+1))){
            possibleCharacters.removeAll(List.of('|', '7', 'J'));
        }
        if(startingPoint.x < lines.size()-1 && List.of('|', 'J', 'L').contains(lines.get(startingPoint.x+1).charAt(startingPoint.y))){
            possibleCharacters.removeAll(List.of('-', 'L', 'J'));
        }
        if(startingPoint.x > 0 && List.of('|', 'F', '7').contains(lines.get(startingPoint.x-1).charAt(startingPoint.y))){
            possibleCharacters.removeAll(List.of('-', 'F', '7'));
        }
        lines.set(startingPoint.x, lines.get(startingPoint.x).replace('S',possibleCharacters.getFirst()));
    }

    private static void buildPipePath(Point startingPoint, List<String> lines, Set<Point> pathPoints) {
        Point currentPoint = startingPoint;
        Point previousPoint = null;
        do {
            Point nextPoint = findNextPoint(currentPoint, previousPoint, lines);
            previousPoint = currentPoint;
            currentPoint = nextPoint;
            pathPoints.add(currentPoint);
        } while (!currentPoint.equals(startingPoint));
    }

    private static Point findNextPoint(Point currentPoint, Point previousPoint, List<String> lines) {
        return switch(lines.get(currentPoint.x).charAt(currentPoint.y)){
            case '|' -> {
                if(previousPoint == null || previousPoint.x == currentPoint.x + 1) {
                    yield new Point(currentPoint.x - 1, currentPoint.y);
                }
                yield new Point(currentPoint.x + 1, currentPoint.y);
            }
            case '-' -> {
                if(previousPoint == null || previousPoint.y == currentPoint.y + 1) {
                    yield new Point(currentPoint.x , currentPoint.y - 1);
                }
                yield new Point(currentPoint.x, currentPoint.y + 1);
            }
            case 'L' -> {
                if(previousPoint == null || previousPoint.x == currentPoint.x) {
                    yield new Point(currentPoint.x - 1, currentPoint.y);
                }
                yield new Point(currentPoint.x, currentPoint.y + 1);
            }
            case 'J' -> {
                if(previousPoint == null || previousPoint.x == currentPoint.x) {
                    yield new Point(currentPoint.x - 1 , currentPoint.y);
                }
                yield new Point(currentPoint.x, currentPoint.y - 1);
            }
            case '7' -> {
                if(previousPoint == null || previousPoint.x == currentPoint.x) {
                    yield new Point(currentPoint.x + 1 , currentPoint.y);
                }
                yield new Point(currentPoint.x, currentPoint.y - 1);
            }
            case 'F' -> {
                if(previousPoint == null || previousPoint.x == currentPoint.x) {
                    yield new Point(currentPoint.x + 1 , currentPoint.y);
                }
                yield new Point(currentPoint.x, currentPoint.y + 1);
            }
            default ->
                    throw new IllegalStateException("Unexpected value: " + lines.get(currentPoint.x).charAt(currentPoint.y));
        };
    }

    private static int getNumberOfInsiders(List<String> lines, Set<Point> pathPoints) {
        int numberOfInsiders = 0;
        boolean isInside = false;
        char previousAngle = ' ';
        for(int i = 0; i< lines.size() * lines.getFirst().length(); i++) {
            int x = i / lines.getFirst().length();
            int y = i % lines.getFirst().length();

            if(pathPoints.contains(new Point(x, y))) {
                char pathCharacter = lines.get(x).charAt(y);
                if(pathCharacter == '|') {
                    isInside = !isInside;
                } else if(pathCharacter != '-') {
                    if(previousAngle == ' ') {
                        previousAngle = pathCharacter;
                    } else {
                        if((previousAngle == 'F' && pathCharacter == 'J') || (previousAngle == 'L' && pathCharacter == '7')) {
                            isInside = !isInside;
                        }
                        previousAngle = ' ';
                    }
                }

            } else if(isInside) {
                numberOfInsiders ++;
            }
        }
        return numberOfInsiders;
    }

    private record Point(int x, int y){}
}
