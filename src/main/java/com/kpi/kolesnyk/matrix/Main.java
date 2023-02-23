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

        measureTimeForMatrixCalculator(new MatrixCalculatorNaive(matrix1, matrix2));
        measureTimeForMatrixCalculator(new MatrixCalculatorFox(matrix1, matrix2, 2));
        measureTimeForMatrixCalculator(new MatrixCalculatorStriped(matrix1, matrix2, 1));
    }

    private static void measureTimeForMatrixCalculator(MatrixCalculator matrixCalculator) {
        Instant start = Instant.now();
        matrixCalculator
                .multiply()
                .print();
        System.out.println(matrixCalculator.getClass().getSimpleName() + " time = "
                + Duration.between(start, Instant.now()).toMillis() + " ms");
    }
}
