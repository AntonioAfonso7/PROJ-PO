package ggc.transactions;

import ggc.DerivedProduct;
import ggc.Partner;
import ggc.Product;
import ggc.visitorTransactions.Visitor;

public class Breakdown extends Transaction {

  private Partner _supplier;

  private DerivedProduct _product;

  private double _amount;

  private float _price;

  private BreakdownRecipe _recipe;

  private int _id;

  public Breakdown(Partner supplier, DerivedProduct product, BreakdownRecipe recipe, double amount, float price, int id){
    super(0);
    _price = price;
    _supplier = supplier;
    _product  = product;
    _amount   = amount;
    _recipe = recipe;
    _id = id;
  }

  @Override
  public String toString() {
    long amount = Math.round(_amount);
    int price = Math.round(_price);
    int actualPrice = 0;
    if (price > 0) { actualPrice = price;}
    return "DESAGREGAÇÃO|" + this.getId() + "|" + _supplier.getId() + "|" + _product.getName() + "|"
                           + amount + "|" + price + "|" + actualPrice + "|" + super.getPaymentDate()
                           + "|" + _recipe.toString();
  }

  @Override
  public int compareTo(Transaction t) {
    return this._id - t.getId();
  }

  @Override
  public Transaction accept(Visitor v) {
    return v.visitSale(this);
  }

  @Override
  public void setRealPrice(float realPrice) {}
  
  @Override
  public int getId() {
    return _id;
  }

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



}
