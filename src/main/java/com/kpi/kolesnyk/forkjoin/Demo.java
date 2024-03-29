package com.kpi.kolesnyk.forkjoin;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;

/**
 * Check out demo on
 * <a href="https://mkyong.com/java/java-fork-join-framework-examples/">link</a>
 */
public class Demo {
    public static void main(String[] args) {
        ForkJoinFibonacci task = new ForkJoinFibonacci(11);
        try (ForkJoinPool pool = new ForkJoinPool()) {
            pool.invoke(task);
        }
        System.out.println(task.getNumber());
    }
}

class ForkJoinFibonacci extends RecursiveAction {
    private static final long threshold = 10;
    private volatile long number;

    public ForkJoinFibonacci(long number) {
        this.number = number;
    }

    public long getNumber() {
        return number;
    }

    @Override
    protected void compute() {
        long n = number;
        if (n <= threshold) {
            number = fib(n);
        } else {
            ForkJoinFibonacci f1 = new ForkJoinFibonacci(n - 1);
            ForkJoinFibonacci f2 = new ForkJoinFibonacci(n - 2);
            ForkJoinTask.invokeAll(f1, f2);
            number = f1.number + f2.number;
        }
    }

    private static long fib(long n) {
        if (n <= 1) {
            return n;
        }
        return fib(n - 1) + fib(n - 2);
    }
}