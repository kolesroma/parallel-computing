package com.kpi.kolesnyk.forkjoin.findfile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

public class FindFileService {
    public static final String KEYWORD = "information technology";

    private final Path path;
    private final ExecutorService executor;

    public FindFileService(Path path) {
        if (!path.toFile().isDirectory()) {
            throw new IllegalArgumentException("path should be folder");
        }
        this.path = path;
        this.executor = new ForkJoinPool();
    }

    public void iterateFolder() {
        iterateFolder(path);
    }

    private void iterateFolder(Path path) {
        for (var file : path.toFile().listFiles()) {
            System.out.printf("reading file [%s]%n", file.getName());
            if (file.isDirectory()) {
                executor.submit(() -> iterateFolder(file.toPath()));
            } else {
                findKeywordInFile(file);
            }
        }
        try {
            executor.awaitTermination(3, TimeUnit.SECONDS);
            executor.shutdown();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static void findKeywordInFile(File file) {
        try (var linesStream = Files.lines(file.toPath())) {
            linesStream
                    .filter(line -> line.contains(KEYWORD))
                    .forEach(line -> System.out.printf("Found keyword in file [%s] in line [%s]%n",
                            file.getName(), line));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
