package org.example.days;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Day5 {

    public static final Pattern SEEDS_PATTERN = Pattern.compile("seeds: (?<seeds>.*)");
    public static final Pattern MAP_PATTERN = Pattern.compile("(?<destination>\\d+) (?<origin>\\d+) (?<length>\\d+)");

    private record Range(long rangeInit, long rangeEnd) {
    }

    public static long solve(Path filePath, boolean isPart2) throws IOException {
        long[] seeds = new long[0];
        boolean[] isCurrentMapAlreadyUsed = new boolean[0];
        List<Range> ranges = new ArrayList<>(), newRanges = new ArrayList<>();

        for(String line : Files.readAllLines(filePath)) {
            var seedMatcher = SEEDS_PATTERN.matcher(line);

            if (seedMatcher.find()) {
                seeds = Arrays.stream(seedMatcher.group("seeds").split(" ")).mapToLong(Long::parseLong).toArray();
                isCurrentMapAlreadyUsed = new boolean[seeds.length];
                for(int i = 0; i<seeds.length; i+=2) {
                    ranges.add(new Range(seeds[i],seeds[i] + seeds[i+1] - 1 ));
                }
            } else {
                if(line.isEmpty())  {
                    Arrays.fill(isCurrentMapAlreadyUsed, false);
                    ranges.addAll(newRanges);
                    newRanges = new ArrayList<>();
                }

                var mapMatcher = MAP_PATTERN.matcher(line);
                if(mapMatcher.find()) {
                    long destination = Long.parseLong(mapMatcher.group("destination"));
                    long origin = Long.parseLong(mapMatcher.group("origin"));
                    long length = Long.parseLong(mapMatcher.group("length"));

                    int rangesSize = ranges.size();
                    int rangeIndex = 0;
                    while(rangeIndex < rangesSize) {
                        var range = ranges.get(0);
                        calculateNewRanges(range, origin, destination, length, newRanges, ranges);
                        rangeIndex++;
                        ranges.remove(0);
                    }

                    for (int i = 0; i < seeds.length; i++) {
                        if(seeds[i] >= origin && seeds[i] < origin + length && !isCurrentMapAlreadyUsed[i]) {
                            seeds[i] = seeds[i] + destination - origin;
                            isCurrentMapAlreadyUsed[i] = true;
                        }
                    }
                }
            }
        }

        if(isPart2) {
            return Stream.concat(ranges.stream(), newRanges.stream()).min(Comparator.comparing(range -> range.rangeInit)).map(range -> range.rangeInit).orElse(0L);
        }
        return Arrays.stream(seeds).sorted().findFirst().orElse(0);
    }

    private static void calculateNewRanges(
            Range currentRange,
            long currentMappingOrigin,
            long currentMappingDestination,
            long currentMappingLength,
            List<Range> newRanges,
            List<Range> currentRanges
    ) {

        if (currentRange.rangeInit < currentMappingOrigin && currentRange.rangeEnd >= currentMappingOrigin + currentMappingLength) {
            newRanges.add(new Range(currentMappingDestination, currentMappingDestination + currentMappingLength - 1));
            currentRanges.add(new Range(currentRange.rangeInit, currentMappingOrigin -1));
            currentRanges.add(new Range(currentMappingOrigin + currentMappingLength, currentRange.rangeEnd));
        }
        else if (currentRange.rangeInit < currentMappingOrigin && currentRange.rangeEnd >= currentMappingOrigin) {
            newRanges.add(new Range(currentMappingDestination, currentRange.rangeEnd + currentMappingDestination - currentMappingOrigin));
            currentRanges.add(new Range(currentRange.rangeInit, currentMappingOrigin -1));
        }
        else if (currentRange.rangeInit >= currentMappingOrigin && currentRange.rangeEnd < currentMappingOrigin + currentMappingLength) {
            newRanges.add(new Range(currentRange.rangeInit + currentMappingDestination - currentMappingOrigin, currentRange.rangeEnd + currentMappingDestination - currentMappingOrigin));
        }
        else if (currentRange.rangeInit >= currentMappingOrigin && currentRange.rangeInit < currentMappingOrigin + currentMappingLength && currentRange.rangeEnd >= currentMappingOrigin + currentMappingLength) {
            newRanges.add(new Range(currentRange.rangeInit + currentMappingDestination - currentMappingOrigin, currentMappingDestination + currentMappingLength - 1));
            currentRanges.add(new Range(currentMappingOrigin + currentMappingLength,  currentRange.rangeEnd));
        }
        else {
            currentRanges.add(currentRange);
        }
    }
}
