package org.example.days;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.function.Predicate.not;

public class Day4 {

    public static long solvePart1(Path filePath) throws IOException {
        int sum = 0;
        var pattern = Pattern.compile(".*: (?<winning>.*)\\|(?<actual>.*)");

        for(String line : Files.readAllLines(filePath)) {
            var matcher = pattern.matcher(line);

            if(matcher.find()) {
                Set<Integer> winningNumbers = Arrays
                        .stream(matcher.group("winning").split(" "))
                        .filter(not(String::isEmpty))
                        .map(Integer::parseInt)
                        .collect(Collectors.toSet());
                sum +=  Arrays
                        .stream(matcher.group("actual").split(" "))
                        .filter(not(String::isEmpty))
                        .map(Integer::parseInt)
                        .filter(winningNumbers::contains)
                        .reduce(0, (a,b) -> {
                            if(a == 0) {
                                return 1;
                            }
                            return a*2;
                        });
            }

        }
        return sum;
    }


    public static long solvePart2(Path filePath) throws IOException {
        int numberOfCards = 0;
        var pattern = Pattern.compile(".*: (?<winning>.*)\\|(?<actual>.*)");

        List<String> lines = Files.readAllLines(filePath);
        int[] numberOfCopies = new int[lines.size()];

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            var matcher = pattern.matcher(line);

            if(matcher.find()) {
                Set<Integer> winningNumbers = Arrays
                        .stream(matcher.group("winning").split(" "))
                        .filter(not(String::isEmpty))
                        .map(Integer::parseInt)
                        .collect(Collectors.toSet());
                long numberOfActualNumbersWinning =  Arrays
                        .stream(matcher.group("actual").split(" "))
                        .filter(not(String::isEmpty))
                        .map(Integer::parseInt)
                        .filter(winningNumbers::contains)
                        .count();

                do {
                    numberOfCards++;
                    for(int j = i+1; j <= i+numberOfActualNumbersWinning; j++) {
                        numberOfCopies[j]++;
                    }
                    numberOfCopies[i] --;
                } while (numberOfCopies[i] >= 0);
            }

        }
        return numberOfCards;
    }
}
