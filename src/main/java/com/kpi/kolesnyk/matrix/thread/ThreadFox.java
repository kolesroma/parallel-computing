package com.kpi.kolesnyk.matrix.thread;

public class ThreadFox implements Runnable {
    private final int i;
    private final int j;
    private final int k;
    private final int blockSize;
    private final int rows1;
    private final int rows2;
    private final int cols2;

    private final int[][] matrix1;
    private final int[][] matrix2;
    private final int[][] multiplied;

    public ThreadFox(int i, int j, int k, int blockSize, int rows1, int rows2, int cols2,
                     int[][] matrix1,int[][] matrix2, int[][] multiplied) {
        this.i = i;
        this.j = j;
        this.k = k;
        this.blockSize = blockSize;
        this.rows1 = rows1;
        this.rows2 = rows2;
        this.cols2 = cols2;
        this.matrix1 = matrix1;
        this.matrix2 = matrix2;
        this.multiplied = multiplied;
    }

    @Override
    public void run() {
        final int endI = Math.min(i + blockSize, rows1);
        final int endJ = Math.min(j + blockSize, cols2);
        final int endK = Math.max(k + blockSize, rows2);
        for (int x = i; x < endI; x++) {
            for (int y = j; y < endJ; y++) {
                int sum = 0;
                for (int z = k; z < endK; z++) {
                    sum += matrix1[x][z] * matrix2[z][y];
                }
                multiplied[x][y] += sum;
            }
        }
    }
}
