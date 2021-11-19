package ggc;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import javax.swing.text.html.HTMLDocument.HTMLReader.ParagraphAction;

import ggc.exceptions.BadEntryException;
import ggc.exceptions.DuplicatePartnerKeyException_;
import ggc.exceptions.ImportFileException;
import ggc.exceptions.InvalidDateException_;
import ggc.exceptions.MissingFileAssociationException;
import ggc.exceptions.UnavailableFileException;
import ggc.exceptions.UnavailableProductException_;
import ggc.exceptions.UnknownPartnerKeyException_;
import ggc.exceptions.UnknownProductKeyException_;
import ggc.exceptions.UnknownTransactionIdException_;

import ggc.transactions.Transaction;

/** Façade for access. */
public class WarehouseManager {

  /** Name of file storing current store. */
  private String _filename = "";

  /** The warehouse itself. */
  private Warehouse _warehouse = new Warehouse();








  /*
  ---------------------------------------------------------------------------------------------------------------------
  /////////////////////////////////////////////DATE FUNCTIONS//////////////////////////////////////////////////////////
  ---------------------------------------------------------------------------------------------------------------------
  */


  /**
   * 
   * @return date associated to the warehouse
   */
  public int getDate() {
    return _warehouse.getDate();
  }

  /**
   * 
   * @param days to advance
   * @throws InvalidDateException_
   */
  public void advanceDate(int days) throws InvalidDateException_ {
    _warehouse.advanceDate(days);
  }

   /*
  ---------------------------------------------------------------------------------------------------------------------
  ---------------------------------------------------------------------------------------------------------------------
  */





   /*
  ---------------------------------------------------------------------------------------------------------------------
  ///////////////////////////////////////////BALANCE FUNCTIONS/////////////////////////////////////////////////////////
  ---------------------------------------------------------------------------------------------------------------------
  */
  public double showBalanceReal() {
    return _warehouse.getBalanceReal();
  }

  public double showBalanceFake() {
    return _warehouse.getBalanceFake();
  }

   /*
  ---------------------------------------------------------------------------------------------------------------------
  ---------------------------------------------------------------------------------------------------------------------
  */




  /*
  ---------------------------------------------------------------------------------------------------------------------
  //////////////////////////////////////PARTNER FUNCTIONS////////////////////////////////////////////////////////////
  ---------------------------------------------------------------------------------------------------------------------
  */
  /**
   * 
   * @param key     id of the new partner
   * @param name    of the new partner
   * @param address of the new partner
   * @throws DuplicatePartnerKeyException_
   */
  public void registerPartner(String key, String name, String address) throws DuplicatePartnerKeyException_ {
    _warehouse.addPartner(key, name, address);
  }

  /**
   * 
   * @param id of the desired partner
   * @return the desired partner
   * @throws UnknownPartnerKeyException_
   */
  public Partner showPartner(String id) throws UnknownPartnerKeyException_ {
    return _warehouse.getPartner(id); // Solucao possivel compareToIgnoreCase
  }

  /**
   * 
   * @return list of all the partners associated with the warehouse
   */
  public List<Partner> showPartners() {
    List<Partner> lst = new ArrayList<Partner>(_warehouse.partners().values());
    Collections.sort(lst);
    return lst;
  }

  public Partner showPartnerLowestBuyValue(){
    return _warehouse.showPartnerLowestBuyValue();
  }

