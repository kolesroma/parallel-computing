package com.kpi.kolesnyk.priority.symbol;

public abstract class SymbolThread extends Thread {
    abstract String printSymbol();

    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            System.out.println(printSymbol());
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
