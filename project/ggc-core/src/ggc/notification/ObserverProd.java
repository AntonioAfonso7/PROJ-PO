package ggc.notification;

import java.io.Serializable;

import ggc.Partner;
import ggc.Product;

public class ObserverProd implements Observer, Serializable {

  private Product _product;

  private Partner _partner;

  public ObserverProd(Product product, Partner partner) {
    _product = product;
    _partner = partner;
  }
  
  public void updatePrice(float price) {
    _partner.receiveNotification(new Bargain(_product, price));
  }

  public void updateNew(float price) {
    _partner.receiveNotification(new New(_product, price));
  }

  public Partner getPartner() {
    return _partner;
  }
}
