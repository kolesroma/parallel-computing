package com.kpi.kolesnyk.gmdh;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GMDH {
    static double[] getColumn(double[][] data, int columnNumber) {
        double[] collector = new double[data.length];
        for (int rowNumber = 0; rowNumber < data.length; rowNumber++) {
            collector[rowNumber] = data[rowNumber][columnNumber];
        }
        return collector;
    }

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

        findB(inputMatrix);

//        List<Matrix> candidates = new ModelContainer(inputMatrix).candidates;

//        candidates.forEach(candidate -> findB(candidate.getValues()));
    }

    static class ModelContainer {
        List<Matrix> candidates = new ArrayList<>();

        public ModelContainer(double[][] complexModel) {
            Matrix complexMatrix = new Matrix(complexModel);

            candidates.add(MatrixMathematics.createSubMatrix(complexMatrix, 1, 1));
            candidates.add(MatrixMathematics.createSubMatrix(complexMatrix, 1, 2));
            candidates.add(complexMatrix);

//            candidates.add(nullColumn(complexMatrix, 1));
//            candidates.add(nullColumn(complexMatrix, 2));
//            candidates.add(complexMatrix);
        }

        private Matrix nullColumn(Matrix complexMatrix, int columnNumber) {
            double[][] values = complexMatrix.getValues();
            double[][] collector = Arrays.copyOf(values, values.length);
            for (var row : collector) {
                row[columnNumber] = 0;
            }

            Matrix matrix = new Matrix(collector);
            System.out.println(">>>");
            matrix.print();

            return matrix;
        }
    }

    private static void findB(double[][] inputMatrix) {
        final int n = inputMatrix.length;
        double[][] testData = Arrays.copyOfRange(inputMatrix, 0, (n + 1) / 2);
        double[][] checkData = Arrays.copyOfRange(inputMatrix, (n + 1) / 2, n);
        inputMatrix = testData;

        System.out.println("input matrix");
        System.out.println(Arrays.deepToString(inputMatrix));
        System.out.println();

        System.out.println("X");
        double[][] X = provideXMatrix(inputMatrix);
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
        Matrix Y = new Matrix(provideYMatrix(inputMatrix));
        Y.print();

        System.out.println("Xt * X ^ -1 * Xt * Y");
        Matrix b = MatrixMathematics.multiply(multiply1, Y);
        b.print();

        double regularityCriterion = getRegularityCriterion(checkData, b);
        System.out.println("regularityCriterion = " + regularityCriterion);
    }

    private static double getRegularityCriterion(double[][] checkData, Matrix b) {
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

class Matrix {
    private int nrows;
    private int ncols;
    private double[][] data;

    public Matrix(double[][] dat) {
        this.data = dat;
        this.nrows = dat.length;
        this.ncols = dat[0].length;
    }

    public Matrix(int nrow, int ncol) {
        this.nrows = nrow;
        this.ncols = ncol;
        data = new double[nrow][ncol];
    }

    public void print() {
        for (double[] row : data) {
            System.out.println(Arrays.toString(row));
        }
        System.out.println();
    }

    public int getNrows() {
        return nrows;
    }

    public void setNrows(int nrows) {
        this.nrows = nrows;
    }

    public int getNcols() {
        return ncols;
    }

    public void setNcols(int ncols) {
        this.ncols = ncols;
    }

    public double[][] getValues() {
        return data;
    }

    public void setValues(double[][] values) {
        this.data = values;
    }

    public void setValueAt(int row, int col, double value) {
        data[row][col] = value;
    }

    public double getValueAt(int row, int col) {
        return data[row][col];
    }

    public boolean isSquare() {
        return nrows == ncols;
    }

    public int size() {
        if (isSquare())
            return nrows;
        return -1;
    }

    public Matrix multiplyByConstant(double constant) {
        Matrix mat = new Matrix(nrows, ncols);
        for (int i = 0; i < nrows; i++) {
            for (int j = 0; j < ncols; j++) {
                mat.setValueAt(i, j, data[i][j] * constant);
            }
        }
        return mat;
    }

    public Matrix insertColumnWithValue1() {
        Matrix X_ = new Matrix(this.getNrows(), this.getNcols() + 1);
        for (int i = 0; i < X_.getNrows(); i++) {
            for (int j = 0; j < X_.getNcols(); j++) {
                if (j == 0)
                    X_.setValueAt(i, j, 1.0);
                else
                    X_.setValueAt(i, j, this.getValueAt(i, j - 1));

            }
        }
        return X_;
    }
}

class MatrixMathematics {

    /**
     * This class a matrix utility class and cannot be instantiated.
     */
    private MatrixMathematics() {
    }

    /**
     * Transpose of a matrix - Swap the columns with rows
     *
     * @param matrix
     * @return
     */
    public static Matrix transpose(Matrix matrix) {
        Matrix transposedMatrix = new Matrix(matrix.getNcols(), matrix.getNrows());
        for (int i = 0; i < matrix.getNrows(); i++) {
            for (int j = 0; j < matrix.getNcols(); j++) {
                transposedMatrix.setValueAt(j, i, matrix.getValueAt(i, j));
            }
        }
        return transposedMatrix;
    }

    /**
     * Inverse of a matrix - A-1 * A = I where I is the identity matrix
     * A matrix that have inverse is called non-singular or invertible. If the matrix does not have inverse it is called singular.
     * For a singular matrix the values of the inverted matrix are either NAN or Infinity
     * Only square matrices have inverse and the following method will throw exception if the matrix is not square.
     *
     * @param matrix
     * @return
     * @throws NoSquareException
     */
    public static Matrix inverse(Matrix matrix) throws NoSquareException {
        if (determinant(matrix) == 0)
            throw new NoSquareException("determinant equals zero");

        return (transpose(cofactor(matrix)).multiplyByConstant(1.0 / determinant(matrix)));
    }

    /**
     * Determinant of a square matrix
     * The following function find the determinant in a recursively.
     *
     * @param matrix
     * @return
     * @throws NoSquareException
     */
    public static double determinant(Matrix matrix) throws NoSquareException {
        if (!matrix.isSquare())
            throw new NoSquareException("matrix need to be square.");
        if (matrix.size() == 1) {
            return matrix.getValueAt(0, 0);
        }

        if (matrix.size() == 2) {
            return (matrix.getValueAt(0, 0) * matrix.getValueAt(1, 1)) - (matrix.getValueAt(0, 1) * matrix.getValueAt(1, 0));
        }
        double sum = 0.0;
        for (int i = 0; i < matrix.getNcols(); i++) {
            sum += changeSign(i) * matrix.getValueAt(0, i) * determinant(createSubMatrix(matrix, 0, i));
        }
        return sum;
    }

    /**
     * Determine the sign; i.e. even numbers have sign + and odds -
     *
     * @param i
     * @return
     */
    private static int changeSign(int i) {
        if (i % 2 == 0)
            return 1;
        return -1;
    }

    /**
     * Creates a submatrix excluding the given row and column
     *
     * @param matrix
     * @param excluding_row
     * @param excluding_col
     * @return
     */
    public static Matrix createSubMatrix(Matrix matrix, int excluding_row, int excluding_col) {
        Matrix mat = new Matrix(matrix.getNrows() - 1, matrix.getNcols() - 1);
        int r = -1;
        for (int i = 0; i < matrix.getNrows(); i++) {
            if (i == excluding_row)
                continue;
            r++;
            int c = -1;
            for (int j = 0; j < matrix.getNcols(); j++) {
                if (j == excluding_col)
                    continue;
                mat.setValueAt(r, ++c, matrix.getValueAt(i, j));
            }
        }
        return mat;
    }

    /**
     * The cofactor of a matrix
     *
     * @param matrix
     * @return
     * @throws NoSquareException
     */
    public static Matrix cofactor(Matrix matrix) throws NoSquareException {
        Matrix mat = new Matrix(matrix.getNrows(), matrix.getNcols());
        for (int i = 0; i < matrix.getNrows(); i++) {
            for (int j = 0; j < matrix.getNcols(); j++) {
                mat.setValueAt(i, j, changeSign(i) * changeSign(j) * determinant(createSubMatrix(matrix, i, j)));
            }
        }

        return mat;
    }

    /**
     * Adds two matrices of the same dimension
     *
     * @param matrix1
     * @param matrix2
     * @return
     * @throws IllegalDimensionException
     */
    public static Matrix add(Matrix matrix1, Matrix matrix2) throws IllegalDimensionException {
        if (matrix1.getNcols() != matrix2.getNcols() || matrix1.getNrows() != matrix2.getNrows())
            throw new IllegalDimensionException("Two matrices should be the same dimension.");
        Matrix sumMatrix = new Matrix(matrix1.getNrows(), matrix1.getNcols());
        for (int i = 0; i < matrix1.getNrows(); i++) {
            for (int j = 0; j < matrix1.getNcols(); j++)
                sumMatrix.setValueAt(i, j, matrix1.getValueAt(i, j) + matrix2.getValueAt(i, j));

        }
        return sumMatrix;
    }

    /**
     * subtract two matrices using the above addition method. Matrices should be the same dimension.
     *
     * @param matrix1
     * @param matrix2
     * @return
     * @throws IllegalDimensionException
     */
    public static Matrix subtract(Matrix matrix1, Matrix matrix2) throws IllegalDimensionException {
        return add(matrix1, matrix2.multiplyByConstant(-1));
    }

    /**
     * Multiply two matrices
     *
     * @param matrix1
     * @param matrix2
     * @return
     */
    public static Matrix multiply(Matrix matrix1, Matrix matrix2) {
        Matrix multipliedMatrix = new Matrix(matrix1.getNrows(), matrix2.getNcols());

        for (int i = 0; i < multipliedMatrix.getNrows(); i++) {
            for (int j = 0; j < multipliedMatrix.getNcols(); j++) {
                double sum = 0.0;
                for (int k = 0; k < matrix1.getNcols(); k++) {
                    sum += matrix1.getValueAt(i, k) * matrix2.getValueAt(k, j);
                }
                multipliedMatrix.setValueAt(i, j, sum);
            }
        }
        return multipliedMatrix;
    }
}

class NoSquareException extends RuntimeException {

    public NoSquareException() {
        super();
        // TODO Auto-generated constructor stub
    }

    public NoSquareException(String message) {
        super(message);
        // TODO Auto-generated constructor stub
    }

}

class IllegalDimensionException extends RuntimeException {

    public IllegalDimensionException() {
        super();
        // TODO Auto-generated constructor stub
    }

    public IllegalDimensionException(String message) {
        super(message);
        // TODO Auto-generated constructor stub
    }

}