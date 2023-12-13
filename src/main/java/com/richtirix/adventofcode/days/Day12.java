package com.richtirix.adventofcode.days;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day12 {

    private final static Pattern LINE_PATTERN = Pattern.compile("(?<pipes>[#|.|?]*) (?<numbers>.*)");
    public static long solve(Path filePath, boolean isPart2) throws IOException {
        AtomicInteger i = new AtomicInteger();
        return Files.lines(filePath).parallel().map(Day12::findNumberOfPossibilities).peek(l -> System.out.println(i.incrementAndGet())).reduce(0L, Long::sum);
    }

    private static Long findNumberOfPossibilities(String line) {
        var matcher = LINE_PATTERN.matcher(line);
        if(matcher.find()) {
            String tmpPipeTemplate = matcher.group("pipes");
            String pipeTemplate = String.join(".", Arrays.stream(tmpPipeTemplate.split("\\.")).filter(Predicate.not(String::isEmpty)).toList());


            List<Integer> numbers = Arrays.stream(matcher.group("numbers").split(",")).map(Integer::parseInt).toList();
            List<Integer> finalNumbers = new ArrayList<>(numbers);

            int totalPipes = finalNumbers.stream().reduce(0, Integer::sum);
            char[] currentPossibility = new char[pipeTemplate.length()];
            Arrays.fill(currentPossibility, '.');

            return findNumberOfPossibilities(pipeTemplate, currentPossibility, 0, finalNumbers, totalPipes);
        }

        return 0L;
    }

    private static Long findNumberOfPossibilities(String pipeTemplate, char[] currentPossibility,  int startIndex, List<Integer> numbers, int totalPipes) {
        if(pipeTemplate.length() - startIndex < totalPipes + numbers.size()-1) {
            return 0L;
        }
        if(numbers.isEmpty()) {
            if(validateCurrentPossibility(pipeTemplate, currentPossibility,0, pipeTemplate.length())) {
                return 1L;
            }
            return 0L;
        }
        if(!validateCurrentPossibility(pipeTemplate, currentPossibility, 0, startIndex)) {
            return 0L;
        }

        long res = 0L;
        int firstNumber = numbers.getFirst();

        for(int i = startIndex; i<=pipeTemplate.length()-firstNumber; i++) {
            for(int j = i; j<i+firstNumber; j++) {
                currentPossibility[j] = '#';
            }
            if(i+firstNumber < currentPossibility.length) {
                currentPossibility[i+firstNumber] = '.';
            }
            res += findNumberOfPossibilities(pipeTemplate, currentPossibility, i+firstNumber+1, numbers.stream().skip(1).toList(), totalPipes - firstNumber);

            Arrays.fill(currentPossibility, startIndex, currentPossibility.length, '.');
        }
        return res;
    }

    private static boolean validateCurrentPossibility(String pipeTemplate, char[] currentPossibility,int startIndex, int endIndex) {

        for(int i = startIndex; i<endIndex; i++) {
            if(pipeTemplate.charAt(i) == '#' && currentPossibility[i] != '#') {
                return false;
            }
            if(pipeTemplate.charAt(i) == '.' && currentPossibility[i] != '.') {
                return false;
            }
        }
        return true;
    }
}
