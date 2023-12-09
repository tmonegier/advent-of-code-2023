package com.richtirix.adventofcode;

import com.richtirix.adventofcode.days.Day4;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) throws URISyntaxException, IOException {
        long solution = Day4.solve(Path.of(Main.class.getClassLoader().getResource("day_4").toURI()), true);
        System.out.println(solution);

    }
}