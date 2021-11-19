package ggc.simplefactory;

import ggc.Warehouse;
import ggc.exceptions.DuplicatePartnerKeyException_;

public class PartnerFactory extends Factory {
  
  public PartnerFactory(Warehouse w) {
    super(w);
  }

  public void addPartner(String key, String name, String address) {
    try {
      getWarehouse().addPartner(key, name, address);
    } catch (DuplicatePartnerKeyException_ e) {
      e.printStackTrace();
    }
  }
}