  public Partner getLowestTransactionPartner(){
    return _warehouse.getLowestTransactionPartner();
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

  /**
   * 
   * @return list of all the batches associated with the warehouse
   */
  public List<Batch> showBatches() {

    List<Batch> lst = new ArrayList<Batch>();
    List<String> names = new ArrayList<String>(_warehouse.batches().keySet());

    for (int i = 0; i < names.size(); i++) {
      lst.addAll(_warehouse.batches().get(names.get(i)));
    }
    return lst;
  }

  public List<Batch> showBatchesByProduct(String product) throws UnknownProductKeyException_ {
    return _warehouse.getBatchesByProduct(product);
  }

  public Set<Batch> showBatchesByPartner(String partner) throws UnknownPartnerKeyException_ {
    return _warehouse.getBatchesByPartner(partner);
  }

   /*
  ---------------------------------------------------------------------------------------------------------------------
  ---------------------------------------------------------------------------------------------------------------------
  */












   /*
  ---------------------------------------------------------------------------------------------------------------------
  //////////////////////////////////////PRODUCT FUNCTIONS////////////////////////////////////////////////////////////
  ---------------------------------------------------------------------------------------------------------------------
  */

  /**
   * 
   * @return list of all the products in the warehouse
   */
  public List<Product> showProducts() {
    List<Product> lst = new ArrayList<Product>(_warehouse.products().values());
    Collections.sort(lst);
    return lst;
  }

  public Product getProduct(String name) throws UnknownProductKeyException_ {
    return _warehouse.getProduct(name);
  }

  public List<Batch> getBatchesUnderPrice(float price) {
    return _warehouse.getBatchUnderPrice(price);
  }

  public Product showLowestProduct(){
    return _warehouse.showLowestProduct();
  }

  public Product showHighestStockProduct(){
    return _warehouse.showHighestStockProduct();
  }
   /*
  ---------------------------------------------------------------------------------------------------------------------
  ---------------------------------------------------------------------------------------------------------------------
  */









   /*
  ---------------------------------------------------------------------------------------------------------------------
  //////////////////////////////////////TRANSACTION FUNCTIONS//////////////////////////////////////////////////////////
  ---------------------------------------------------------------------------------------------------------------------
  */

  public Transaction getTransaction(int id) throws UnknownTransactionIdException_ {
    return _warehouse.getTransaction(id);
  }

  public void registerPayment(int t) throws UnknownTransactionIdException_ {
    if (t >= 0) {
      Transaction trans = _warehouse.getTransaction(t);
      _warehouse.registerPayment(trans);
    }
    else throw new UnknownTransactionIdException_(t);
  }

  public void registerPaidPartner(String p) throws UnknownPartnerKeyException_{
    _warehouse.getPaidPartner(p);
  }
  

  public List<Transaction> getPaidPartner(String name) throws UnknownPartnerKeyException_ {
    return _warehouse.getPaidPartner(name);
  }

  /*
  ---------------------------------------------------------------------------------------------------------------------
  ---------------------------------------------------------------------------------------------------------------------
  */









  /*
  ---------------------------------------------------------------------------------------------------------------------
  //////////////////////////////////////ACQUISITION FUNCTIONS////////////////////////////////////////////////////////////
  ---------------------------------------------------------------------------------------------------------------------
  */
  public void registerAquisitionSimple(String supplier, String product, float price, double amount) throws UnknownPartnerKeyException_ {
    try{
        Product p = _warehouse.getProduct(product);
        _warehouse.registerAquisition(supplier, p, amount, price);
    } catch (UnknownProductKeyException_ e) {
      _warehouse.registerAquisition(supplier, new Product(product, price, amount), amount, price);
    }
  }

  public void registerAquisitionDerived(String supplier, String product, float price, double amount, float rate,
      LinkedHashMap<Product, Integer> recipe) throws UnknownPartnerKeyException_ {
    DerivedProduct p = new DerivedProduct(product, amount, price, new Recipe(recipe, rate));
    _warehouse.registerAquisition(supplier, p, amount, price);
  }

  public List<Transaction> getAquisitionParnter(String name) throws UnknownPartnerKeyException_ {
    return _warehouse.getAquisitionsPartner(name);
  }

  /*
  ---------------------------------------------------------------------------------------------------------------------
  ---------------------------------------------------------------------------------------------------------------------
  */







  /*
  ---------------------------------------------------------------------------------------------------------------------
  //////////////////////////////////////SALE FUNCTIONS////////////////////////////////////////////////////////////
  ---------------------------------------------------------------------------------------------------------------------
  */

  public void registerSaleWarehouse(String supplier, Product product, double amount, int limitDate)
      throws UnknownPartnerKeyException_, UnavailableProductException_, UnknownProductKeyException_ {
    _warehouse.registerSale(supplier, product, amount, limitDate);
  }



  public List<Transaction> getSaleParnter(String name) throws UnknownPartnerKeyException_ {
    return _warehouse.getSalePartner(name);
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

  public void registerBreakdown(String partner, String product, double amount) throws UnknownPartnerKeyException_, UnknownProductKeyException_, UnavailableProductException_ {
    _warehouse.registerBreakdown(product, _warehouse.getPartner(partner), (Double)amount);
  }


  /*
  ---------------------------------------------------------------------------------------------------------------------
  ---------------------------------------------------------------------------------------------------------------------
  */









 /*
  ---------------------------------------------------------------------------------------------------------------------
  //////////////////////////////////////UTILITIES FUNCTIONS////////////////////////////////////////////////////////////
  ---------------------------------------------------------------------------------------------------------------------
  */

  /**
   * @return wether ther is a file already associated with the warehous or not
   */
  public boolean hasFile() {
    return !(_filename.equals(""));
  }

  public boolean hasNoChanges() {
    boolean res = false;
    if (hasFile()) {
      try (ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(_filename)))) {
        if (_warehouse == (Warehouse) ois.readObject()) {
          res = true;
        }
        ois.close();
      } catch (IOException e) {
        e.printStackTrace();
      } catch (ClassNotFoundException e) {
        e.printStackTrace();
      }
    }
    return res;
  }

  public boolean unknownProduct(String name) {
    return _warehouse.unknownProduct(name);
  }
   /*
  ---------------------------------------------------------------------------------------------------------------------
  ---------------------------------------------------------------------------------------------------------------------
  */







  /*
  ---------------------------------------------------------------------------------------------------------------------
  //////////////////////////////////////SAVE AND LOAD/IMPORT FUNCTIONS/////////////////////////////////////////////////
  ---------------------------------------------------------------------------------------------------------------------
  */

  /**
   * @@throws IOException
   * @@throws FileNotFoundException
   * @@throws MissingFileAssociationException
   */
  public void save() throws IOException, FileNotFoundException, MissingFileAssociationException {
    try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(_filename))) {
      out.writeObject(this._warehouse);
      out.close();
    }
  }

  /**
   * @@param filename
   * @@throws MissingFileAssociationException
   * @@throws IOException
   * @@throws FileNotFoundException
   */
  public void saveAs(String filename) throws MissingFileAssociationException, FileNotFoundException, IOException {
    _filename = filename;
    save();
  }

  /**
   * @@param filename
   * @@throws UnavailableFileException
   */
  public void load(String filename) throws UnavailableFileException {
    try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
      _filename = filename;
      _warehouse = (Warehouse) in.readObject();

      in.close();

    } catch (FileNotFoundException e) {
      throw new UnavailableFileException(filename);
    } catch (IOException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
  }

  /**
   * @param textfile
   * @throws ImportFileException
   */
  public void importFile(String textfile) throws ImportFileException {
    try {
      _warehouse.importFile(textfile);
    } catch (IOException | BadEntryException e) {
      throw new ImportFileException(textfile);
    }
  }
}

 /*
  ---------------------------------------------------------------------------------------------------------------------
  ---------------------------------------------------------------------------------------------------------------------
  */