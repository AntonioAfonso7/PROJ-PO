package ggc.app.transactions;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.Product;
import ggc.WarehouseManager;
import ggc.app.exceptions.UnavailableProductException;
import ggc.app.exceptions.UnknownPartnerKeyException;
import ggc.app.exceptions.UnknownProductKeyException;
import ggc.exceptions.UnavailableProductException_;
import ggc.exceptions.UnknownPartnerKeyException_;
import ggc.exceptions.UnknownProductKeyException_;


/**
 * Register order.
 */
public class DoRegisterSaleTransaction extends Command<WarehouseManager> {

  public DoRegisterSaleTransaction(WarehouseManager receiver) {
    super(Label.REGISTER_SALE_TRANSACTION, receiver);
    addStringField("partner", Prompt.partnerKey());
    addIntegerField("date", Prompt.paymentDeadline());
    addStringField("product", Prompt.productKey());
    addRealField("amount", Prompt.amount());
  }

  @Override
  public final void execute() throws CommandException {
    String partner = stringField("partner");
    int date = integerField("date");
    String productKey = stringField("product");
    double amount = realField("amount");

    try {
      Product product = _receiver.getProduct(productKey);
      _receiver.registerSaleWarehouse(partner, product, amount, date);
    } catch (UnknownPartnerKeyException_ e) {
      throw new UnknownPartnerKeyException(e.getId());
    } catch (UnknownProductKeyException_ e) {
      throw new UnknownProductKeyException(e.getKey());
    } catch (UnavailableProductException_ e) {
      throw new UnavailableProductException(e.getKey(), e.getRequested(), e.getAvailable());
    }
  }

}
