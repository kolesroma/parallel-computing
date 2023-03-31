package com.kpi.kolesnyk.forkjoin.statistics;

import java.nio.file.Path;
import java.util.concurrent.ForkJoinPool;

public class TextAnalysisMain {
    public static void main(String[] args) {
        String pathToFile = "src/main/resources/folder/file.txt";
        var task = new ForkJoinTextAnalysis(Path.of(pathToFile));
        try (var pool = new ForkJoinPool()) {
            pool.invoke(task);
        }
        task.printStatistic();
    }
}
