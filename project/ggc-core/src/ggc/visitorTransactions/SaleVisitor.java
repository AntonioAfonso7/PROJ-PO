package ggc.visitorTransactions;

import ggc.transactions.Transaction;

public class SaleVisitor implements Visitor {

  @Override
  public Transaction visitAquisition(Transaction t) {
    return null;
  }

  @Override
  public Transaction visitSale(Transaction t) {
    return t;
  }

  public Transaction visitPaid(Transaction t){
    return t;
  }


}
