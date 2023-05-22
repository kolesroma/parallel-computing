package com.kpi.kolesnyk.queueingtheory;

public class Buffer {
    private final int maxSize;
    private int size = 0;
    public Buffer(int maxSize) {
        this.maxSize = maxSize;
    }

    public synchronized boolean add() {
        if (size == maxSize)
            return false;
        else {
            size++;
            return true;
        }
    }

    public synchronized boolean get() {
        if (size == 0)
            return false;
        else {
            size--;
            return true;
        }
    }

    public int getSize() {
        return this.size;
    }
}
