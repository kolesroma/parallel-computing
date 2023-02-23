package com.kpi.kolesnyk.matrix;

import com.kpi.kolesnyk.matrix.calculator.MatrixCalculator;
import com.kpi.kolesnyk.matrix.calculator.impl.MatrixCalculatorFox;
import com.kpi.kolesnyk.matrix.calculator.impl.MatrixCalculatorNaive;
import com.kpi.kolesnyk.matrix.calculator.impl.MatrixCalculatorStriped;

import java.time.Duration;
import java.time.Instant;

public class Main {
    public static void main(String[] args) {
        int[][] matrix1 = {{1, 2, 3}, {4, 5, 6}};
        int[][] matrix2 = {{7, 8}, {9, 10}, {11, 12}};

        Instant start;
        Instant finish;

        var matrixCalculatorNaive = new MatrixCalculatorNaive(matrix1, matrix2);
        var matrixCalculatorFox = new MatrixCalculatorFox(matrix1, matrix2, 2);
        var matrixCalculatorStriped = new MatrixCalculatorStriped(matrix1, matrix2, 1);

        start = Instant.now();
        multiplyAndPrint(matrixCalculatorNaive);
        finish = Instant.now();
        printResultsBetweenIntervals(start, finish);

        start = Instant.now();
        multiplyAndPrint(matrixCalculatorFox);
        finish = Instant.now();
        printResultsBetweenIntervals(start, finish);

        start = Instant.now();
        multiplyAndPrint(matrixCalculatorStriped);
        finish = Instant.now();
        printResultsBetweenIntervals(start, finish);
    }

    private static void printResultsBetweenIntervals(Instant start, Instant finish) {
        System.out.println("time = " + Duration.between(start, finish).toMillis() + " ms");
    }

    private static void multiplyAndPrint(MatrixCalculator matrixCalculator) {
        matrixCalculator
                .multiply()
                .print();
    }
}
