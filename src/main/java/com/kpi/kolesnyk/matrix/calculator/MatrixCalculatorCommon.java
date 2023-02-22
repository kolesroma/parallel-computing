package com.kpi.kolesnyk.matrix.calculator;

import com.kpi.kolesnyk.matrix.record.Result;

public abstract class MatrixCalculatorCommon implements MatrixCalculator {
    private Result result;

    public Result getResult() {
        if (result == null) {
            throw new IllegalArgumentException("Method multiply() should be called before");
        }
        return result;
    }
}
