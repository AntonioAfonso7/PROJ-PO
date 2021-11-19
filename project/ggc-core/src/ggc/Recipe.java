package ggc;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class Recipe implements Serializable{
 
    private LinkedHashMap<Product, Integer> _products;

    private float _rate;

    public Recipe(LinkedHashMap<Product, Integer> products, float rate) {
        _products = products;
        _rate = rate;
    }

    public List<Product> getProducts() {
      List<Product> lst = new ArrayList<Product>();

      for (Product p : _products.keySet()) {
        for(int i = 0; i < _products.get(p); i++) {
          lst.add(p);
        }
      }
      return lst;
    }

    public float getPrice() {
        float totalPrice = 0;

        for (Product p : _products.keySet()) {
            totalPrice += (p.getHighestPrice()) * _products.get(p);
            totalPrice = (1 + _rate) * totalPrice;
        }

        return totalPrice;
    }

    public float getRate() {
      return _rate;
    }

    public LinkedHashMap<Product, Integer> getRecipe() {
      return _products;
    }

    public String toString() {
      String s = _rate + "|";

      for (Product p : _products.keySet()) {
        s += p.getName() + ":" + _products.get(p) + "#";
      }
      s = s.substring(0, s.length() - 1);  // take the last element "#" 
      return s;
    }
}
