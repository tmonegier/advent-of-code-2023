package org.example.days;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;

public class Day4 {

    public static final Pattern PATTERN_FOR_LINE = Pattern.compile(".*: (?<winning>.*)\\|(?<actual>.*)");

    public static long solve(Path filePath, boolean isPart2) throws IOException {
        int res = 0;
        boolean isPart1 = !isPart2;

        List<String> lines = Files.readAllLines(filePath);
        int[] numberOfCopies = new int[lines.size()];

        for (int lineIndex = 0; lineIndex < lines.size(); lineIndex++) {
            String line = lines.get(lineIndex);
            var matcher = PATTERN_FOR_LINE.matcher(line);

            if(matcher.find()) {
                Set<Integer> winningNumbers = Arrays.stream(matcher.group("winning").split(" "))
                        .filter(not(String::isEmpty))
                        .map(Integer::parseInt)
                        .collect(Collectors.toSet());

                var streamOfActualWinningNumber =  Arrays.stream(matcher.group("actual").split(" "))
                        .filter(not(String::isEmpty))
                        .map(Integer::parseInt)
                        .filter(winningNumbers::contains);

                if(isPart1) {
                    res += calculateScoreOfCurrentCard(streamOfActualWinningNumber);
                } else {
                    long numberOfActualNumbersWinning = streamOfActualWinningNumber.count();
                    do {
                        res++;
                        for(int j = lineIndex+1; j <= lineIndex+numberOfActualNumbersWinning; j++) {
                            numberOfCopies[j]++;
                        }
                        numberOfCopies[lineIndex] --;
                    } while (numberOfCopies[lineIndex] >= 0);
                }
            }
        }
        return res;
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
