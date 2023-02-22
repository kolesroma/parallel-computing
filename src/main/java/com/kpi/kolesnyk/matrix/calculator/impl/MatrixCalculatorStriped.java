package com.kpi.kolesnyk.matrix.calculator.impl;

import com.kpi.kolesnyk.matrix.calculator.MatrixCalculatorCommon;
import com.kpi.kolesnyk.matrix.record.Result;
import com.kpi.kolesnyk.matrix.thread.ThreadStriped;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MatrixCalculatorStriped extends MatrixCalculatorCommon {
    private final int[][] matrix1;
    private final int[][] matrix2;
    private final int stripsQuantity;

    private Result result;

    public MatrixCalculatorStriped(int[][] matrix1, int[][] matrix2, int stripsQuantity) {
        this.matrix1 = matrix1;
        this.matrix2 = matrix2;
        this.stripsQuantity = stripsQuantity;
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
        if (stripsQuantity > rows1 || stripsQuantity > cols2) {
            throw new IllegalArgumentException("Strips quantity should be less than matrix dimensions");
        }
        int[][] multiplied = new int[rows1][cols2];
        calculateInParallel(rows1, multiplied);
        result = new Result(multiplied);
        return result;
    }

    private void calculateInParallel(int rows1, int[][] multiplied) {
        try(ExecutorService executor = Executors.newFixedThreadPool(stripsQuantity)) {
            final int stripeSize = rows1 / stripsQuantity;
            for (int i = 0; i < stripsQuantity; i++) {
                int start = i * stripeSize;
                int end = (i == stripsQuantity - 1) ? rows1 : (i + 1) * stripeSize;
                executor.execute(new ThreadStriped(matrix1, matrix2, multiplied, start, end));
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
