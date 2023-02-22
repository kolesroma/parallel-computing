package com.kpi.kolesnyk.matrix;

import com.kpi.kolesnyk.matrix.calculator.impl.MatrixCalculatorFox;
import com.kpi.kolesnyk.matrix.calculator.impl.MatrixCalculatorNaive;
import com.kpi.kolesnyk.matrix.calculator.impl.MatrixCalculatorStriped;

public class Main {
    public static void main(String[] args) {
        int[][] matrix1 = {{1, 2, 3}, {4, 5, 6}};
        int[][] matrix2 = {{7, 8}, {9, 10}, {11, 12}};

        new MatrixCalculatorNaive(matrix1, matrix2)
                .multiply()
                .print();

        new MatrixCalculatorFox(matrix1, matrix2, 2)
                .multiply()
                .print();


        new MatrixCalculatorStriped(matrix1, matrix2, 1)
                .multiply()
                .print();
    }
}
