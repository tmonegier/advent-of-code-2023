package org.example.days;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Pattern;

public class Day2 {

    public static final String COLOR = "color";
    public static final String BLUE = "blue";
    public static final String RED = "red";
    public static final String GREEN = "green";

    public static long solvePart2(Path path) throws IOException {
        var patternForColor = Pattern.compile("(\\d*) (?<color>blue|red|green)");

        int sumsOfPower = 0;

        for (String line : Files.readAllLines(path)) {
            int currentColor = 0, minRed =0, minBlue =0, minGreen =0;
            var matcherForColor = patternForColor.matcher(line);

            while(matcherForColor.find()) {
                currentColor = Integer.parseInt(matcherForColor.group(1));
                if(matcherForColor.group(COLOR).equals(BLUE) && currentColor > minBlue) {
                    minBlue = currentColor;
                }
                if(matcherForColor.group(COLOR).equals(RED) && currentColor > minRed) {
                    minRed = currentColor;
                }
                if(matcherForColor.group(COLOR).equals(GREEN) && currentColor > minGreen) {
                    minGreen = currentColor;
                }
            }
            sumsOfPower += minRed*minBlue*minGreen;
        }

        return sumsOfPower;
    }
}
