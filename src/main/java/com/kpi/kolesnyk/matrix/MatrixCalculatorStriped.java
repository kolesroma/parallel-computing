package com.kpi.kolesnyk.matrix;

public class MatrixCalculatorStriped implements MatrixCalculator {
    private final int[][] matrix1;
    private final int[][] matrix2;



    private Result result;

    public MatrixCalculatorStriped(int[][] matrix1, int[][] matrix2) {
        this.matrix1 = matrix1;
        this.matrix2 = matrix2;
    }

    @Override
    public Result multiply() {
        return null;
    }

    @Override
    public void print() {
    }
}
