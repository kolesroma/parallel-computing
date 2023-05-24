package com.kpi.kolesnyk.gmdh;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class Gmdh {
    public static final double REGULARITY_CRITERION_THRESHOLD = 0.05;

    private final CandidateContainer candidateContainer;
    private final Set<Matrix> approvedCandidates;
    private final ForkJoinPool pool;

    public Gmdh(double[][] tableData, ForkJoinPool pool) {
        this.candidateContainer = new CandidateContainer(tableData);
        this.pool = pool;
        this.approvedCandidates = calculateApprovedCandidatesInParallel();
    }

    private Set<Matrix> calculateApprovedCandidatesInParallel() {
        return pool.invokeAll(getTaskList())
                .stream()
                .map(Gmdh::mapFutureToMatrix)
                .filter(candidate -> candidate.getRegularityCriterion() <= REGULARITY_CRITERION_THRESHOLD)
                .sorted(Comparator.comparing(Matrix::getRegularityCriterion))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private List<Callable<Matrix>> getTaskList() {
        return getAllCandidates()
                .stream()
                .map(candidate -> (Callable<Matrix>) () -> findB(candidate))
                .toList();
    }

    private static Matrix mapFutureToMatrix(Future<Matrix> matrixFuture) {
        try {
            return matrixFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Matrix> getAllCandidates() {
        return candidateContainer.getCandidateList();
    }

    public void printApprovedCandidateStatistics() {
        System.out.println("Approved models = " + approvedCandidates.size());
        System.out.println("With regularity criterion threshold = " + REGULARITY_CRITERION_THRESHOLD);
        approvedCandidates.forEach(candidate -> {
            System.out.println("regularityCriterion = " + candidate.getRegularityCriterion());
            System.out.println("b = " + Arrays.deepToString(candidate.getB().getValues()));
            System.out.println("matrix = " + Arrays.deepToString(candidate.getValues()));
            System.out.println();
        });
    }

    private static double[][] provideXMatrix(double[][] data) {
        double[][] collector = new double[data.length][data[0].length];
        for (int rowNumber = 0; rowNumber < data.length; rowNumber++) {
            double[] row = new double[data[0].length];
            row[0] = 1;
            System.arraycopy(data[rowNumber], 0, row, 1, row.length - 1);
            collector[rowNumber] = row;
        }
        return collector;
    }

    private static double[][] provideYMatrix(double[][] data) {
        double[][] collector = new double[data.length][1];
        int columnNumber = data[0].length;
        for (int rowNumber = 0; rowNumber < data.length; rowNumber++) {
            collector[rowNumber][0] = data[rowNumber][columnNumber - 1];
        }
        return collector;
    }

    private static Matrix findB(Matrix candidate) {
        double[][] inputMatrix = candidate.getValues();
        final int n = inputMatrix.length;
        double[][] trainData = Arrays.copyOfRange(inputMatrix, 0, (n + 1) / 2);
        double[][] checkData = Arrays.copyOfRange(inputMatrix, (n + 1) / 2, n);

        double[][] X = provideXMatrix(trainData);
        Matrix matrix = new Matrix(X);

        Matrix transpose = MatrixMathematics.transpose(matrix);
        Matrix multiply = MatrixMathematics.multiply(transpose, matrix);
        Matrix inverse = MatrixMathematics.inverse(multiply);
        Matrix multiply1 = MatrixMathematics.multiply(inverse, transpose);
        Matrix Y = new Matrix(provideYMatrix(trainData));

        Matrix b = MatrixMathematics.multiply(multiply1, Y);
        candidate.setB(b);

        candidate.setRegularityCriterion(calculateRegularityCriterion(checkData, b));
        return candidate;
    }

    private static double calculateRegularityCriterion(double[][] checkData, Matrix b) {
        Matrix checkXMatrix = new Matrix(provideXMatrix(checkData));
        Matrix checkYMatrix = new Matrix(provideYMatrix(checkData));
        Matrix multipliedCheckXAndB = MatrixMathematics.multiply(checkXMatrix, b);

        double delta = 0;
        double divided = 0;
        for (int row = 0; row < checkYMatrix.getNrows(); row++) {
            double modelValue = multipliedCheckXAndB.getValueAt(row, 0);
            double tableValue = checkYMatrix.getValueAt(row, 0);
            delta += Math.pow(modelValue - tableValue, 2);
            divided += Math.pow(tableValue, 2);
        }
        return delta / divided;
    }
}
