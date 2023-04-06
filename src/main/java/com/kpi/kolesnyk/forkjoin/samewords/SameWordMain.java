package com.kpi.kolesnyk.forkjoin.samewords;

import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ForkJoinPool;

public class SameWordMain {
    public static void main(String[] args) {
        Instant start = Instant.now();
        var task1 = new ForkJoinSameWord(Path.of("src/main/resources/folder/file.txt"));
        var task2 = new ForkJoinSameWord(Path.of("src/main/resources/folder/other.txt"));
        try (var pool = new ForkJoinPool(2)) {
            pool.invoke(task1);
            pool.invoke(task2);
        }
        var words1 = task1.getSameWords();
        var words2 = task2.getSameWords();
        words1.parallelStream()
                .filter(words2::contains)
                .forEach(System.out::println);
        System.out.println("time = " + Duration.between(start, Instant.now()).toMillis() + " ms");
    }
}
