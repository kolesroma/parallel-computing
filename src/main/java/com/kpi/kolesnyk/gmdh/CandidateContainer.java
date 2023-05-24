package com.kpi.kolesnyk.gmdh;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class CandidateContainer {
    private final double[][] tableData;
    private final LinkedList<int[]> coefficientVectorList = new LinkedList<>();
    private final List<Matrix> candidateList = new ArrayList<>();

    public CandidateContainer(double[][] tableData) {
        this.tableData = tableData;
        prepareCoefficientVectorList();
        prepareCandidateList();
    }

    private void prepareCoefficientVectorList() {
        int[] coefficientVectorComplexModel = new int[tableData[0].length - 1];
        generateCombinations(coefficientVectorComplexModel, 0);
        // remove model with coefficients [0, ... , 0, 0]
        coefficientVectorList.pollLast();
    }

    private void prepareCandidateList() {
        coefficientVectorList.stream()
                .map(this::getSimplifiedDataForCoefficientVector)
                .map(Matrix::new)
                .forEach(candidateList::add);
    }

    private void generateCombinations(int[] coefficientVector, int index) {
        if (index == coefficientVector.length) {
            coefficientVectorList.add(coefficientVector.clone());
        } else {
            coefficientVector[index] = 1;
            generateCombinations(coefficientVector, index + 1);

            coefficientVector[index] = 0;
            generateCombinations(coefficientVector, index + 1);
        }
    }

    private double[][] getSimplifiedDataForCoefficientVector(int[] vectorValue) {
        final int basedFunctions = Arrays.stream(vectorValue).sum();
        final int basedFunctionsWithColumnY = basedFunctions + 1;
        double[][] container = new double[tableData.length][basedFunctionsWithColumnY];
        for (int i = 0; i < tableData.length; i++) {
            // set x values where vector value coefficient = 1
            int offset = 0;
            for (int j = 0; j < vectorValue.length; j++) {
                if (vectorValue[j] == 1) {
                    container[i][j - offset] = tableData[i][j];
                } else {
                    offset++;
                }
            }
            // set y value
            container[i][basedFunctions] = tableData[i][tableData[i].length - 1];
        }
        return container;
    }

    public List<Matrix> getCandidateList() {
        return candidateList;
    }
}
