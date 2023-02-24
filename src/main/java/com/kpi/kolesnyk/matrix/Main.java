package com.kpi.kolesnyk.matrix;

import com.kpi.kolesnyk.matrix.calculator.MatrixCalculator;
import com.kpi.kolesnyk.matrix.calculator.impl.MatrixCalculatorFox;
import com.kpi.kolesnyk.matrix.calculator.impl.MatrixCalculatorNaive;
import com.kpi.kolesnyk.matrix.calculator.impl.MatrixCalculatorStriped;

import java.time.Duration;
import java.time.Instant;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        int[][] matrix = createMatrix(500, 500);

        measureTimeForMatrixCalculator(new MatrixCalculatorNaive(matrix, matrix));
        measureTimeForMatrixCalculator(new MatrixCalculatorFox(matrix, matrix, 100));
        measureTimeForMatrixCalculator(new MatrixCalculatorStriped(matrix, matrix, 250));
    }

    private static int[][] createMatrix(int a, int b) {
        int[][] matrix = new int[a][b];
        for (int i = 0; i < a; i++) {
            for (int j = 0; j < b; j++) {
                matrix[i][j] = new Random().nextInt(99);
            }
        }
        return matrix;
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
