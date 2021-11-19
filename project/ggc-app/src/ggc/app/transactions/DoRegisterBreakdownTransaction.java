package ggc.app.transactions;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
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
public class DoRegisterBreakdownTransaction extends Command<WarehouseManager> {

  public DoRegisterBreakdownTransaction(WarehouseManager receiver) {
    super(Label.REGISTER_BREAKDOWN_TRANSACTION, receiver);
    addStringField("partner", Prompt.partnerKey());
    addStringField("product", Prompt.productKey());
    addRealField("amount", Prompt.amount());
  }

  @Override
  public final void execute() throws CommandException {
    try {
      _receiver.registerBreakdown(stringField("partner"), stringField("product"), realField("amount"));
    } catch (UnknownPartnerKeyException_ e) {
      throw new UnknownPartnerKeyException(e.getId());
    } catch (UnknownProductKeyException_ e) {
      throw new UnknownProductKeyException(e.getKey());
    } catch (UnavailableProductException_ e) {
      throw new UnavailableProductException(e.getKey(), e.getRequested(), e.getAvailable());
    }
  }

}
