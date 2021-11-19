package ggc.app.partners;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.WarehouseManager;
import ggc.app.exceptions.UnknownPartnerKeyException;
import ggc.app.exceptions.UnknownProductKeyException;
import ggc.exceptions.UnknownPartnerKeyException_;
import ggc.exceptions.UnknownProductKeyException_;
import ggc.notification.Observer;
import ggc.notification.ObserverProd;

/**
 * Toggle product-related notifications.
 */
class DoToggleProductNotifications extends Command<WarehouseManager> {

  DoToggleProductNotifications(WarehouseManager receiver) {
    super(Label.TOGGLE_PRODUCT_NOTIFICATIONS, receiver);
    addStringField("partner", Prompt.partnerKey());
    addStringField("product", Prompt.productKey());
  }

  @Override
  public void execute() throws CommandException {
    String product = stringField("product");
    String partner = stringField("partner");

    try {
      Observer obs = new ObserverProd(_receiver.getProduct(product), _receiver.showPartner(partner));

      _receiver.getProduct(product).toggleObserver(obs);
    } catch (UnknownPartnerKeyException_ e) {
      throw new UnknownPartnerKeyException(e.getId());
    } catch (UnknownProductKeyException_ e) {
      throw new UnknownProductKeyException(e.getKey());
    }
  }

}
