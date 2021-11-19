package ggc;

import java.io.Serializable;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;

import ggc.exceptions.*;
import ggc.notification.ObserverProd;
import ggc.simplefactory.Factory;
import ggc.states.PartnerState;
import ggc.transactions.Aquisition;
import ggc.transactions.Breakdown;
import ggc.transactions.BreakdownRecipe;
import ggc.transactions.Sale;
import ggc.transactions.Transaction;
import ggc.visitState.EliteStateVisitor;
import ggc.visitState.NormalStateVisitor;
import ggc.visitState.SelectionStateVisitor;
import ggc.visitState.VisitorState;
import ggc.visitorTransactions.AquistionVisitor;
import ggc.visitorTransactions.PaidVisitor;
import ggc.visitorTransactions.SaleVisitor;
import ggc.visitorTransactions.Visitor;

/**
 * Class Warehouse implements a warehouse.
 */
public class Warehouse implements Serializable {

  /*
  -------------------------------------- SETTING OF VARIABLES-------------------------------------
  */

  /** Serial number for serialization. */
  private static final long serialVersionUID = 202109192006L;

  // Current date of the warehouse
  private int _date = 0;
  // Current balance of the warehouse (only paid sales)
  private double _balanceReal = 0;
  // Current balance of the warehouse (all sales)
  private double _balanceFake = 0;
  // Current transaction id
  private int _currentTransactionId = 0;
  // List that contains all warehouse's Partners, organized by their IDs
  private Map<String, Partner> _partnerList = new TreeMap<String, Partner>(String.CASE_INSENSITIVE_ORDER);
  // List that contains all warehouse's Batches (Note: we used an ArrayList
  // because a HashMap doens't alow repetited keys)
  private Map<String, List<Batch>> _batchList = new TreeMap<String, List<Batch>>(String.CASE_INSENSITIVE_ORDER);
  // List that contains all known Products of the warehouse
  private Map<String, Product> _productList = new HashMap<String, Product>();
  // List of all transaction ordered by partner
  private Map<String, TreeSet<Transaction>> _transactionPartner = new TreeMap<String, TreeSet<Transaction>>();
  // List of the transactions by id
  private ArrayList<Transaction> _transactions = new ArrayList<Transaction>();

  /*
  ---------------------------------------------------------------------------------------------------------------------
  ---------------------------------------------------------------------------------------------------------------------
  */



  /*
  ---------------------------------------------------------------------------------------------------------------------
  ///////////////////////////////////////////GLOBAL WAREHOUSE FUCNTIONS////////////////////////////////////////////////
  ---------------------------------------------------------------------------------------------------------------------
  */

  public int getDate() {
    return _date;
  }

  public void advanceDate(int days) throws InvalidDateException_ {
    if (days > 0)
      _date += days;
    else
      throw new InvalidDateException_(days);
  }

  public double getBalanceReal() {
    return _balanceReal;
  }

  public double getBalanceFake() {
    return _balanceFake;
  }

  public void updateBalanceFake(float n) {
    _balanceFake += n;
  }

  public void updateBalance(float n) {
    _balanceReal += n;
  }

  /*
  ---------------------------------------------------------------------------------------------------------------------
  ---------------------------------------------------------------------------------------------------------------------
  */

















  /*
  ---------------------------------------------------------------------------------------------------------------------
  //////////////////////////////////////PARTNER FUNCTIONS//////////////////////////////////////////////////////////////
  ---------------------------------------------------------------------------------------------------------------------
  */

  public void addPartner(String key, String name, String address) throws DuplicatePartnerKeyException_ {
    if (_partnerList.containsKey(key)) {
      throw new DuplicatePartnerKeyException_(key, name, address);
    }
    _partnerList.put(key, new Partner(key, name, address));
  }

  public Partner getPartner(String partnerId) throws UnknownPartnerKeyException_ {
    if (_partnerList.containsKey(partnerId))
      return _partnerList.get(partnerId);
    else
      throw new UnknownPartnerKeyException_(partnerId, "", "");
  }

  public Map<String, Partner> partners() {
    return _partnerList;
  }

  public Partner getLowestTransactionPartner (){
    Partner res = null;
    Integer aux = Integer.MAX_VALUE;
    for (Partner p : _partnerList.values()){
      if (p.getTotalTransactions() < aux){
        aux = p.getTotalTransactions();
        res = p;
      }
    }
    return res;
  }

