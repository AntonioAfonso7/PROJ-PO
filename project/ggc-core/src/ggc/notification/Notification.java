package ggc.notification;

import java.io.Serializable;

import ggc.Product;

public class Notification implements Serializable{
  private float _price;

  private String _name;

  private Product _product;

  public Notification(Product p) {
    _product = p;
  }

  @Override
  public String toString() {
    return _name + "|" + _product.getName() + "|" +  Math.round(_price) ;
  }

  public void setName(String name) {
    _name = name;
  }

  public void setPrice(float price) {
    _price = price;
  }

}
