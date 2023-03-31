package com.kpi.kolesnyk.forkjoin.findfile;

import java.nio.file.Path;

public class FindFileMain {
    public static void main(String[] args) {
        String pathToFile = "src/main/resources";
        new FindFileService(Path.of(pathToFile)).iterateFolder();
    }
}
