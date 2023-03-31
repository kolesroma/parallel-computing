package com.kpi.kolesnyk.forkjoin.statistics;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.atomic.AtomicInteger;

public class ForkJoinTextAnalysis extends RecursiveAction {
    private final Map<Integer, AtomicInteger> wordLengthQuantity;
    private final Path path;
    private final long threshold = 10;
    private final long start;
    private final long end;

    public ForkJoinTextAnalysis(Path path) {
        this.wordLengthQuantity = new ConcurrentHashMap<>();
        this.path = path;
        this.start = 0;
        this.end = getLineCount(path);
    }

    private ForkJoinTextAnalysis(Path path, Map<Integer, AtomicInteger> wordLengthQuantity, long start, long end) {
        this.wordLengthQuantity = wordLengthQuantity;
        this.path = path;
        this.start = start;
        this.end = end;
    }

    public void printStatistic() {
        System.out.println(wordLengthQuantity);
    }

    @Override
    protected void compute() {
        long length = end - start;
        if (length <= threshold) {
            fillWordLengthQuantityWithLines();
        } else {
            ForkJoinTask.invokeAll(
                    new ForkJoinTextAnalysis(path, wordLengthQuantity, start, start + length / 2),
                    new ForkJoinTextAnalysis(path, wordLengthQuantity, start + length / 2, end));
        }
    }

    private static long getLineCount(Path path) {
        try (var lines = Files.lines(path)) {
            return lines.count();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void fillWordLengthQuantityWithLines() {
        try (var linesStream = Files.lines(path)) {
            linesStream
                    .skip(start)
                    .limit(end)
                    .flatMap(line -> Arrays.stream(line.split("\\s+")))
                    .map(String::length)
                    .forEach(number -> {
                        wordLengthQuantity.putIfAbsent(number, new AtomicInteger(0));
                        wordLengthQuantity.get(number).incrementAndGet();
                    });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
