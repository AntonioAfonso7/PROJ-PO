package ggc.app.transactions;

import java.util.LinkedHashMap;

import ggc.Product;
import ggc.WarehouseManager;
import ggc.app.exceptions.UnknownPartnerKeyException;
import ggc.app.exceptions.UnknownProductKeyException;
import ggc.exceptions.UnknownPartnerKeyException_;
import ggc.exceptions.UnknownProductKeyException_;
import pt.tecnico.uilib.forms.Form;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

/**
 * Register order.
 */
public class DoRegisterAcquisitionTransaction extends Command<WarehouseManager> {

  public DoRegisterAcquisitionTransaction(WarehouseManager receiver) {
    super(Label.REGISTER_ACQUISITION_TRANSACTION, receiver);
    addStringField("supplier", Prompt.partnerKey());
    addStringField("product", Prompt.productKey());
    addStringField("price", Prompt.price());
    addRealField("amount", Prompt.amount());
  }

  @Override
  public final void execute() throws CommandException {
    try {
      String supplier = stringField("supplier");
      String product = stringField("product");
      float price = Float.parseFloat(stringField("price"));
      double amount = realField("amount");

      if (_receiver.unknownProduct(product)) {
        if (Form.requestString(Prompt.addRecipe()).equals("s")) {

          int nElemnts = Form.requestInteger(Prompt.numberOfComponents());
          LinkedHashMap<Product, Integer> recipe = new LinkedHashMap<Product, Integer>();
          float rate = Float.parseFloat(Form.requestString(Prompt.alpha()));
          
          for (int i = 0; i < nElemnts; i++) {
            recipe.put(_receiver.getProduct(Form.requestString(Prompt.productKey())),
                Form.requestInteger(Prompt.amount()));
          }
          _receiver.registerAquisitionDerived(supplier, product, price, amount, rate, recipe);
        } else {
          _receiver.registerAquisitionSimple(supplier, product, price, amount);
        }
      } else {
        _receiver.registerAquisitionSimple(supplier, product, price, amount);
      }

    } catch (UnknownPartnerKeyException_ e) {
      throw new UnknownPartnerKeyException(e.getId());
    } catch (UnknownProductKeyException_ e) {
      throw new UnknownProductKeyException(e.getKey());
    }
  }

}
