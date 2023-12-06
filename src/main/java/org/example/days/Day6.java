package org.example.days;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Pattern;
import java.util.*;
import java.util.stream.IntStream;

import static java.util.function.Predicate.not;

public class Day6 {
    public static final Pattern PATTERN_FOR_TIME = Pattern.compile("Time:(?<times>.*)");
    public static final Pattern PATTERN_FOR_DISTANCE = Pattern.compile("Distance: (?<recordDistances>.*)");

    private record Race(long time, long recordDistance){
        long numberOfWaysOfWinning() {
            int count = 0;
            for(int i = 1; i< time; i++) {
                if(i * (time - i) > recordDistance) {
                    count++;
                }
            }
            return count;
        }
    }

    public static long solve(Path filePath, boolean isPart2) throws IOException {
        List<String> lines = Files.readAllLines(filePath);
        var matcherFortTime = PATTERN_FOR_TIME.matcher(lines.get(0));
        var matcherForDistance = PATTERN_FOR_DISTANCE.matcher(lines.get(1));
        if(!matcherFortTime.find() || !matcherForDistance.find()){
            return 0;
        }

        List<Long> times, recordDistance;

        if(!isPart2) {
            times = Arrays.stream(matcherFortTime.group("times").split(" ")).filter(not((String::isBlank))).map(Long::parseLong).toList() ;
            recordDistance = Arrays.stream(matcherForDistance.group("recordDistances").split(" ")).filter(not((String::isBlank))).map(Long::parseLong).toList() ;
        } else {
            times = List.of(Long.parseLong(matcherFortTime.group("times").replace(" ", "")));
            recordDistance = List.of(Long.parseLong(matcherForDistance.group("recordDistances").replace(" ", "")));
        }

        assert times.size() == recordDistance.size();
        return IntStream.range(0, times.size())
                .boxed()
                .map(
                    i -> new Race(times.get(i), recordDistance.get(i)).numberOfWaysOfWinning()
                )
                .reduce(1L, (a, b) -> a*b);
    }
}