  /*
  ---------------------------------------------------------------------------------------------------------------------
  ---------------------------------------------------------------------------------------------------------------------
  */









  /*
  ---------------------------------------------------------------------------------------------------------------------
  /////////////////////////////////////////////PRODUCT FUNCTIONS///////////////////////////////////////////////////////
  ---------------------------------------------------------------------------------------------------------------------
  */

  public void addProduct(Product product) {
    if (!_productList.containsKey(product.getName())) {
      _productList.put(product.getName(), product);
      for (Partner p : _partnerList.values()) {
        product.addObserver(new ObserverProd(product, p));
      }
    }
  }

  public Map<String, Product> products() {
    return _productList;
  }

  public Product getProduct(String name) throws UnknownProductKeyException_ {
    if (_productList.containsKey(name)) {
      return _productList.get(name);
    } else
      throw new UnknownProductKeyException_(name);
  }

  public Product showLowestProduct(){
    Product res = null;
    float price_low = Float.POSITIVE_INFINITY;
    float price = 0;
    String name;
    for (Product p : _productList.values()){
      name = p.getName();
      for(Batch b : _batchList.get(name)){
        price += (b.getAmount() * b.getPrice());
      }
      if (price < price_low) {
        price_low = price;
        res = p;
      }
      price = 0; //reset the variable value
    }
    return res;

  }

  public Partner showPartnerLowestBuyValue (){
    Partner res = null;
    float lowestBuyValue = Float.POSITIVE_INFINITY;
    for (Partner p : _partnerList.values()){
      if (p.getBuyValue() < lowestBuyValue){
        lowestBuyValue = p.getBuyValue();
        res = p;
      }
    }
    return res;
  }

  public Product showHighestStockProduct(){
    Product res = null;
    Boolean check = true;
    Double aux = 0.0;
    for (Product p1 :_productList.values()){
      if (check){
        aux = p1.getTotalStock();
        check = false;
      }
    for (Product p : _productList.values()){
      if (p.getTotalStock() > aux){
        aux = p.getTotalStock();
        res = p;
      }
    }
      
    }

    return res;
  }


  /*
  ---------------------------------------------------------------------------------------------------------------------
  ---------------------------------------------------------------------------------------------------------------------
  */











  /*
  ---------------------------------------------------------------------------------------------------------------------
  ///////////////////////////////////////////BATCH FUNCTIONS///////////////////////////////////////////////////////////
  ---------------------------------------------------------------------------------------------------------------------
  */

  public Batch getLowestBatch(String product) {
    Batch res = _batchList.get(product).get(0);
    for (Batch b : _batchList.get(product)) {
      if (res.getPrice() > b.getPrice()) {
        res = b;
      }
    }
    return res;
  }

  public void addBatch(Partner supplier, double amount, float price, Product product) {
    if (_batchList.containsKey(product.getName())) {
      List<Batch> products = _batchList.get(product.getName());
      products.add(new Batch(supplier, amount, price, product));
      product.update(amount, price);
      Collections.sort(products);
    } else {
      List<Batch> products = new ArrayList<Batch>();
      products.add(new Batch(supplier, amount, price, product));
      _batchList.put(product.getName(), products);
    }
  }

  public Map<String, List<Batch>> batches() {
    return _batchList;
  }

  public List<Batch> getBatchesByProduct(String name) throws UnknownProductKeyException_ {
    if (_batchList.containsKey(name)) {
      return _batchList.get(name);
    } else
      throw new UnknownProductKeyException_(name);
  }

  public Set<Batch> getBatchesByPartner(String name) throws UnknownPartnerKeyException_ {
    if (_partnerList.containsKey(name)) {
      Set<Batch> lst = new TreeSet<Batch>();
      for (List<Batch> b : _batchList.values()) {
        for (Batch x : b) {
          if (x.getSupplier().getId().equals(name)) {
            lst.add(x);
          }
        }
      }
      return lst;
    } else
      throw new UnknownPartnerKeyException_(name, "", "");
  }

