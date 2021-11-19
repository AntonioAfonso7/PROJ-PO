package ggc.app.products;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

import java.util.List;

import ggc.Batch;
import ggc.WarehouseManager;
import ggc.app.exceptions.UnknownProductKeyException;
import ggc.exceptions.UnknownProductKeyException_;

/**
 * Show all products.
 */
class DoShowBatchesByProduct extends Command<WarehouseManager> {

  DoShowBatchesByProduct(WarehouseManager receiver) {
    super(Label.SHOW_BATCHES_BY_PRODUCT, receiver);
    addStringField("name", Prompt.productKey());  }

  @Override
  public final void execute() throws CommandException {
    try {
      List<Batch> lst = _receiver.showBatchesByProduct(stringField("name"));

      for (Batch b : lst) {
        _display.popup(b);
      }

    } catch ( UnknownProductKeyException_ e) {
      throw new UnknownProductKeyException(e.getKey());
    }  
  }
}
