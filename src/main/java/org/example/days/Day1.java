package org.example.days;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

public class Day1 {

    private static final List<String> DIGITS = List.of(
        "one", "two", "three", "four", "five", "six", "seven", "eight", "nine"
    );

    public static long solve(Path filePath, boolean isPart2) throws IOException {
        try(var lines = Files.lines(filePath)){
            return lines.map(line -> buildLineNumber(line, isPart2)).reduce(0, Integer::sum);
        }
    }

    private static int buildLineNumber(String line, boolean isPart2) {
        int length = line.length();
        int lineNumber = 0;
        for(int characterIndex = 0; characterIndex < length; characterIndex++) {
            var digit = findDigit(line, characterIndex, isPart2);
            if(digit.isPresent()) {
                lineNumber += 10 * digit.get();
                break;
            }
        }
        for(int characterIndex = length-1; characterIndex >=0; characterIndex--) {
            var digit = findDigit(line, characterIndex, isPart2);
            if(digit.isPresent()) {
                lineNumber +=  digit.get();
                break;
            }
        }
        return lineNumber;
    }

    private static Optional<Integer> findDigit(String line, int index, boolean isPart2) {
        if(Character.isDigit(line.charAt(index))) {
            return Optional.of(Character.getNumericValue(line.charAt(index)));
        }
        if(!isPart2) {
            return Optional.empty();
        }
        return findSpelledDigit(line, index);
    }

    private static Optional<Integer> findSpelledDigit(String line, int index) {
        return IntStream.range(0, DIGITS.size())
                .boxed()
                .filter(digitIndex -> line.startsWith(DIGITS.get(digitIndex), index))
                .map(digitIndex -> digitIndex+1)
                .findFirst();
    }
}
