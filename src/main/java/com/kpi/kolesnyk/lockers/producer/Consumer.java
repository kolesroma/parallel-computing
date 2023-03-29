package com.kpi.kolesnyk.lockers.producer;

public class Consumer implements Runnable {
    private final Drop drop;

    public Consumer(Drop drop) {
        this.drop = drop;
    }

    @Override
    public void run() {
        String message;
        while (!"DONE".equals(message = drop.take())) {
            System.out.format("MESSAGE RECEIVED: %s%n", message);
        }
    }
}
