package com.kpi.kolesnyk.gmdh;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class GMDH {
    static double[][] provideXMatrix(double[][] data) {
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

    public static void main(String[] args) {
        Instant start = Instant.now();
        double[][] inputMatrix = {
                {5.2, 27.04, 603.552},
                {9.3, 86.49, 1821.5595},
                {149, 22201, 436060.15},
                {-90.6, 8208.36, 159245.478},
                {142.9, 20420.41, 401166.6555},
                {-300.1, 90060.01, 1756596.0355},
                {3, 9, 220.95},
                {9.01, 81.1801, 1713.806955}
        };

        double[][] inputMatrixBig = {
                {5.2, 27.04, 140.608, 856.6464},
                {9.3, 86.49, 804.357, 3269.4021},
                {149, 22201, 3307949, 6390368.35},
                {-90.6, 8208.36, -743677.416, -1179373.8708},
                {142.9, 20420.41, 2918076.589, 5653704.5157},
                {-300.1, 90060.01, -27027009.001, -46892020.1663},
                {3, 9, 27, 269.55},
                {9.01, 81.1801, 731.432701, 3030.3858168}
        };

        Set<Matrix> candidates = new ModelContainer(inputMatrixBig)
                .getCandidates()
                .stream()
                .limit(1)
                .map(GMDH::findB)
                .collect(Collectors.toSet());

        printCandidateStatistics(candidates);
        System.out.printf("time elapsed = %d ms", Duration.between(start, Instant.now()).toMillis());
    }

    private static void printCandidateStatistics(Set<Matrix> candidates) {
        System.out.println(":::: Statistics");
        candidates.forEach(candidate -> {
            System.out.println("regularityCriterion = " + candidate.getRegularityCriterion());
            System.out.println("b = " + Arrays.deepToString(candidate.getB().getValues()));
            System.out.println("matrix = " + Arrays.deepToString(candidate.getValues()));
            System.out.println();
        });
    }

    private static class ModelContainer {
        private final Set<Matrix> candidates = new LinkedHashSet<>();

        public ModelContainer(double[][] complexModel) {
            simplifyComplexMatrix(new Matrix(complexModel));
        }

        /**
         * @param complexMatrix example
         *                      x  |  x^2 |    y
         *                      [
         *                      [5.2, 27.04, 603.552],
         *                      [9.3, 86.49, 1821.5595],
         *                      [149.0, 22201.0, 436060.15]
         *                      ]
         */
        private void simplifyComplexMatrix(Matrix complexMatrix) {
            candidates.add(complexMatrix);
            int basedFunctions = complexMatrix.getNcols() - 1;
            if (basedFunctions <= 1) {
                return;
            }
            for (int columnNumber = 0; columnNumber < basedFunctions; columnNumber++) {
                simplifyComplexMatrix(removeColumn(complexMatrix, columnNumber));
            }
        }

        private Matrix removeColumn(Matrix complexMatrix, int columnNumber) {
            return new Matrix(cloneWithNoColumn(complexMatrix.getValues(), columnNumber));
        }

        private static double[][] cloneWithNoColumn(double[][] a, int columnNumber) {
            double[][] b = new double[a.length][];
            for (int i = 0; i < a.length; i++) {
                b[i] = new double[a[i].length - 1];
                for (int j = 0; j < a[i].length - 1; j++) {
                    if (j >= columnNumber) {
                        b[i][j] = a[i][j + 1];
                    } else {
                        b[i][j] = a[i][j];
                    }
                }
            }
            return b;
        }

        public Set<Matrix> getCandidates() {
            return candidates;
        }
    }

    private static Matrix findB(Matrix candidate) {
        double[][] inputMatrix = candidate.getValues();
        final int n = inputMatrix.length;
        double[][] trainData = Arrays.copyOfRange(inputMatrix, 0, (n + 1) / 2);
        double[][] checkData = Arrays.copyOfRange(inputMatrix, (n + 1) / 2, n);

        System.out.println("input train matrix");
        System.out.println(Arrays.deepToString(trainData));
        System.out.println();

        System.out.println("X");
        double[][] X = provideXMatrix(trainData);
        Matrix matrix = new Matrix(X);
        matrix.print();

        System.out.println("Xt");
        Matrix transpose = MatrixMathematics.transpose(matrix);
        transpose.print();

        System.out.println("Xt * X");
        Matrix multiply = MatrixMathematics.multiply(transpose, matrix);
        multiply.print();

        System.out.println("Xt * X ^ -1");
        Matrix inverse = MatrixMathematics.inverse(multiply);
        inverse.print();

        System.out.println("Xt * X ^ -1 * Xt");
        Matrix multiply1 = MatrixMathematics.multiply(inverse, transpose);
        multiply1.print();

        System.out.println("Y");
        Matrix Y = new Matrix(provideYMatrix(trainData));
        Y.print();

        System.out.println("Xt * X ^ -1 * Xt * Y");
        Matrix b = MatrixMathematics.multiply(multiply1, Y);
        b.print();
        candidate.setB(b);

        candidate.setRegularityCriterion(calculateRegularityCriterion(checkData, b));
        return candidate;
    }

    private static double calculateRegularityCriterion(double[][] checkData, Matrix b) {
        Matrix checkXMatrix = new Matrix(provideXMatrix(checkData));
        Matrix checkYMatrix = new Matrix(provideYMatrix(checkData));
        Matrix multipliedCheckXAndB = MatrixMathematics.multiply(checkXMatrix, b);

        System.out.println("table data");
        checkYMatrix.print();
        System.out.println("model data");
        multipliedCheckXAndB.print();

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
