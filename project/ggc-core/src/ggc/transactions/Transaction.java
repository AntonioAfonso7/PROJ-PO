package ggc.transactions;

import java.io.Serializable;

import ggc.Product;
import ggc.Partner;
import ggc.visitorTransactions.Visitor;

public abstract class Transaction implements Serializable, Comparable<Transaction> {

  private int _paymentDate;


  public Transaction(int date) {
    _paymentDate = date;
  }

  public void setPaymentDate(int date) {
    _paymentDate = date;
  }

  public int getPaymentDate() {
    return _paymentDate;
  }


  public abstract float getPrice();
  public abstract Product getProduct();
  public abstract Transaction accept(Visitor v);
  public abstract int getId();
  public abstract Partner getPartner();
  public abstract void setRealPrice(float realPrice);
}
