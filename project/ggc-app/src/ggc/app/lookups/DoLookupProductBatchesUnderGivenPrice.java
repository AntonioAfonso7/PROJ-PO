package ggc.app.lookups;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

import ggc.WarehouseManager;
/**
 * Lookup products cheaper than a given price.
 */
public class DoLookupProductBatchesUnderGivenPrice extends Command<WarehouseManager> {

  public DoLookupProductBatchesUnderGivenPrice(WarehouseManager receiver) {
    super(Label.PRODUCTS_UNDER_PRICE, receiver);
    addStringField("price", Prompt.priceLimit());
  }

  @Override
  public void execute() throws CommandException {
    float price = Float.parseFloat(stringField("price"));
    for ( Object o : _receiver.getBatchesUnderPrice(price)) {
      _display.popup(o);
    }
  }
}
