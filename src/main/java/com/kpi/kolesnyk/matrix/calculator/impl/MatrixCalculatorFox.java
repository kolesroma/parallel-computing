package com.kpi.kolesnyk.matrix.calculator.impl;

import com.kpi.kolesnyk.matrix.calculator.MatrixCalculatorCommon;
import com.kpi.kolesnyk.matrix.record.Result;
import com.kpi.kolesnyk.matrix.thread.ThreadFox;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MatrixCalculatorFox extends MatrixCalculatorCommon {
    private final int[][] matrix1;
    private final int[][] matrix2;
    private final int blockSize;

    private Result result;

    public MatrixCalculatorFox(int[][] matrix1, int[][] matrix2, int blockSize) {
        this.matrix1 = matrix1;
        this.matrix2 = matrix2;
        this.blockSize = blockSize;
    }

    @Override
    public Result multiply() {
        int rows1 = matrix1.length;
        int cols1 = matrix1[0].length;
        int rows2 = matrix2.length;
        int cols2 = matrix2[0].length;
        if (cols1 != rows2) {
            throw new IllegalArgumentException("Matrices cannot be multiplied - invalid dimensions");
        }
        if (blockSize > rows1 || blockSize > cols2) {
            throw new IllegalArgumentException("Block size should be a divisor of the matrix dimensions");
        }
        int[][] multiplied = new int[rows1][cols2];
        calculateInParallel(rows1, rows2, cols2, multiplied);
        result = new Result(multiplied);
        return result;
    }

    private void calculateInParallel(int rows1, int rows2, int cols2, int[][] multiplied) {
        try (ExecutorService executor = Executors.newCachedThreadPool()) {
            for (int k = 0; k < rows1; k += blockSize) {
                for (int j = 0; j < cols2; j += blockSize) {
                    for (int i = 0; i < rows1; i += blockSize) {
                        executor.submit(
                                new ThreadFox(i, j, k, blockSize, rows1, rows2, cols2, matrix1, matrix2, multiplied)
                        );
                    }
                }
            }
            try {
                executor.shutdown();
                executor.awaitTermination(180, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
