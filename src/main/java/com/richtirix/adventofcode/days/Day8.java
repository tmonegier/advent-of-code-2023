package com.richtirix.adventofcode.days;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Pattern;

import static java.nio.file.Files.readAllLines;

public class Day8 {

    private static final Pattern NODE_PATTERN = Pattern.compile("(?<key>.{3}) = \\((?<left>.{3}), (?<right>.{3})\\)");

    public static long solve(Path filePath, boolean isPart2) throws IOException {
        List<String> lines = readAllLines(filePath);
        String directions = lines.getFirst();
        Map<String, Node> nodesMap = new HashMap<>();
        Set<String> startingNodes = new HashSet<>();
        startingNodes.add("AAA");

        buildNodeMapAndStartingPoints(isPart2, lines, nodesMap, startingNodes);
        return calculateResult(startingNodes, directions, nodesMap);
    }

    private static void buildNodeMapAndStartingPoints(boolean isPart2, List<String> lines, Map<String, Node> nodesMap, Set<String> startingNodes) {
        for(String line: lines) {
            var matcher = NODE_PATTERN.matcher(line);
            if(matcher.find()) {
                String key = matcher.group("key");
                nodesMap.put(key, new Node(matcher.group("left"), matcher.group("right")));
                if(isPart2 && key.charAt(2) == 'A') {
                    startingNodes.add(key);
                }
            }
        }
    }

    private static Long calculateResult(Set<String> startingNodes, String directions, Map<String, Node> nodesMap) {
        return startingNodes.stream().map(
            startingNode -> {
                String currentNodeKey = startingNode;
                long step = 0L;
                do {
                    currentNodeKey = choseNextNode(directions, nodesMap, step, currentNodeKey);
                    step++;
                } while (currentNodeKey.charAt(2) != 'Z');
                return step;
            }
        ).reduce(1L, Day8::lcm);
    }

    private static String choseNextNode(String directions, Map<String, Node> nodesMap, long step, String currentNodeKey) {
        if (directions.charAt((int) (step % directions.length())) == 'L') {
            currentNodeKey = nodesMap.get(currentNodeKey).left;
        } else {
            currentNodeKey = nodesMap.get(currentNodeKey).right;
        }
        return currentNodeKey;
    }

    private record Node(String left, String right){}

    private static long lcm(long a, long b) {
        return Math.abs(a * b) / gcd(a, b);
    }

    // Function to calculate the GCD using Euclidean algorithm
    private static long gcd(long a, long b) {
        if (b == 0) {
            return a;
        }
        return gcd(b, a % b);
    }
}
