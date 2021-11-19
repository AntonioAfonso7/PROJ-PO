package ggc.transactions;

import java.util.Map;
import java.util.TreeMap;

import ggc.Product;

public class BreakdownRecipe {

  private Map<Product, Double> _amount;

  private Map<Product, Float> _price;

  public BreakdownRecipe(TreeMap<Product, Double> amount, TreeMap<Product, Float> price) {
    _amount = amount;
    _price = price;
  }

  public String toString() {
    String str = "";

    for(Product p : _amount.keySet()) {
      str += p.getName() + ":" + Math.round(_amount.get(p)) + ":" + Math.round(_price.get(p)) + "#";
    }

    str = str.substring(0, str.length() - 1);  // take the last element "#" 
    return str;

  }
  
}
