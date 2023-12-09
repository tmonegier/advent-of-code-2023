package org.example.days;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;

public class Day4 {

    public static final Pattern PATTERN_FOR_LINE = Pattern.compile(".*: (?<winning>.*)\\|(?<actual>.*)");

    public static long solve(Path filePath, boolean isPart2) throws IOException {
        List<String> lines = Files.readAllLines(filePath);
        int[] numberOfCopies = new int[lines.size()];

        return IntStream.range(0, lines.size())
            .map(
                lineIndex -> {
                    String line = lines.get(lineIndex);
                    var matcher = PATTERN_FOR_LINE.matcher(line);
                    if(!matcher.find()) {
                        return 0;
                    }
                    return calculateResult(lineIndex, matcher, !isPart2, numberOfCopies);
                }
            )
            .reduce(0, Integer::sum);
    }

    private static Integer calculateResult(Integer lineIndex, Matcher matcher, boolean isPart1, int[] numberOfCopies) {
        Set<Integer> winningNumbers = retrieveWinningNumbers(matcher);
        var actualWinningNumbers = retrieveActualWinningNumbers(matcher, winningNumbers);
        if(isPart1) {
            return calculateScoreOfCurrentCard(actualWinningNumbers);
        }
        processWinningCard(lineIndex, numberOfCopies, actualWinningNumbers.count());
        return numberOfCopies[lineIndex]+1;
    }

    private static Stream<Integer> retrieveActualWinningNumbers(Matcher matcher, Set<Integer> winningNumbers) {
        return Arrays.stream(matcher.group("actual").split(" "))
                .filter(not(String::isEmpty))
                .map(Integer::parseInt)
                .filter(winningNumbers::contains);
    }

    private static Set<Integer> retrieveWinningNumbers(Matcher matcher) {
        return Arrays.stream(matcher.group("winning").split(" "))
                .filter(not(String::isEmpty))
                .map(Integer::parseInt)
                .collect(Collectors.toSet());
    }

    private static void processWinningCard(Integer lineIndex, int[] numberOfCopies, long numberOfActualNumbersWinning) {
        for(int j = 1; j <= numberOfActualNumbersWinning; j++) {
            numberOfCopies[lineIndex + j] += numberOfCopies[lineIndex]+1;
        }
    }

    private static Integer calculateScoreOfCurrentCard(Stream<Integer> streamOfActualWinningNumber) {
        return streamOfActualWinningNumber.reduce(0, (a, b) -> {
            if (a == 0) {
                return 1;
            }
            return a << 1;
        });
    }
}
