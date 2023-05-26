package com.kpi.kolesnyk.gmdh;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Stream;

public class GmdhMain {
    public static void main(String[] args) {
        double[][] tableData = readDataFromFile(Path.of("src/main/resources/gmdh/inputDataBasedFunctions5.txt"));

        Instant start = Instant.now();
        Gmdh gmdh = new Gmdh(tableData, new ForkJoinPool());
        List<Matrix> allCandidates = gmdh.getAllCandidates();
        System.out.println("Total models = " + allCandidates.size());
        gmdh.printApprovedCandidateStatistics();
        System.out.printf("Time elapsed = %d ms", Duration.between(start, Instant.now()).toMillis());
    }

    static public double[][] readDataFromFile(Path path) {
        try (Stream<String> lines = Files.lines(path)) {
            return lines.map((line) -> line.trim().split("\\s+"))
                    .map((word) -> Stream.of(word).mapToDouble(Double::parseDouble).toArray())
                    .toArray(double[][]::new);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
