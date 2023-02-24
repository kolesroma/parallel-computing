package com.kpi.kolesnyk.priority.symbol;

public class SymbolService {
    public static void main(String[] args) {
        SymbolThread t1 = new SymbolThread("-");
        SymbolThread t2 = new SymbolThread("|");
        t1.start();
        t2.start();
    }
}
