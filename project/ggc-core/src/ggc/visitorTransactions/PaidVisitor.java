package ggc.visitorTransactions;

import ggc.transactions.Transaction;

public class PaidVisitor extends SaleVisitor {

  @Override
  public Transaction visitAquisition(Transaction t) {
    return null;
  }

  @Override
  public Transaction visitSale(Transaction t) {
    return null;
  }

  public Transaction visitPaid(Transaction t) {
      return t;
  }


}