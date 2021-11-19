package ggc.visitorTransactions;

import ggc.transactions.Transaction;

public interface VisitorTransaction {
  public Transaction visitAquisition(Transaction t);
  public Transaction visitSale(Transaction t);
  public Transaction visitPaid (Transaction t);
}


