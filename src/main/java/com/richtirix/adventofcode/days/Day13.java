package com.richtirix.adventofcode.days;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.IntStream;

public class Day13 {
    public static long solve(Path filePath, boolean isPart2) throws IOException, ExecutionException, InterruptedException {

        var lines = Files.readAllLines(filePath);
        List<String> blockLines = new ArrayList<>();
        List<List<String>> map = new ArrayList<>();
        for(String line : lines) {
            if(line.isEmpty()) {
                map.add(blockLines);
                blockLines = new ArrayList<>();
            } else {
                blockLines.add(line);
            }
        }
        map.add(blockLines);

        if(!isPart2) {
            return map.stream().map(Day13::calculateSymetricResult).reduce(0L, Long::sum);
        }
        return 0L;
    }

    private static long calculateSymetricResult(List<String> blockLines) {
        List<Long> rowNumbers = blockLines.stream().map(
            line -> Long.parseLong(line.replace("#", "1").replace(".", "0"), 2)
        ).toList();
        List<Long> columnNumbers = IntStream.range(0, blockLines.getFirst().length()).boxed().map(
            columnIndex ->  {
                StringBuilder buffer = new StringBuilder();
                for (String blockLine : blockLines) {
                    buffer.append(blockLine.charAt(columnIndex));
                }
                return Long.parseLong(buffer.toString().replace("#", "1").replace(".", "0"), 2);
            }
        ).toList();

        return 100L * retrieveSymetricCandidates(rowNumbers) + retrieveSymetricCandidates(columnNumbers);
    }

    private static int retrieveSymetricCandidates(List<Long> rowNumbers) {
        List<Integer> candidates = new ArrayList<>();
        for (int i = 1; i < rowNumbers.size(); i++) {
            if(rowNumbers.get(i).equals(rowNumbers.get(i-1))) {
                candidates.add(i);
            }
        }
        for (Integer integer : candidates) {
            boolean isSymetricLine = true;
            int i = 0;
            int candidate = integer;
            while (candidate + i < rowNumbers.size() && candidate - i - 1 >= 0) {
                if (!rowNumbers.get(candidate + i).equals(rowNumbers.get(candidate - i - 1))) {
                    isSymetricLine = false;
                    break;
                }
                i++;
            }
            if (isSymetricLine) {
                return candidate;
            }
        }
        return 0;
    }
}
