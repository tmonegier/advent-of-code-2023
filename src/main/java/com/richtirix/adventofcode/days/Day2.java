package com.richtirix.adventofcode.days;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class Day2 {
    private static final String COLOR = "color", BLUE = "blue", RED = "red", GREEN = "green";
    private static final Map<String, Integer> maxima = Map.of(
        BLUE, 14, RED, 12, GREEN, 13
    );
    private static final Pattern
            PATTERN_FOR_GAME_ID = Pattern.compile("Game (?<gameId>\\d*):*"),
            PATTERN_FOR_COLOR = Pattern.compile("(?<numberOfCubes>\\d*) (?<color>blue|red|green)");

    public static long solve(Path path, boolean isPart2) throws IOException {
        return Files.lines(path).map(
            line -> {
                var matcherForGameId = PATTERN_FOR_GAME_ID.matcher(line);
                if(!matcherForGameId.find()) {
                    return 0;
                }
                return calculateLineResult(line, !isPart2, Integer.parseInt(matcherForGameId.group("gameId")));
            }
        ).reduce(0, Integer::sum);
    }

    private static int calculateLineResult(String line, boolean isPart1, Integer gameId) {
        var matcherForColors = PATTERN_FOR_COLOR.matcher(line);
        var minima = new HashMap<String, Integer>(){{
            this.put(BLUE, 0);
            this.put(RED, 0);
            this.put(GREEN, 0);
        }};

        while(matcherForColors.find()) {
            int numberOfCubes = Integer.parseInt(matcherForColors.group("numberOfCubes"));
            String colorType = matcherForColors.group(COLOR);
            if(isColorAboveLimits(colorType,numberOfCubes, isPart1)) {
                return 0;
            }
            minima.put(colorType, Math.max(numberOfCubes, minima.get(colorType)));
        }
        if(isPart1) {
            return gameId;
        }
        return minima.values().stream().reduce(1, (a, b) -> a * b);
    }

    private static boolean isColorAboveLimits(String colorType, int currentColor, boolean isPart1) {
        return isPart1 && currentColor > maxima.get(colorType);
    }
}
