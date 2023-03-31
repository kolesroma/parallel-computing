package com.kpi.kolesnyk.forkjoin.samewords;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.atomic.AtomicInteger;

public class ForkJoinSameWord extends RecursiveAction {
    private final Set<String> sameWords;
    private final Path path;
    private final long threshold = 10;
    private final long start;
    private final long end;

    public ForkJoinSameWord(Path path) {
        this.sameWords = new HashSet<>();
        this.path = path;
        this.start = 0;
        this.end = getLineCount(path);
    }

    private ForkJoinSameWord(Path path, Set<String> sameWords, long start, long end) {
        this.sameWords = sameWords;
        this.path = path;
        this.start = start;
        this.end = end;
    }

    public Set<String> getSameWords() {
        return sameWords;
    }

    @Override
    protected void compute() {
        long length = end - start;
        if (length <= threshold) {
            findSameWords();
        } else {
            ForkJoinTask.invokeAll(
                    new ForkJoinSameWord(path, sameWords, start, start + length / 2),
                    new ForkJoinSameWord(path, sameWords, start + length / 2, end));
        }
    }

    private static long getLineCount(Path path) {
        try (var lines = Files.lines(path)) {
            return lines.count();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void findSameWords() {
        try (var linesStream = Files.lines(path)) {
            linesStream
                    .skip(start)
                    .limit(end)
                    .flatMap(line -> Arrays.stream(line.split("\\s+")))
                    .forEach(sameWords::add);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
