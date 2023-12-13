package com.richtirix.adventofcode.days;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class Day13 {
    public static long solve(Path filePath, boolean isPart2) throws IOException {

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

        return map.stream().map(block -> calculateSymmetricResult(block, isPart2)).reduce(0L, Long::sum);
    }

    private static long calculateSymmetricResult(List<String> blockLines, boolean isPart2) {
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

        return 100L * retrieveSymmetricCandidates(rowNumbers, isPart2) + retrieveSymmetricCandidates(columnNumbers, isPart2);
    }

    private static int retrieveSymmetricCandidates(List<Long> numbers, boolean isPart2) {
        List<Integer> candidates = new ArrayList<>();
        for (int i = 1; i < numbers.size(); i++) {
            if(
                numbers.get(i).equals(numbers.get(i-1)) || (isPart2 && isPowerOfTwo(Math.abs(numbers.get(i) - numbers.get(i-1))))
            ) {
                candidates.add(i);
            }
        }
        for (Integer candidate : candidates) {
            if(isSymmetricLine(numbers, candidate, isPart2)) {
                return candidate;
            }
        }
        return 0;
    }

    private static boolean isSymmetricLine(List<Long> rowNumbers, int lineIndex, boolean isPart2) {
        boolean powerOfTwoAlreadyUsed = false;
        int i = 0;
        while (lineIndex + i < rowNumbers.size() && lineIndex - i - 1 >= 0) {
            if (!rowNumbers.get(lineIndex + i).equals(rowNumbers.get(lineIndex - i - 1))) {
                if(
                    !isPart2 ||
                        !isPowerOfTwo(Math.abs(rowNumbers.get(lineIndex+i) - rowNumbers.get(lineIndex - i - 1))) ||
                            powerOfTwoAlreadyUsed
                ){
                    return false;
                }
                powerOfTwoAlreadyUsed = true;
            }
            i++;
        }
        return (!isPart2 || powerOfTwoAlreadyUsed);
    }

    private static boolean isPowerOfTwo(long n)
    {
        if (n == 0)
            return false;

        double v = Math.log(n) / Math.log(2);
        return (int)(Math.ceil(v)) == (int)(Math.floor(v));
    }
}
