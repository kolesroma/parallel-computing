package com.kpi.kolesnyk.matrix.calculator.impl;

import com.kpi.kolesnyk.matrix.calculator.MatrixCalculatorCommon;
import com.kpi.kolesnyk.matrix.record.Result;

public class MatrixCalculatorNaive extends MatrixCalculatorCommon {
    private final int[][] matrix1;
    private final int[][] matrix2;

    private Result result;

    public MatrixCalculatorNaive(int[][] matrix1, int[][] matrix2) {
        this.matrix1 = matrix1;
        this.matrix2 = matrix2;
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
        int[][] multiplied = new int[rows1][cols2];
        for (int i = 0; i < rows1; i++) {
            for (int j = 0; j < cols2; j++) {
                for (int k = 0; k < cols1; k++) {
                    multiplied[i][j] += matrix1[i][k] * matrix2[k][j];
                }
            }
        }
        result = new Result(multiplied);
        return result;
    }
}
