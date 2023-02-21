package com.kpi.kolesnyk.matrix;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Stripped {

    public static void main(String[] args) {
        int[][] matrix1 = {{1, 2, 3}, {4, 5, 6}};
        int[][] matrix2 = {{7, 8}, {9, 10}, {11, 12}};
        int[][] result = matrixMultiplication(matrix1, matrix2, 1);
        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result[0].length; j++) {
                System.out.print(result[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static int[][] matrixMultiplication(int[][] A, int[][] B, int num_stripes) {
        int m = A.length; // number of rows in A
        int n = A[0].length; // number of columns in A
        int p = B[0].length; // number of columns in B
        int row2 = B.length; // number of rows in B

        if (n != row2) {
            throw new IllegalArgumentException("Matrices cannot be multiplied - invalid dimensions");
        }

        int[][] C = new int[m][p];

        int stripe_size = m / num_stripes;

        ExecutorService executor = Executors.newFixedThreadPool(num_stripes);

        for (int i = 0; i < num_stripes; i++) {
            int start = i * stripe_size;
            int end = (i == num_stripes - 1) ? m : (i + 1) * stripe_size;
            executor.execute(new MatrixMultiplicationTask(A, B, C, start, end));
        }

        executor.shutdown();
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            // handle interruption
        }

        return C;
    }

    private static class MatrixMultiplicationTask implements Runnable {
        private int[][] A;
        private int[][] B;
        private int[][] C;
        private int start;
        private int end;

        public MatrixMultiplicationTask(int[][] A, int[][] B, int[][] C, int start, int end) {
            this.A = A;
            this.B = B;
            this.C = C;
            this.start = start;
            this.end = end;
        }

        @Override
        public void run() {
            int n = A.length;

            for (int i = start; i < end; i++) {
                for (int j = 0; j < n; j++) {
                    int sum = 0;
                    for (int k = 0; k < B.length; k++) {
                        sum += A[i][k] * B[k][j];
                    }
                    C[i][j] = sum;
                }
            }
        }
    }

}
