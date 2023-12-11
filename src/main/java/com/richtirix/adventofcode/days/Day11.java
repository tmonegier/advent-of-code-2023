package com.richtirix.adventofcode.days;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class Day11 {

    public static long solve(Path filePath, boolean isPart2) throws IOException {
        List<String> lines = Files.readAllLines(filePath);
        if (lines.isEmpty()) {
            return 0;
        }
        Boolean[] emptyRows = new Boolean[lines.size()];
        Arrays.fill(emptyRows, true);
        Boolean[] emptyColumns = new Boolean[lines.getFirst().length()];
        Arrays.fill(emptyColumns, true);

        List<GalaxyCoordinates> galaxiesCoordinates = buildGalaxyCoordinates(lines, emptyColumns, emptyRows);
        galaxiesCoordinates =  buildRealCoordinates(galaxiesCoordinates, emptyRows, emptyColumns, isPart2?1_000_000:2);
        return getSumOfShortestPaths(galaxiesCoordinates);
    }

    private static long getSumOfShortestPaths(List<GalaxyCoordinates> galaxiesCoordinates) {
        long sumOfShortestPaths = 0L;
        for (int galaxyIndex = 0; galaxyIndex < galaxiesCoordinates.size(); galaxyIndex++) {
            var galaxy = galaxiesCoordinates.get(galaxyIndex);
            sumOfShortestPaths+=galaxiesCoordinates.stream().skip(galaxyIndex).map(galaxy::shortestPath).reduce(0L, Long::sum);
        }
        return sumOfShortestPaths;
    }

    private static List<GalaxyCoordinates> buildRealCoordinates(List<GalaxyCoordinates> galaxiesCoordinates, Boolean[] emptyRows, Boolean[] emptyColumns, int expansionMultiplier) {
        return galaxiesCoordinates.stream().map(
            galaxyCoordinates -> {
                long xGalaxy = galaxyCoordinates.x;
                long yGalaxy = galaxyCoordinates.y;
                long numberOfEmptyRowsBeforeGalaxy = Arrays.stream(emptyRows).limit(galaxyCoordinates.x).filter(b->b).count();
                long numberOfEmptyColumnsBeforeGalaxy = Arrays.stream(emptyColumns).limit(galaxyCoordinates.y).filter(b->b).count();
                return new GalaxyCoordinates(
                    xGalaxy + numberOfEmptyRowsBeforeGalaxy * (expansionMultiplier - 1),
                    yGalaxy+ numberOfEmptyColumnsBeforeGalaxy * (expansionMultiplier - 1));
            }
        ).toList();
    }

    private static List<GalaxyCoordinates> buildGalaxyCoordinates(List<String> lines, Boolean[] emptyColumns, Boolean[] emptyRows) {
        return IntStream.range(0, lines.size()*lines.getFirst().length()).boxed()
                .map(coordinate -> new int[]{coordinate/lines.size(), coordinate%lines.size()})
                .filter(coordinate -> lines.get(coordinate[0]).charAt(coordinate[1]) == '#')
                .map(coordinate ->  {
                    int x = coordinate[0];
                    int y = coordinate[1];
                    emptyColumns[y] = false;
                    emptyRows[x] = false;
                    return new GalaxyCoordinates(x, y);
                })
                .toList();
    }

    private record GalaxyCoordinates(long x, long y) {
        public long shortestPath(GalaxyCoordinates otherGalaxy) {
            return Math.max(x, otherGalaxy.x) - Math.min(x, otherGalaxy.x)
                    + Math.max(y, otherGalaxy.y) - Math.min(y, otherGalaxy.y);
        }
    }
}
