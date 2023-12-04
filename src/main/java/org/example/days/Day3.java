package org.example.days;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Day3 {
    private record Point(int x, int y) {
    }

    public static long solve(Path path) throws IOException {
        List<String> lines = Files.readAllLines(path);
        int currentNumber = 0;
        Set<Point> currentNumberPossibleGears = new HashSet<>();
        Map<Point, List<Integer>> possibleGears = new HashMap<>();
        int decade = 1;

        for(int lineIndex = 0; lineIndex < lines.size(); lineIndex++) {
            String line = lines.get(lineIndex);

            reportPossibleGears(currentNumber, currentNumberPossibleGears, possibleGears);
            currentNumberPossibleGears = new HashSet<>();
            decade = 1;
            currentNumber = 0;

            for(int index = line.length()-1; index >=0; index--) {
                if(Character.isDigit(line.charAt(index))) {
                    currentNumber += decade * (Character.getNumericValue(line.charAt(index)));
                    decade *= 10;

                    accumulateCurrentNumberPossibleGears(index, line, currentNumberPossibleGears, lineIndex );
                    if(lineIndex > 0) {
                        String previousLine = lines.get(lineIndex -1);
                        accumulateCurrentNumberPossibleGears(index, previousLine, currentNumberPossibleGears, lineIndex - 1);
                    }
                    if(lineIndex < lines.size() - 1) {
                        String nextLine = lines.get(lineIndex + 1);
                        accumulateCurrentNumberPossibleGears(index, nextLine, currentNumberPossibleGears, lineIndex + 1);
                    }

                } else {
                    reportPossibleGears(currentNumber, currentNumberPossibleGears, possibleGears);
                    currentNumberPossibleGears = new HashSet<>();
                    decade = 1;
                    currentNumber = 0;
                }
            }
        }

        return possibleGears.entrySet()
            .stream()
            .filter(pointListEntry -> pointListEntry.getValue().size() == 2)
            .map(pointListEntry -> pointListEntry.getValue().get(0) * pointListEntry.getValue().get(1))
            .reduce(0, Integer::sum);
    }

    private static void reportPossibleGears(int currentNumber, Set<Point> currentNumberPoints, Map<Point, List<Integer>> map) {
        if(currentNumber != 0) {
            for(Point p : currentNumberPoints) {
                if(map.containsKey(p)) {
                    map.get(p).add(currentNumber);
                } else {
                    map.put(p, new ArrayList<>(List.of(currentNumber)));
                }
            }
        }
    }

    private static void accumulateCurrentNumberPossibleGears(int index, String previousLine, Set<Point> currentNumberPoints, int lineIndex) {
        if (index > 0) {
            if (previousLine.charAt(index - 1) == '*') {
                currentNumberPoints.add(new Point(lineIndex, index - 1));
            }
        }
        if (previousLine.charAt(index) == '*') {
            currentNumberPoints.add(new Point(lineIndex, index));
        }
        if (index < previousLine.length() - 1) {
            if (previousLine.charAt(index + 1) == '*') {
                currentNumberPoints.add(new Point(lineIndex, index + 1));
            }
        }
    }
}
