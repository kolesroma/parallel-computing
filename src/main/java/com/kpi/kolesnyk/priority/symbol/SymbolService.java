package com.kpi.kolesnyk.priority.symbol;

public class SymbolService {
    public static void main(String[] args) {
        SymbolThread t1 = new SymbolThreadDash();
        SymbolThread t2 = new SymbolThreadVerticalLine();
        t1.start();
        t2.start();
    }
}
