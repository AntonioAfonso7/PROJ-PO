package ggc.simplefactory;

import ggc.Partner;
import ggc.Product;
import ggc.Warehouse;
import ggc.exceptions.UnknownPartnerKeyException_;

public class BatchFactory extends Factory {

  public BatchFactory(Warehouse w) {
    super(w);
  }

  /**
   * 
   * @param product
   * @param supplier
   * @param price
   * @param amount
   */
  public void addBacth(String product, String supplier, String price, String amount) {
    try {
      Partner p = getWarehouse().getPartner(supplier);
      double a = Double.parseDouble(amount);
      float f = Float.parseFloat(price);
      Product prod;

      if (!getWarehouse().products().containsKey(product)) {
        prod = new Product(product, f, a);
      } else {
        prod = getWarehouse().products().get(product);
      }

      getWarehouse().addBatch(p, a, f, prod);
      getWarehouse().addProduct(prod);

    } catch (UnknownPartnerKeyException_ e) {
      e.printStackTrace();
    }
  }
}
