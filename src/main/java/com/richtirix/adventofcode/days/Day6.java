package com.richtirix.adventofcode.days;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import static java.util.function.Predicate.not;

public class Day6 {
    public static final Pattern PATTERN_FOR_TIME = Pattern.compile("Time:(?<times>.*)");
    public static final Pattern PATTERN_FOR_DISTANCE = Pattern.compile("Distance: (?<recordDistances>.*)");

    public static long solve(Path filePath, boolean isPart2) throws IOException {
        List<String> lines = Files.readAllLines(filePath);
        var matcherForTime = PATTERN_FOR_TIME.matcher(lines.get(0));
        var matcherForDistance = PATTERN_FOR_DISTANCE.matcher(lines.get(1));
        if(!matcherForTime.find() || !matcherForDistance.find()){
            return 0;
        }
        ListRaces listRaces = retrieveRaces(matcherForTime, matcherForDistance, !isPart2);
        return IntStream.range(0, listRaces.times.size())
                .boxed()
                .map(listRaces::getRace)
                .map(Race::numberOfWaysOfWinning)
                .reduce(1L, (a, b) -> a*b);
    }

    private static ListRaces retrieveRaces(Matcher matcherForTime, Matcher matcherForDistance, boolean isPart1) {
        final String times = matcherForTime.group("times");
        final String recordDistances = matcherForDistance.group("recordDistances");
        if(isPart1) {
            return new ListRaces(
                Arrays.stream(times.split(" ")).filter(not((String::isBlank))).map(Long::parseLong).toList(),
                Arrays.stream(recordDistances.split(" ")).filter(not((String::isBlank))).map(Long::parseLong).toList()
            );
        }
        return new ListRaces(
            List.of(Long.parseLong(times.replace(" ", ""))),
            List.of(Long.parseLong(recordDistances.replace(" ", "")))
        );
    }

    private record Race(long time, long recordDistance){
        long numberOfWaysOfWinning() {
            return LongStream.range(1, time).filter(
                i -> i * (time - i) > recordDistance
            ).count();
        }
    }

    private record ListRaces(List<Long> times, List<Long> recordDistances) {
        Race getRace(int raceIndex) {
            return new Race(times.get(raceIndex), recordDistances.get(raceIndex));
        }
    }
}