  public List<Batch> getBatchUnderPrice(float price) {
    List<Batch> res = new ArrayList<Batch>();

    for (List<Batch> lst : _batchList.values()) {
      for (Batch b : lst) {
        if (b.underPrice(price)) {
          res.add(b);
        }
      }
    }
    return res;
  }

  /*
  ---------------------------------------------------------------------------------------------------------------------
  ---------------------------------------------------------------------------------------------------------------------
  







  /*
  ---------------------------------------------------------------------------------------------------------------------
  //////////////////////////////////////TRANSACTION FUNCTIONS//////////////////////////////////////////////////////////
  ---------------------------------------------------------------------------------------------------------------------
  */
  

  public Transaction getTransaction(int id) throws UnknownTransactionIdException_ {
    if (id < _transactions.size()){
      // creating some variables for better understanding 
      Transaction t = _transactions.get(id);
      int date = getDate();
      int paymentDate = t.getPaymentDate();
      int dateDiff = paymentDate - date;
      float price = t.getPrice(); 
      Partner p = t.getPartner();
      PartnerState s = p.getState();
      Product prod = t.getProduct();

      VisitorState vNormal = new NormalStateVisitor();
      VisitorState vSelection = new SelectionStateVisitor();
      VisitorState vElite = new EliteStateVisitor();

      // defining the real price of the Transaction
      if (s.accept(vNormal, dateDiff, price, prod) != 0) {
        t.setRealPrice(s.accept(vNormal, dateDiff, price, prod));
      }
      if (s.accept(vSelection, dateDiff, price, prod) != 0) {
        t.setRealPrice(s.accept(vSelection, dateDiff, price, prod));
      }
      if (s.accept(vElite, dateDiff, price, prod) != 0) {
        t.setRealPrice(s.accept(vElite, dateDiff, price, prod));
      }
      return _transactions.get(id);
    }
    else
      throw new UnknownTransactionIdException_(id);
  }

  public void registerPayment(Transaction t) {

    int date = getDate();
    int paymentDate = t.getPaymentDate();
    int dateDiff = paymentDate - date;
    float price = t.getPrice(); 
    Partner partner = t.getPartner();
    PartnerState s = partner.getState();
    Product prod = t.getProduct();

    Visitor v = new SaleVisitor();
    VisitorState vNormal = new NormalStateVisitor();
    VisitorState vSelection = new SelectionStateVisitor();
    VisitorState vElite = new EliteStateVisitor();

    // handle the different transaction types

    // SALE
    if (t.accept(v) != null) {
      Sale sale = (Sale) t;
      sale.pay();
      sale.setPayDate(getDate());
      if (s.accept(vNormal, dateDiff, price, prod) != 0) {
        updateBalance(s.accept(vNormal, dateDiff, price, prod));  
        t.getPartner().setPaidValue(price);
        t.setRealPrice(s.accept(vNormal, dateDiff, price, prod));
      }
      if (s.accept(vSelection, dateDiff, price, prod) != 0) {
        updateBalance(s.accept(vSelection, dateDiff, price, prod));
        t.getPartner().setPaidValue(price);
        t.setRealPrice(s.accept(vSelection, dateDiff, price, prod));
      }
      if (s.accept(vElite, dateDiff, price, prod) != 0) {
        updateBalance(s.accept(vElite, dateDiff, price, prod));
        t.getPartner().setPaidValue(price);
        t.setRealPrice(s.accept(vElite, dateDiff, price, prod));
      }
    }
  }

  public List<Transaction> getPaidPartner(String partner) throws UnknownPartnerKeyException_ {
    if (_partnerList.containsKey(partner)) {
      List<Transaction> res = new ArrayList<Transaction>();
      if (_transactionPartner.containsKey(partner)) {
        Set<Transaction> trans = _transactionPartner.get(partner);
        Visitor vis = new PaidVisitor();

        for (Transaction t : trans) {
          t = t.accept(vis);
          if (t != null)
            res.add(t);
        }
      }
      return res;
    } else
      throw new UnknownPartnerKeyException_(partner, "", "");
  }

  /*
  ---------------------------------------------------------------------------------------------------------------------
  ---------------------------------------------------------------------------------------------------------------------
  */













  /*
  ---------------------------------------------------------------------------------------------------------------------
  /////////////////////////////////////////ACQUISTION FUNCTIONS////////////////////////////////////////////////////////
  ---------------------------------------------------------------------------------------------------------------------
  */

