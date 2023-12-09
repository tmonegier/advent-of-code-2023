package org.example.days;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Day9 {

    public static long solve(Path filePath, boolean isPart2) throws IOException {
        return Files.lines(filePath).map(line -> getPreviousOrNextData(line, isPart2, !isPart2)).reduce(0L, Long::sum);
    }

    private static long getPreviousOrNextData(String line, boolean isPart2, boolean isPart1) {
        long tmp = 0;
        List<Long> numbers = Arrays.stream(line.split(" ")).map(Long::parseLong).collect(
            LinkedList::new,
            LinkedList::add,
            LinkedList::addAll
        );
        List<Long> initNumbers = new LinkedList<>();

        boolean isAllZeroes = false;
        while(!isAllZeroes) {
            if(isPart1) {
                tmp += numbers.getLast();
            }
            initNumbers.addFirst(numbers.getFirst());
            isAllZeroes = numbers.getLast() == 0;
            int size = numbers.size();
            for(int i = 0; i< size-1; i++) {
                if(numbers.get(0) != 0) {
                    isAllZeroes = false;
                }
                numbers.add(numbers.get(1)-numbers.getFirst());
                numbers.removeFirst();
            }
            numbers.removeFirst();
        }

        if(isPart2) {
            return initNumbers.stream().reduce(0L, (a, b) -> b - a);
        }
        return tmp;
    }
}
