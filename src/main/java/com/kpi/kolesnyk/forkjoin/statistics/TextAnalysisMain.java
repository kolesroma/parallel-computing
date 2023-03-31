package com.kpi.kolesnyk.forkjoin.statistics;

import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ForkJoinPool;

public class TextAnalysisMain {
    public static void main(String[] args) {
        Instant start = Instant.now();
        String pathToFile = "src/main/resources/folder/big.txt";
        var task = new ForkJoinTextAnalysis(Path.of(pathToFile));
        try (var pool = new ForkJoinPool()) {
            pool.invoke(task);
        }
        task.printStatistic();
        System.out.println("time = " + Duration.between(start, Instant.now()).toMillis() + " ms");
    }
}
