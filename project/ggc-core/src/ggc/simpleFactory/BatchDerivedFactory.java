package ggc.simplefactory;

import java.util.LinkedHashMap;

import ggc.DerivedProduct;
import ggc.Partner;
import ggc.Product;
import ggc.Recipe;
import ggc.Warehouse;
import ggc.exceptions.UnknownPartnerKeyException_;

public class BatchDerivedFactory extends Factory {

  public BatchDerivedFactory(Warehouse w) {
    super(w);
  }

  public void addBacth(String product, String supplier, String price, String amount, String rate, String recipe) {
    try {
      Partner part = getWarehouse().getPartner(supplier);
      double a = Double.parseDouble(amount);
      float p = Float.parseFloat(price);
      Product prod;
      Recipe recipeObj = parseRecipe(rate, recipe);

      if (!getWarehouse().products().containsKey(product)) {
        prod = new DerivedProduct(product, a, p, recipeObj);
      } else {
        prod = getWarehouse().products().get(product);
      }

      getWarehouse().addProduct(prod);
      getWarehouse().addBatch(part, a, p, prod);
    } catch (UnknownPartnerKeyException_ e) {
      e.printStackTrace();
    }
  }

  public Recipe parseRecipe(String rate, String recipe) {
    String[] components = recipe.split("#");
    LinkedHashMap<Product, Integer> rec = new LinkedHashMap<Product, Integer>();

    for (String s : components) {
      String[] info = s.split(":");
      rec.put(getWarehouse().products().get(info[0]), Integer.parseInt(info[1]));
    }

    float r = Float.parseFloat(rate);
    return new Recipe(rec, r);
  }
  
}
