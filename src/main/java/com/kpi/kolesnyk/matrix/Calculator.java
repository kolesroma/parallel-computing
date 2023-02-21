package com.kpi.kolesnyk.matrix;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Calculator {
    public static void main(String[] args) {
        int[][] matrix1 = {{1, 2, 3}, {4, 5, 6}};
        int[][] matrix2 = {{7, 8}, {9, 10}, {11, 12}};

        MatrixCalculator matrixCalculatorNaive = new MatrixCalculatorNaive(matrix1, matrix2);
//        matrixCalculatorNaive.multiply();
//        matrixCalculatorNaive.print();

    }

}
