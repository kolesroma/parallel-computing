package com.kpi.kolesnyk.queueingtheory;

import java.util.Random;

public class Producer extends Thread{
    private final Buffer buff;
    private final int a;
    private final int b;
    private int numOfDenies = 0;
    private boolean toEnd = false;

    public Producer(Buffer buff, int a, int b) {
        this.buff = buff;
        this.a = a;
        this.b = b;
        this.numOfDenies = 0;
    }

    public void end() {
        this.toEnd = true;
    }

    public int getNumOfDenies() {
        return this.numOfDenies;
    }

    @Override
    public void run() {
        Random rand = new Random();
        while (!toEnd) {
            try {
                Thread.sleep(rand.nextInt(b - a) + a);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (!buff.add()) {
                numOfDenies++;
            }
        }
    }
}
