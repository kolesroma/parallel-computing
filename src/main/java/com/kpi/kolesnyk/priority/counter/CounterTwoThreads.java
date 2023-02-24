package com.kpi.kolesnyk.priority.counter;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CounterTwoThreads {
    private int score = 0;

    public static void main(String[] args) {
        CounterTwoThreads counter = new CounterTwoThreads();
        Lock lock = new ReentrantLock();

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 100_000; i++) {
                lock.lock();
                try {
                    counter.increment();
                } finally {
                    lock.unlock();
                }
            }
        });
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 100_000; i++) {
                lock.lock();
                try {
                    counter.decrement();
                } finally {
                    lock.unlock();
                }
            }
        });

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println(counter.score);
    }

    private void increment() {
        score++;
    }

    private void decrement() {
        score--;
    }
}
