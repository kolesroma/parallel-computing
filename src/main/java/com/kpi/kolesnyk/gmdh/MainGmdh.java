package com.kpi.kolesnyk.gmdh;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ForkJoinPool;

public class MainGmdh {
    public static void main(String[] args) {
        Instant start = Instant.now();
        double[][] tableData = {
                {5.20, 27.04, 140.61, 731.16, 32.66, 7053.91},
                {9.30, 86.49, 804.36, 7480.52, 29.20, 69622.42},
                {11.90, 141.61, 1685.16, 20053.39, 18.68, 185510.01},
                {-9.06, 82.08, -743.68, 6737.72, -56.90, 64782.66},
                {1.43, 2.04, 2.92, 4.17, 4.49, 84.00},
                {-3.90, 15.22, -59.36, 231.58, -6.12, 2482.62},
                {3.10, 9.61, 29.79, 92.35, 19.47, 984.67},
                {9.01, 81.18, 731.43, 6590.21, 28.29, 61406.58},
                {14.39, 207.07, 2979.77, 42878.85, 22.59, 395527.37},
                {-2.99, 8.95, -26.76, 80.03, -18.78, 971.15},
                {4.10, 16.81, 68.92, 282.58, 12.87, 2861.17},
                {10.01, 100.20, 1003.00, 10040.06, 15.72, 93275.23}
        };

        Gmdh gmdh = new Gmdh(tableData, new ForkJoinPool());
        List<Matrix> allCandidates = gmdh.getAllCandidates();
        System.out.println("Total models = " + allCandidates.size());
        gmdh.printApprovedCandidateStatistics();
        System.out.printf("Time elapsed = %d ms", Duration.between(start, Instant.now()).toMillis());
    }
}
