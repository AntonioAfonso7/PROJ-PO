package ggc.app.products;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

import java.util.Set;

import ggc.WarehouseManager;
import ggc.app.exceptions.UnknownPartnerKeyException;
import ggc.exceptions.UnknownPartnerKeyException_;

/**
 * Show batches supplied by partner.
 */
class DoShowBatchesByPartner extends Command<WarehouseManager> {

  DoShowBatchesByPartner(WarehouseManager receiver) {
    super(Label.SHOW_BATCHES_SUPPLIED_BY_PARTNER, receiver);  
    addStringField("partner", Prompt.partnerKey());
  }

  @Override
  public final void execute() throws CommandException {
    try {
      Set<?> lst = _receiver.showBatchesByPartner(stringField("partner"));

      for(Object o : lst) {
        _display.popup(o);
      }

    } catch (UnknownPartnerKeyException_ e) {
      throw new UnknownPartnerKeyException(e.getId());
    }
  }

}
