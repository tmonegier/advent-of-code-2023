package com.richtirix.adventofcode.days;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.List;

import static java.nio.file.Files.readAllLines;

public class Day8 {

    private static final Pattern NODE_PATTERN = Pattern.compile("(?<key>.{3}) = \\((?<left>.{3}), (?<right>.{3})\\)");

    public static long solve(Path filePath, boolean isPart2) throws IOException {
        List<String> lines = readAllLines(filePath);
        String directions = lines.getFirst();
        Map<String, Node> nodesMap = new HashMap<>();

        for(String line: lines) {
            var matcher = NODE_PATTERN.matcher(line);
            if(matcher.find()) {
                nodesMap.put(matcher.group("key"), new Node(matcher.group("left"), matcher.group("right")));
            }
        }

        String currentNodeKey = "AAA";
        int step = 0;
        while(!currentNodeKey.equals("ZZZ")){
            if(directions.charAt(step%directions.length()) == 'L') {
                currentNodeKey = nodesMap.get(currentNodeKey).left;
            } else {
                currentNodeKey = nodesMap.get(currentNodeKey).right;
            }
            step ++;
        }

        return step;
    }

    private record Node(String left, String right){}
}