  public void registerAquisition(String supplier, Product product, double amount, float price)
      throws UnknownPartnerKeyException_ {
    if (_partnerList.containsKey(supplier)) {
      // Creation of variables for better understanding
      Transaction trans = new Aquisition(_partnerList.get(supplier), product, amount, price, _currentTransactionId++,
          getDate());

      _transactions.add(trans); // Add the transaction to the list of all transactions
      _partnerList.get(supplier).incrementTotalTransactions();


      // Add the transaction to the list of transaction, organized by partner
      if (_transactionPartner.containsKey(supplier)) {
        _transactionPartner.get(supplier).add(trans);
      } else {
        TreeSet<Transaction> lst = new TreeSet<Transaction>();
        lst.add(trans);
        _partnerList.get(supplier).incrementTotalTransactions();
        _transactionPartner.put(supplier, lst);
      }

      // Add the correspondent batch of the product to batch list and product list
      addBatch(_partnerList.get(supplier), amount, price, product);
      _partnerList.get(supplier).addBuyValue((float) amount * price);
      addProduct(product);

      updateBalance((float) amount * price * -1);
      updateBalanceFake((float) amount * price * -1);
    } else
      throw new UnknownPartnerKeyException_(supplier, "", "");
  }

/* 
-----------------------------------------------------------------------------------------------------------------------
-----------------------------------------------------------------------------------------------------------------------
*/













/*
  ---------------------------------------------------------------------------------------------------------------------
  ////////////////////////////////////////////ASSEMBLE FUNCTIONS///////////////////////////////////////////////////////
  ---------------------------------------------------------------------------------------------------------------------
  */

  public float assembleProduct(Recipe recipe, Double amount) throws UnavailableProductException_ {
    LinkedHashMap<Product, Integer> lst = recipe.getRecipe();
    float price = 0;
    for (Product p : lst.keySet()) {
      if (p.getTotalStock() < amount * lst.get(p)) {
        throw new UnavailableProductException_(p.getName(), (int) (amount * lst.get(p)), p.getTotalStock().intValue());
      }
    }
    
    for (Product p : lst.keySet()) {
      Batch b = getLowestBatch(p.getName());
      price += b.getPrice() * lst.get(p);
      b.removeAmount(amount * lst.get(p));
      p.removeTotalStock(amount * lst.get(p));
      if (b.getAmount() == 0) {
        _batchList.get(p.getName()).remove(b);
      }
    }

    return price;
  }



  public List<Transaction> getAquisitionsPartner(String partner) throws UnknownPartnerKeyException_ {
    if (_partnerList.containsKey(partner)) {
      List<Transaction> res = new ArrayList<Transaction>();
      if (_transactionPartner.containsKey(partner)) {
        Set<Transaction> trans = _transactionPartner.get(partner);
        Visitor vis = new AquistionVisitor();

        for (Transaction t : trans) {
          t = t.accept(vis);
          if (t != null)
            res.add(t);
        }
      }
      return res;
    } else
      throw new UnknownPartnerKeyException_(partner, "", "");
  }

  /*
  ---------------------------------------------------------------------------------------------------------------------
  ---------------------------------------------------------------------------------------------------------------------
  */











  /*
  ---------------------------------------------------------------------------------------------------------------------
  ////////////////////////////////////////////////SALE FUNCTIONS///////////////////////////////////////////////////////
  ---------------------------------------------------------------------------------------------------------------------
  */

