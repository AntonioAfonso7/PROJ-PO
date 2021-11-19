package ggc.transactions;

import ggc.Product;
import ggc.visitorTransactions.Visitor;
import ggc.Partner;


public class Sale extends Transaction {

  private Partner _supplier;

  private Product _product;

  private double _amount;

  private float _price;

  private int _id;

  private int _payDate = 0;

  private float _realPrice = 0;

  private boolean _paid = false; // default at false 


  public Sale(Partner supplier, Product product, double amount, float price, int limitDate, int id){
    super(limitDate);
    _price = price;
    _supplier = supplier;
    _product  = product;
    _amount   = amount;
    _id = id;
  }


  public Transaction accept(Visitor v) {
    if (_paid) return v.visitPaid(this);
    return v.visitSale(this);
  }

  public void setPayDate(int date) {
    _payDate = date;
  }

  public void setRealPrice(float realPrice){
    _realPrice = realPrice;
  }

  public int getId() {
    return _id;
  }

  public float getPrice(){
    return _price;  
  }

  public void pay() {
    _paid = true;
  }


  @Override
  public int compareTo(Transaction t) {
    return this._id - t.getId();
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
    int price = Math.round(_price);
    if (_paid) {
      return "VENDA|" + this.getId() + "|" + _supplier.getId() + "|" +
              _product.getName() + "|" + amount + '|' + price + "|" +
               Math.round(_realPrice) + "|" + super.getPaymentDate() + "|" + _payDate;
    }
    return "VENDA|" + this.getId() + "|" + _supplier.getId() + "|" +
                      _product.getName() + "|" + amount + '|' + price + 
                      "|" + Math.round(_realPrice) + "|" + super.getPaymentDate();
  }

}
