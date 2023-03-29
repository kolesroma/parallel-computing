package com.kpi.kolesnyk.lockers.bank;

import java.util.*;

public class Bank {
    public static final int NTEST = 10000;
    private final List<Integer> accounts;
    private long ntransacts;

    public Bank(int n, int initialBalance) {
        accounts = Collections.synchronizedList(new ArrayList<>());
        for (int i = 0; i < n; i++) {
            accounts.add(initialBalance);
        }
        ntransacts = 0;
    }

    public void transfer(int from, int to, int amount) {
        synchronized (accounts) {
            accounts.set(from, accounts.get(from) - amount);
            accounts.set(to, accounts.get(to) + amount);
        }
        if (ntransacts++ % NTEST == 0) {
            test();
        }
    }

    public void test() {
        synchronized (accounts) {
            int sum = 0;
            for (int account : accounts) {
                sum += account;
            }
            System.out.println("Transactions:" + ntransacts + " Sum: " + sum);
        }
    }

    public int size() {
        return accounts.size();
    }
}