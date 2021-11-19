package ggc.notification;

import ggc.Product;

public class Bargain extends Notification {
  public Bargain(Product product, float price) {
    super(product);
    super.setName("BARGAIN");
    super.setPrice(price);
  }
}
