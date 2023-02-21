package com.kpi.kolesnyk.matrix;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MatrixCalculatorFox implements MatrixCalculator {
    private int[][] A;
    private int[][] B;
    private int blockSize;

    private Result result;

    public MatrixCalculatorFox(int[][] a, int[][] b, int blockSize) {
        A = a;
        B = b;
        this.blockSize = blockSize;
    }

    @Override
    public Result multiply() {
        int n = A.length;
        int m = B[0].length;
        int p = B.length;
        int col1 = A[0].length;

        int[][] C = new int[n][m];

        if (col1 != p) {
            throw new IllegalArgumentException("Matrices cannot be multiplied - invalid dimensions");
        }
        if (blockSize > n || blockSize > m) {
            throw new IllegalArgumentException("Block size should be a divisor of the matrix dimensions");
        }

        // Creating a thread pool
        ExecutorService executor = Executors.newFixedThreadPool(n * m / blockSize / blockSize);

        // Performing the Fox algorithm
        for (int k = 0; k < n; k += blockSize) {
            for (int j = 0; j < m; j += blockSize) {
                for (int i = 0; i < n; i += blockSize) {
                    final int startI = i;
                    final int endI = Math.min(i + blockSize, n);
                    final int startJ = j;
                    final int endJ = Math.min(j + blockSize, m);
                    final int startK = k;
                    final int endK = Math.max(k + blockSize, p);

                    // Submitting a task for each block of C
                    executor.submit(() -> {
                        for (int x = startI; x < endI; x++) {
                            for (int y = startJ; y < endJ; y++) {
                                int sum = 0;
                                for (int z = startK; z < endK; z++) {
                                    sum += A[x][z] * B[z][y];
                                }
                                C[x][y] += sum;
                            }
                        }
                    });
                }
            }
        }

        // Shutting down the thread pool and waiting for all tasks to complete
        executor.shutdown();
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        result = new Result(C);
        return result;
    }

    @Override
    public void print() {
    }
}
