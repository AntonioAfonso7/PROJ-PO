package ggc;

import java.io.Serializable;


/* This class represents a Batch in the Warehouse system, which 
   is a collection of products (whether they are raw or byproducts ) */
public class Batch implements Serializable, Comparable<Batch> {

  // the supplier of the Batch
  private Partner _supplier;

  // the ammount of the Batch
  private double _amount;

  // the price of the Batch
  private float _price;

  // the product within the Batch
  private Product _product;

  public Batch(Partner supplier, double amount, float price, Product product) {
    _supplier = supplier;
    _amount = amount;
    _price = price;
    _product = product;
  }

  // returns the amount of a given Batch
  public double getAmount() {
    return _amount;
  }

  public float getPrice() {
    return _price;
  }

  public Product getProduct() {
    return _product;
  }

  public Partner getSupplier() {
    return _supplier;
  }

  @Override
  public String toString() {
    long amount = Math.round(_amount);
    int price = Math.round(_price);
    return _product.getName() + "|" + _supplier.getId() + "|" + price + "|" + amount;
  }

  public boolean isEmptyBatch(double amount){
    double diff = getAmount() - amount;
    if (diff <= 0) return true;
     else return false;
  }

  public void removeAmount(double amount) {
    _amount -= amount;
  }

  @Override
  public int compareTo(Batch b) {
    int res = _product.getName().compareTo(b.getProduct().getName()); // Compares the product name
    if (res == 0) {
      res = _supplier.compareTo(b.getSupplier()); // Compares the supplier names
    }
    if (res == 0) {
      res = Math.round((_price - b.getPrice())); // Compares the batches price
    }
    if (res == 0) {
      res = (int) (_amount - b.getAmount()); // Compares the batches amounnt
    }
    return res;
  }

  // checks if the batch is empty (if there's no amount)
  public boolean isEmpty() {
    return getAmount() == 0;
  }

  public boolean underPrice(float price) {
    return _price < price;
  }

}
