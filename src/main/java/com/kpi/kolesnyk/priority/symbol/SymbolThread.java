package com.kpi.kolesnyk.priority.symbol;

public class SymbolThread extends Thread {
    private final String symbol;

    public SymbolThread(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            try {
                synchronized (System.out) {
                    System.out.println(symbol);
                    System.out.notify();
                    System.out.wait(2000);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
