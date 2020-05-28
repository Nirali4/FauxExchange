package com.g4.fauxexchange.service.impl;

import java.util.List;
import com.g4.fauxexchange.model.Transaction;
import com.g4.fauxexchange.model.Order;

public interface TransactionService {
    public abstract boolean createTransaction(Order order);
    // public abstract void updateTransaction();
    // public abstract boolean deleteTransaction(Transaction transaction);
    public abstract void processTransactions(Transaction transaction);
    public abstract List<Transaction> getTransactions();
    public abstract List<Transaction> getTransactionsByUserId(String id);
    public abstract double getStandings(String id);
}
