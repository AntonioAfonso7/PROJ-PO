package ggc.notification;

import ggc.Product;

public class New extends Notification {
  public New(Product product, float price) {
    super(product);
    super.setName("NEW");
    super.setPrice(price);
  }
}
