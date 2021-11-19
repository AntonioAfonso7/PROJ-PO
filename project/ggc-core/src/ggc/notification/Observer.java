package ggc.notification;

import ggc.Partner;

public interface Observer {
  public void updatePrice(float price);
  public void updateNew(float price);
  public Partner getPartner();
}
