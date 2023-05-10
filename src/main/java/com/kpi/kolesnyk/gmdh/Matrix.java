package com.kpi.kolesnyk.gmdh;

import java.util.Arrays;

public class Matrix {
    private int nrows;
    private int ncols;
    private double[][] data;
    private Matrix B;
    private Double regularityCriterion;

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

    public Matrix getB() {
        return B;
    }

    public void setB(Matrix b) {
        this.B = b;
    }

    public Double getRegularityCriterion() {
        return regularityCriterion;
    }

    public void setRegularityCriterion(Double regularityCriterion) {
        this.regularityCriterion = regularityCriterion;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Matrix matrix = (Matrix) o;

        return Arrays.deepEquals(data, matrix.data);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(data);
    }
}
