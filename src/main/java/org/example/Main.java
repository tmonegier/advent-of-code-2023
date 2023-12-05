package org.example;

import org.example.days.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) throws URISyntaxException, IOException {
        long solution = Day5.solve(Path.of(Main.class.getClassLoader().getResource("day_5").toURI()), true);
        System.out.println(solution);

    }
}