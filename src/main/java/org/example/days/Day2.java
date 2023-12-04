package org.example.days;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day2 {

    public static final String COLOR = "color";
    public static final String BLUE = "blue";
    public static final String RED = "red";
    public static final String GREEN = "green";
    public static final Pattern PATTERN_FOR_GAME_ID = Pattern.compile("Game (?<gameId>\\d*):*");
    public static final Pattern PATTERN_FOR_COLOR = Pattern.compile("(\\d*) (?<color>blue|red|green)");

    public static long solve(Path path, boolean isPart2) throws IOException {
        boolean isPart1 = !isPart2;
        return Files.lines(path).map(
            line -> {
                var matcherForGameId = PATTERN_FOR_GAME_ID.matcher(line);

                if(matcherForGameId.find()) {
                    int currentColor, minRed =0, minBlue =0, minGreen =0;
                    var matcherForColors = PATTERN_FOR_COLOR.matcher(line);

                    while(matcherForColors.find()) {
                        currentColor = Integer.parseInt(matcherForColors.group(1));
                        if( isPart1 && isColorAboveLimits(matcherForColors, currentColor)
                        ) {
                            return 0;
                        }
                        if(matcherForColors.group(COLOR).equals(BLUE) && currentColor > minBlue) {
                            minBlue = currentColor;
                        }
                        if(matcherForColors.group(COLOR).equals(RED) && currentColor > minRed) {
                            minRed = currentColor;
                        }
                        if(matcherForColors.group(COLOR).equals(GREEN) && currentColor > minGreen) {
                            minGreen = currentColor;
                        }
                    }
                    if(isPart1) {
                        return Integer.parseInt(matcherForGameId.group("gameId"));
                    }
                    return minRed*minBlue*minGreen;
                }
                return 0;
            }
        ).reduce(0, Integer::sum);
    }

    private static boolean isColorAboveLimits(Matcher matcherForColors, int currentColor) {
        return (matcherForColors.group(COLOR).equals(BLUE) && currentColor > 14) ||
                (matcherForColors.group(COLOR).equals(RED) && currentColor > 12) ||
                (matcherForColors.group(COLOR).equals(GREEN) && currentColor > 13);
    }
}
