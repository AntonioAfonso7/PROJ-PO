package ggc;

import java.util.List;

public class DerivedProduct extends Product {

  // The recipe of the derived product
  private Recipe _recipe;

  public DerivedProduct(String name, double amount, float price, Recipe recipe) {
    super(name, price, amount);
    _recipe = recipe;
  }

  /**
   * @return Recipe return the _recipe
   */
  public Recipe getRecipe() {
    return _recipe;
  }

  public String toString() {
    return super.toString() + "|" + _recipe.toString();
  }

  @Override
  public boolean isSimple() {
    return false;
  }

  @Override
  public List<Product> breakdown(){
    return _recipe.getProducts();
  }
}