  public void registerSale(String supplier, Product product, Double amount, int limitDate)
      throws UnknownPartnerKeyException_, UnknownProductKeyException_, UnavailableProductException_ {

    double amountStat = amount; // Variable for safe keeping
    if (_partnerList.containsKey(supplier)) {
      // if there's not enough amount of product in the warehouse, the method does
      // nothing
      if (getProduct(product.getName()).getTotalStock() < amount) {
        if (!product.isSimple()) {
          DerivedProduct derived = (DerivedProduct) product;
          Recipe recipe = derived.getRecipe();
          float price = 0;
          float assemblePrice = 0;
          
          amount -= product.getTotalStock();
        
          assemblePrice += assembleProduct(recipe, amount);
          assemblePrice = assemblePrice * (1 + recipe.getRate());
          price = assemblePrice * amount.floatValue();

          price += product.getLowestPrice() * product.getTotalStock();

          addBatch(_partnerList.get(supplier), amount, assemblePrice, product);

          int dateDiff = limitDate - getDate();

          Transaction trans = new Sale(_partnerList.get(supplier), product, amountStat, price, limitDate,
              _currentTransactionId++);
          getPartner(supplier).refreshState(10 * price, dateDiff);
          getPartner(supplier).setSaleValue(price);
          _transactions.add(trans); // Add the transaction to the list of all transactions
          _partnerList.get(supplier).incrementTotalTransactions();
          // Add the transaction to the list of transaction, organized by partner
          if (_transactionPartner.containsKey(supplier)) {
            _transactionPartner.get(supplier).add(trans);
          } else {
            TreeSet<Transaction> lst = new TreeSet<Transaction>();
            lst.add(trans);
            _partnerList.get(supplier).incrementTotalTransactions();
            _transactionPartner.put(supplier, lst);
          }
          product.removeTotalStock(amount);

        } else {
          Double available = product.getTotalStock();
          throw new UnavailableProductException_(product.getName(), amount.intValue(), available.intValue());
        }
      }
      // while there's amount, we deduct the batches' amount until there's no more amount
      List<Batch> batches = _batchList.get(product.getName());
      List<Batch> aux = new ArrayList<Batch>();
      float price = 0;
      while (amount > 0) {
        Batch b = getLowestBatch(product.getName());
        if (b.getAmount() <= amount) {
          amount -= b.getAmount();
          product.removeTotalStock(b.getAmount());
          price += b.getPrice() * b.getAmount();
        } else if (amount != 0) {
          b.removeAmount(amount);
          product.removeTotalStock(amount);
          price += amount * b.getPrice();
          aux.add(b);
          amount = 0.0;
        } else {
          aux.add(b);
        }
      }

      // Replaces the Batch list by the new value without empty Batches
      _batchList.replace(product.getName(), batches, aux);

      // Creation of variables for better understanding
      int dateDiff = limitDate - getDate();

      Transaction trans = new Sale(_partnerList.get(supplier), product, amountStat, price, limitDate,
          _currentTransactionId++);
      getPartner(supplier).refreshState(10 * price, dateDiff);
      getPartner(supplier).setSaleValue(price);
      _transactions.add(trans); // Add the transaction to the list of all transactions
      _partnerList.get(supplier).incrementTotalTransactions();
  

      // Add the transaction to the list of transaction, organized by partner
      if (_transactionPartner.containsKey(supplier)) {
        _transactionPartner.get(supplier).add(trans);
      } else {
        TreeSet<Transaction> lst = new TreeSet<Transaction>();
        lst.add(trans);
        _partnerList.get(supplier).incrementTotalTransactions();
        _transactionPartner.put(supplier, lst);
      }
    } else
      throw new UnknownPartnerKeyException_(supplier, "", "");
  }


  public List<Transaction> getSalePartner(String partner) throws UnknownPartnerKeyException_ {
    VisitorState vNormal = new NormalStateVisitor();
    VisitorState vSelection = new SelectionStateVisitor();
    VisitorState vElite = new EliteStateVisitor();

    if (_partnerList.containsKey(partner)) {
      List<Transaction> res = new ArrayList<Transaction>();
      if (_transactionPartner.containsKey(partner)) {
        Set<Transaction> trans = _transactionPartner.get(partner);
        Visitor vis = new SaleVisitor();

        for (Transaction t : trans) {
          t = t.accept(vis);
          if (t == null) {
            continue;
          }
          // creating some variables for better understanding 
          int date = getDate();
          int paymentDate = t.getPaymentDate();
          int dateDiff = paymentDate - date;
          float price = t.getPrice(); 
          Partner p = t.getPartner();
          PartnerState s = p.getState();
          Product prod = t.getProduct();

          // defining the real price of the Transaction
          if (s.accept(vNormal, dateDiff, price, prod) != 0) {
            t.setRealPrice(s.accept(vNormal, dateDiff, price, prod));
          }
          if (s.accept(vSelection, dateDiff, price, prod) != 0) {
            t.setRealPrice(s.accept(vSelection, dateDiff, price, prod));
          }
          if (s.accept(vElite, dateDiff, price, prod) != 0) {
            t.setRealPrice(s.accept(vElite, dateDiff, price, prod));
          }
        
          if (t != null)
            res.add(t);
        }
      }

      return res;
    } else
      throw new UnknownPartnerKeyException_(partner, "", "");
  }
  /*
  ---------------------------------------------------------------------------------------------------------------------
  ---------------------------------------------------------------------------------------------------------------------
  */












