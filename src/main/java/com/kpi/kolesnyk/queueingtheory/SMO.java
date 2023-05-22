package com.kpi.kolesnyk.queueingtheory;

import java.util.ArrayList;

public class SMO extends Thread {
    private final Producer producer;
    private final ArrayList<Consumer> consumers;
    private final Buffer buffer;
    private final Tracker tracker;
    private final int timeToWork;
    private final ArrayList<Double> logs = new ArrayList<>();

    public SMO(int numOfCanals, int maxSize, int a, int b, int mean,
               int std, boolean toPrint, int timeToWork, int interval) {
        this.timeToWork = timeToWork;
        this.buffer = new Buffer(maxSize);
        this.tracker = new Tracker(buffer, interval);
        this.producer = new Producer(this.buffer, a, b);
        this.consumers = new ArrayList<>();
        for (int i = 0; i < numOfCanals; i++) {
            Consumer c;
            if (toPrint)
                c = new Consumer(buffer, mean, std, Integer.toString(i));
            else
                c = new Consumer(buffer, mean, std);
            consumers.add(c);
        }
    }

    public ArrayList<Double> getLogs() {
        return this.logs;
    }

    @Override
    public void run() {
        producer.start();
        tracker.start();
        for (Consumer c : consumers)
            c.start();
        try {
            Thread.sleep(timeToWork);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        producer.end();
        tracker.end();
        for (Consumer c : consumers)
            c.end();

        try {
            producer.join();
            tracker.join();
            for (Consumer c : consumers)
                c.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ArrayList<Integer> log = tracker.getLog();
        double averageSize = 0;
        for (int size : log)
            averageSize += size;
        averageSize /= log.size();

        int acceptedCount = 0;
        for (Consumer c : consumers)
            acceptedCount += c.getAcceptedCount();


        logs.add(averageSize);
        logs.add((double) acceptedCount);
        logs.add((double)producer.getNumOfDenies());
    }
}
