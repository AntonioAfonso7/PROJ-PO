package ggc;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import ggc.notification.Observer;

/*  This class represents a Product in a Warehouse system, which
    can be raw (no agregations) or a byproduct (a combination of 
    raw products)
 */
public class Product implements Serializable, Comparable<Product> {

  // name of the product
  private String _name;

  // the highest price of the product
  private float _highestPrice = 0;

  // total stock amount
  private double _totalStock = 0;

  private float _lowestPrice;

  private Map<String, Observer> _observers = new TreeMap<String, Observer>();


  class CollatorWrapper implements Comparator<String>, Serializable {

    private transient Collator _collator = Collator.getInstance(Locale.getDefault());

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
      ois.defaultReadObject();
      _collator = Collator.getInstance(Locale.getDefault());
    }

    @Override
    public int compare(String p1, String p2) {
      return _collator.compare(p1, p2);
    }
  }

  /**
   * 
   * @return the name of the product (String)
   */
  public String getName() {
    return _name;
  }


  /**
   * 
   * @return the highest price associated with this product
   */
  public float getHighestPrice() {
    return _highestPrice;
  }

  public float getLowestPrice() {
    return _lowestPrice;
  }

  /**
   * updates the total Stock value, and updates the highest price available
   * 
   * @param amount
   * @param price
   */
  public void update(double amount, float price) {
    if (_totalStock == 0 && amount != 0) {
      notifyObsNew(price);
    }
    _totalStock += amount;
    if (price > _highestPrice) {
      _highestPrice = price;
    }
    if (price < _lowestPrice) {
      _lowestPrice = price;
      notifyObsBarg();
    }
  }

  /*
  ---------------------------------------------------------------------------------------------------------------------
  /////////////////////////////////////////OBSERVER FUNCTIONS//////////////////////////////////////////////////////////
  ---------------------------------------------------------------------------------------------------------------------
  */

  public void notifyObsBarg() {
    for (Observer n : _observers.values()) {
      n.updatePrice(_lowestPrice);
    }
  }

  public void notifyObsNew(float price) {
    for (Observer n : _observers.values()) {
      n.updateNew(price);
    }
  }

  public void addObserver(Observer obs) {
    _observers.put(obs.getPartner().getId(), obs);
  }

  public void removeObserver(Observer obs) {
    if (_observers.containsKey(obs.getPartner().getId())) _observers.remove(obs.getPartner().getId());
  }

  public void toggleObserver(Observer obs) {
    if (_observers.containsKey(obs.getPartner().getId())) {
      removeObserver(obs);
    } else {
      addObserver(obs);
    }
  }

  /*
  ---------------------------------------------------------------------------------------------------------------------
  ---------------------------------------------------------------------------------------------------------------------
  */

  

  public boolean isSimple() {
    return true;
  }

  public List<Product> breakdown() {
    List<Product> l = new ArrayList<>();
    return l;
  }

  /**
   * constructor of class Product
   * 
   * @param name
   * @param price
   * @param amount
   */
  public Product(String name, float price, double amount) {
    _name = name;
    _highestPrice = price;
    _totalStock += amount;
    _lowestPrice = price;
  }

  public Double getTotalStock(){
    return _totalStock;
  }

  public void removeTotalStock(double amount) {
    _totalStock -= amount;
  }

  @Override
  /**
   * @return a string corresponding to the external view of the Product class
   */
  public String toString() {
    int highest = Math.round(_highestPrice);
    long total = Math.round(_totalStock);
    return _name + "|" + highest + "|" + total;
  }

  @Override
  /**
   * @param p
   * @return an int that represents the comparison of the product p to a given
   *         product
   */
  public int compareTo(Product p) {
    CollatorWrapper c = new CollatorWrapper();
    return c.compare(this.getName(), p.getName());
  }
}
