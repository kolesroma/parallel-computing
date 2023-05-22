package com.kpi.kolesnyk.queueingtheory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;

public class MainSMO {
    public static void main(String[] args) {
        ArrayList<Double> allLogs = new ArrayList<>(Arrays.asList(0.0, 0.0, 0.0));
        final int numOfSMOs = 12;
        ArrayList<SMO> SMOs = new ArrayList<>();
        initSmos(numOfSMOs, SMOs);

        try (ForkJoinPool pool = new ForkJoinPool()) {
            for (SMO smo : SMOs) {
                pool.execute(() -> {
                    smo.start();
                    task(allLogs, smo);
                });
            }
        }

        printStats(allLogs, numOfSMOs);

    }

    private static void initSmos(int numOfSMOs, ArrayList<SMO> SMOs) {
        for (int i = 0; i < numOfSMOs; i++) {
            SMO smo = new SMO(8, 5, 0, 10,
                    50, 10, true, 1000, 100);
            SMOs.add(smo);
        }
    }

    private static void printStats(ArrayList<Double> allLogs, int numOfSMOs) {
        for (int i = 0; i < 3; i++) {
            allLogs.set(i, allLogs.get(i) / numOfSMOs);
        }
        System.out.println("OVERALL STATISTICS");
        System.out.println("Average queue size = " + allLogs.get(0));
        System.out.println("Accepted = " + allLogs.get(1));
        System.out.println("Denied = " + allLogs.get(2));
    }

    private static void task(ArrayList<Double> allLogs, SMO smo) {
        try {
            smo.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ArrayList<Double> logs = smo.getLogs();
        for (int i = 0; i < 3; i++) {
            allLogs.set(i, allLogs.get(i) + logs.get(i));
        }
        System.out.println("Average queue size = " + logs.get(0));
        System.out.println("Accepted = " + logs.get(1));
        System.out.println("Denied = " + logs.get(2));
    }
}
