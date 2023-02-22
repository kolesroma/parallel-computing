package com.kpi.kolesnyk.matrix.record;

public record Result(int[][] matrix) {
    public void print() {
        for (int[] line : matrix) {
            for (int j = 0; j < matrix[0].length; j++) {
                System.out.print(line[j] + " ");
            }
            System.out.println();
        }
    }
}
