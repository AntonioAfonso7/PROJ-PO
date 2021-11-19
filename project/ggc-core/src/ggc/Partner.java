package ggc;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import ggc.notification.Notification;
import ggc.states.*;

/*
*   This class represents a Parner in the warehouse system
*   wich can execute transactions to aquire products and sell them.
*/
public class Partner implements Serializable, Comparable<Partner> {

  // Id of the partners
  private String _id;

  // Name of the partner
  private String _name;

  // Address of the partner
  private String _address;

  // Valor das compras do parceiro
  private float _buyValue = 0;

  // Valor das vendas pagas pelo parceiro
  private float _saleDoneValue = 0;

  // Valor das vendas do parceiro
  private float _saleValue = 0;

  // Valor total das transações
  private int _totalTransactions = 0;

  // Pontos do parceiro
  private double _points = 0;

  // Current status of the partner
  private PartnerState _state = new NormalState(this);

  private List<Notification> _notifications = new ArrayList<Notification>();


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
   * Simple constructor, used mostly by the user throw the interface
   * 
   * @param id      of the new partnerr
   * @param name    of the new partner
   * @param address of the new partner
   */
  public Partner(String id, String name, String address) {
    _id = id;
    _name = name;
    _address = address;
  }

  public double getPoints(){
    return _points;
  }

  public float getBuyValue() {
    return _buyValue;
  }

  public void incrementTotalTransactions(){
    _totalTransactions ++;
  }

  public int getTotalTransactions(){
    return _totalTransactions;
  }

  public PartnerState getState(){
    return _state;
  }

  public void setSaleValue(float value) {
    _saleValue += value;
  }

  public void setPaidValue(float value) {
    _saleDoneValue += value;
  }


  public void setState(PartnerState state){
    _state = state;
  }

  /**
   * 
   * @return the id of the partner
   */
  public String getId() {
    return _id;
  }

  public void receiveNotification(Notification note) {
    _notifications.add(note);
  }

  public void addPoints(double points) {
    _points += points;
  }

  public void addBuyValue(float value) {
    _buyValue += value;
  }

  public void refreshState(double points, int dateDiff){
    _state.refresh(points, dateDiff);
  }

  @Override
  public String toString() {
    int buyV = Math.round(_buyValue);
    int saleDoneV = Math.round(_saleDoneValue);
    int saleV = Math.round(_saleValue);
    long ps = Math.round(_points);
    return _id + "|" + _name + "|" + _address + "|" + _state.status() + "|" + ps + 
                 "|" + buyV + "|" + saleDoneV + "|" + saleV + "|" + _totalTransactions;
  }

  public String showNotifications() {
    if (_notifications.size() != 0) {
      String res = "";
      for (Notification n : _notifications) {
        res += n.toString() + "\n";
      }
      _notifications.clear();
      res = res.substring(0, res.length() - 1);
      return res;
    }
    else return "null";
  }

  @Override
  public int compareTo(Partner o) {
    CollatorWrapper c = new CollatorWrapper();
    return c.compare(this.getId(), o.getId());
  }

}
