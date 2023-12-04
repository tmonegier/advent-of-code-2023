package org.example.days;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Day1 {

    private static List<String> digits = List.of(
        "one", "two", "three", "four", "five", "six", "seven", "eight", "nine"
    );

    public static long solvePart1(Path filePath) throws IOException {
        int sum = 0;
        for(String line : Files.readAllLines(filePath)) {
            sum += retrieveLineNumberPart1(line);
        }
        return sum;
    }

    private static int retrieveLineNumberPart1(String line) {
        int length = line.length();
        int number = 0;
        for(int index = 0; index < length; index++) {
            int digit = searchDigitInGivenIndex(line, index);
            if(digit != 0) {
                number += 10 * digit;
                break;
            }
        }
        for(int index = length-1; index >=0; index--) {
            int digit = searchDigitInGivenIndex(line, index);
            if(digit != 0) {
                number +=  digit;
                break;
            }
        }
        return number;
    }

    private static int searchDigitInGivenIndex(String line, int index) {
        if(Character.isDigit(line.charAt(index))) {
            return Character.getNumericValue(line.charAt(index));
        }
        return 0;
    }

    public static long solvePart2(Path filePath) throws IOException {
        int sum = 0;
        for(String line : Files.readAllLines(filePath)) {
            sum += retrieveLineNumberPart2(line);
        }
        return sum;
    }

    private static int retrieveLineNumberPart2(String line) {
        int length = line.length();
        int number = 0;
        for(int j = 0; j < length; j++) {
            int digit = findDigitAtCurrentIndexPart2(line, j);
            if(digit != 0) {
                number += 10 * digit;
                break;
            }
        }
        for(int j = length-1; j >=0; j--) {
            int digit = findDigitAtCurrentIndexPart2(line, j);
            if(digit != 0) {
                number +=  digit;
                break;
            }
        }
        return number;
    }

    private static int findDigitAtCurrentIndexPart2(String line, int index) {
        Integer i = searchLetterDigitInLine(line, index);
        if (i != null) return i;
        return searchDigitInGivenIndex(line, index);
    }

    private static Integer searchLetterDigitInLine(String line, int index) {
        int length = line.length();
        for(int i = 0; i < digits.size(); i++) {
            String digit = digits.get(i);
            if(index + digit.length() <= length && line.substring(index, index + digit.length()).equals(digit)) {
                return i + 1;
            }
        }
        return null;
    }
}
