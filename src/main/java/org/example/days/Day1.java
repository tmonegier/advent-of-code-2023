package org.example.days;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Pattern;

public class Day1 {

    private static List<String> digits = List.of(
        "one", "two", "three", "four", "five", "six", "seven", "eight", "nine"
    );

    public static long solve(Path filePath, boolean isPart2) throws IOException {
        try(var lines = Files.lines(filePath)){
            return lines.map(line -> calculateLineNumber(line, isPart2)).reduce(0, Integer::sum);
        }
    }

    private static int calculateLineNumber(String line, boolean isPart2) {
        int length = line.length();
        int number = 0;
        for(int characterIndex = 0; characterIndex < length; characterIndex++) {
            int digit = findPossibleDigit(line, characterIndex, isPart2);
            if(digit != 0) {
                number += 10 * digit;
                break;
            }
        }
        for(int characterIndex = length-1; characterIndex >=0; characterIndex--) {
            int digit = findPossibleDigit(line, characterIndex, isPart2);
            if(digit != 0) {
                number +=  digit;
                break;
            }
        }
        return number;
    }

    private static int findPossibleDigit(String line, int index, boolean isPart2) {
        if (isPart2) {
            Integer i = searchDigitWrittenWithLetters(line, index);
            if (i != null) return i;
        }
        return searchDigit(line, index);
    }

    private static int searchDigit(String line, int index) {
        if(Character.isDigit(line.charAt(index))) {
            return Character.getNumericValue(line.charAt(index));
        }
        return 0;
    }

    private static Integer searchDigitWrittenWithLetters(String line, int index) {
        for(int digitIndex = 0; digitIndex < digits.size(); digitIndex++) {
            String digit = digits.get(digitIndex);
            if(index + digit.length() <= line.length() && line.startsWith(digit, index)) {
                return digitIndex + 1;
            }
        }
        return null;
    }
}
