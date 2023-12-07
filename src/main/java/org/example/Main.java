package org.example;

import org.example.days.day7.Day7;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) throws URISyntaxException, IOException {
        long solution = Day7.solve(Path.of(Main.class.getClassLoader().getResource("day_7").toURI()), false);
        System.out.println(solution);

    }
}