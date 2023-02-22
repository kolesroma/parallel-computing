package com.kpi.kolesnyk.matrix.thread;

public class ThreadStriped implements Runnable {
    private final int[][] matrix1;
    private final int[][] matrix2;
    private final int[][] multiplied;
    private final int start;
    private final int end;

    public ThreadStriped(int[][] matrix1, int[][] matrix2, int[][] multiplied, int start, int end) {
        this.matrix1 = matrix1;
        this.matrix2 = matrix2;
        this.multiplied = multiplied;
        this.start = start;
        this.end = end;
    }

    @Override
    public void run() {
        int n = matrix1.length;
        for (int i = start; i < end; i++) {
            for (int j = 0; j < n; j++) {
                int sum = 0;
                for (int k = 0; k < matrix2.length; k++) {
                    sum += matrix1[i][k] * matrix2[k][j];
                }
                multiplied[i][j] = sum;
            }
        }
    }
}
