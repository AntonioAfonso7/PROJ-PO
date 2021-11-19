package ggc.visitorTransactions;

import ggc.transactions.Transaction;

public class AquistionVisitor implements Visitor {

  @Override
  public Transaction visitAquisition(Transaction t) {
    return t;
  }

  @Override
  public Transaction visitSale(Transaction t) {
    return null;
  }

  public Transaction visitPaid (Transaction t){
    return null;
  }

}
