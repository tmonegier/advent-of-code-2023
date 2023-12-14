package com.richtirix.adventofcode.days;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Day14 {

    public static long solve(Path filePath, boolean isPart2) throws IOException {

        List<String> rows = Files.readAllLines(filePath);
        return calculateNorthLoad(rows);
    }

    private static long calculateNorthLoad(List<String> rows) {
        long totalLoad = 0L;
        for (int columnIndex = 0; columnIndex < rows.getFirst().length(); columnIndex++) {
            int lastStaticRockIndex = -1;
            for (int rowIndex = 0; rowIndex < rows.size(); rowIndex++) {
                if(rows.get(rowIndex).charAt(columnIndex) == '#') {
                    lastStaticRockIndex = rowIndex;
                }
                if(rows.get(rowIndex).charAt(columnIndex) == 'O') {
                    totalLoad += rows.size() - lastStaticRockIndex -1;
                    lastStaticRockIndex ++;
                }
            }
        }
        return totalLoad;
    }
}
