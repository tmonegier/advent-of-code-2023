package org.example.days.day7;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Pattern;

import static java.util.function.Predicate.not;

public class Day7 {
    public static final Pattern PATTERN_HAND = Pattern.compile("(?<cards>.{5}) (?<bid>\\d+)");

    public static long solve(Path filePath, boolean isPart2) throws IOException {
        AtomicLong index = new AtomicLong(1);
        return Files.lines(filePath).map(line -> {
            var matcher = PATTERN_HAND.matcher(line);
            if(matcher.find()) {
                return new Hand(
                    matcher.group("cards"),
                    Long.parseLong(matcher.group("bid")),
                    isPart2
                );
            }
            return null;
        })
        .filter(not(Objects::isNull))
        .sorted()
        .map(
            hand -> index.getAndIncrement() * hand.getBid()
        ).reduce(0L, Long::sum);
    }
}