  /*
  ---------------------------------------------------------------------------------------------------------------------
  //////////////////////////////////////BREAKDOWN FUNCTIONS////////////////////////////////////////////////////////////
  ---------------------------------------------------------------------------------------------------------------------
  */
  public void registerBreakdown(String p1, Partner partner, Double amount)
      throws UnknownPartnerKeyException_, UnknownProductKeyException_, UnavailableProductException_ {
    if (_partnerList.containsValue(partner)) {
      if (_productList.containsKey(p1)) {
        Product p2 = _productList.get(p1);
        if (!p2.isSimple()) {
          DerivedProduct product = (DerivedProduct) p2;
          if (amount <= product.getTotalStock()) {

            float price = 0;
            float finalPrice;
            Batch derived = getLowestBatch(product.getName());

            List<Product> products = product.breakdown();
            TreeMap<Product, Double> amounts = new TreeMap<Product, Double>();
            TreeMap<Product, Float> prices = new TreeMap<Product, Float>();

            for (Product p : products) {
              if (p.getTotalStock() != 0) {
                _batchList.get(p.getName()).add(new Batch(partner, amount, p.getLowestPrice(), p));
                price += p.getLowestPrice() * amount;
                amounts.put(p, amount * product.getRecipe().getRecipe().get(p));
                prices.put(p, amount.floatValue() * product.getRecipe().getRecipe().get(p) * p.getLowestPrice());
              } else {
                _batchList.get(p.getName()).add(new Batch(partner, amount, p.getHighestPrice(), p));
                price += p.getHighestPrice() * amount;
                amounts.put(p, amount);
                prices.put(p, p.getHighestPrice());
              }
            }

            finalPrice = (derived.getPrice() * amount.floatValue()) - price;

            if (finalPrice > 0) {
              updateBalance(finalPrice);
              updateBalanceFake(finalPrice);
            }

            BreakdownRecipe recipe = new BreakdownRecipe(amounts, prices);

            Transaction trans = new Breakdown(partner, product, recipe, amount, finalPrice, _currentTransactionId++);
            _transactions.add(trans);
            partner.incrementTotalTransactions();
            if (_transactionPartner.containsKey(partner.getId())) {
              _transactionPartner.get(partner.getId()).add(trans);
            } else {
              TreeSet<Transaction> lst = new TreeSet<Transaction>();
              lst.add(trans);
              partner.incrementTotalTransactions();
              _transactionPartner.put(partner.getId(), lst);
            }
            derived.removeAmount(amount);
          } else
            throw new UnavailableProductException_(product.getName(), amount.intValue(),
                product.getTotalStock().intValue());
        }
      } else
        throw new UnknownProductKeyException_(p1);
    } else
      throw new UnknownPartnerKeyException_(partner.getId(), "", "");
  }

  /*
  ---------------------------------------------------------------------------------------------------------------------
  ---------------------------------------------------------------------------------------------------------------------
  */



















  /*
  ---------------------------------------------------------------------------------------------------------------------
  //////////////////////////////////////CHECKERS FUNCTIONS////////////////////////////////////////////////////////////
  ---------------------------------------------------------------------------------------------------------------------
  */

  public boolean unknownProduct(String name) {
    return !_productList.containsKey(name);
  }

  void importFile(String txtfile) throws IOException, BadEntryException {
    try (BufferedReader in = new BufferedReader(new FileReader(txtfile))) {
      String s;
      Factory factory = new Factory(this);
      while ((s = in.readLine()) != null) {
        String line = new String(s.getBytes(), "UTF-8");
        factory.processLine(line);
      }
    } catch (IOException e) {
      e.printStackTrace();
    } catch (BadEntryException e) {
      e.printStackTrace();
    }
  }
}

  /*
  ---------------------------------------------------------------------------------------------------------------------
  ---------------------------------------------------------------------------------------------------------------------
  */