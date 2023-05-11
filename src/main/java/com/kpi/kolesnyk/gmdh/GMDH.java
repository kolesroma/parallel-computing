package com.kpi.kolesnyk.gmdh;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.*;
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
                {5.20,27.04,140.61,731.16,32.66,7053.91},
                {9.30,86.49,804.36,7480.52,29.20,69622.42},
                {11.90,141.61,1685.16,20053.39,18.68,185510.01},
                {-9.06,82.08,-743.68,6737.72,-56.90,64782.66},
                {1.43,2.04,2.92,4.17,4.49,84.00},
                {-3.90,15.22,-59.36,231.58,-6.12,2482.62},
                {3.10,9.61,29.79,92.35,19.47,984.67},
                {9.01,81.18,731.43,6590.21,28.29,61406.58},
                {14.39,207.07,2979.77,42878.85,22.59,395527.37},
                {-2.99,8.95,-26.76,80.03,-18.78,971.15},
                {4.10,16.81,68.92,282.58,12.87,2861.17},
                {10.01,100.20,1003.00,10040.06,15.72,93275.23}
        };

        Set<Matrix> candidates1 = new ModelContainer(inputMatrixBig)
                .getCandidates();

        System.out.println(candidates1.size());
        Set<Matrix> candidates = candidates1
                .stream()
//                .limit(1)
                .map(GMDH::findB)
                .sorted(Comparator.comparing(Matrix::getRegularityCriterion))
                .collect(Collectors.toCollection(LinkedHashSet::new));


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
        private final Set<Matrix> candidates = Collections.synchronizedSet(new HashSet<>());
        private final ForkJoinPool pool = new ForkJoinPool();

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
            StarRecursiveAction task = new StarRecursiveAction(complexMatrix, candidates);
            pool.execute(task);
            task.join();
        }

        private static class StarRecursiveAction extends RecursiveAction {
            private static final int THRESHOLD = 10;
            private final Matrix complexMatrix;
            private final Set<Matrix> candidates;

            public StarRecursiveAction(Matrix complexMatrix, Set<Matrix> candidates) {
                this.complexMatrix = complexMatrix;
                this.candidates = candidates;
            }

            private void simplifyRecursive(Matrix complexMatrix) {
                candidates.add(complexMatrix);
                int basedFunctions = complexMatrix.getNcols() - 1;
                if (basedFunctions <= 1) {
                    return;
                }
                for (int columnNumber = 0; columnNumber < basedFunctions; columnNumber++) {
                    simplifyRecursive(removeColumn(complexMatrix, columnNumber));
                }
            }

            @Override
            protected void compute() {
                candidates.add(complexMatrix);
                int basedFunctions = complexMatrix.getNcols() - 1;
                if (basedFunctions <= THRESHOLD) {
                    simplifyRecursive(complexMatrix);
                    return;
                }
                for (int columnNumber = 0; columnNumber < basedFunctions; columnNumber++) {
                    ForkJoinTask.invokeAll(
                            new StarRecursiveAction(removeColumn(complexMatrix, columnNumber), candidates)
                    );
                }
            }
        }

        private static Matrix removeColumn(Matrix complexMatrix, int columnNumber) {
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

//        System.out.println("input train matrix");
//        System.out.println(Arrays.deepToString(trainData));
//        System.out.println();

//        System.out.println("X");
        double[][] X = provideXMatrix(trainData);
        Matrix matrix = new Matrix(X);
//        matrix.print();

//        System.out.println("Xt");
        Matrix transpose = MatrixMathematics.transpose(matrix);
//        transpose.print();

//        System.out.println("Xt * X");
        Matrix multiply = MatrixMathematics.multiply(transpose, matrix);
//        multiply.print();

//        System.out.println("Xt * X ^ -1");
        Matrix inverse = MatrixMathematics.inverse(multiply);
//        inverse.print();

//        System.out.println("Xt * X ^ -1 * Xt");
        Matrix multiply1 = MatrixMathematics.multiply(inverse, transpose);
//        multiply1.print();

//        System.out.println("Y");
        Matrix Y = new Matrix(provideYMatrix(trainData));
//        Y.print();

//        System.out.println("Xt * X ^ -1 * Xt * Y");
        Matrix b = MatrixMathematics.multiply(multiply1, Y);
//        b.print();
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
