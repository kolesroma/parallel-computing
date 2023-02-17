package com.kpi.kolesnyk.priority.bounce;

public class Counter {
    private static int goals;

    public static synchronized void showResults() {
        System.out.println("total score: " + ++goals);;
    }
}
