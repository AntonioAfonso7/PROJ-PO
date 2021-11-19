package ggc.transactions;

import ggc.Partner;
import ggc.Product;
import ggc.visitorTransactions.Visitor;

public class Aquisition extends Transaction {

  private Partner _supplier;

  private Product _product;

  private double _amount;

  private float _price;

  private int _id;

  public Aquisition(Partner supplier, Product product, double amount, float price, int id, int date) {
    super(date);
    _supplier = supplier;
    _product = product;
    _amount = amount;
    _price = price;
    _id = id;
  }

  public int getId() {
    return _id;
  }


  public Transaction accept(Visitor v) {
    return v.visitAquisition(this);
  }
  

  @Override
  public int compareTo(Transaction t) {
    return this._id - t.getId();
  }

  @Override
  public void setRealPrice(float realPrice) {}

  
  @Override
  public float getPrice() {
    return 0;
  }

  @Override
  public Product getProduct() {
    return _product;
  }

  public Partner getPartner() {
    return _supplier;
  }

  @Override
  public String toString() {
    long amount = Math.round(_amount);
    long price = Math.round(_amount * _price);
    return "COMPRA|" + getId() + "|" + _supplier.getId() + "|" + _product.getName() + "|" + amount + "|" + price + "|" + getPaymentDate();
  }

}
