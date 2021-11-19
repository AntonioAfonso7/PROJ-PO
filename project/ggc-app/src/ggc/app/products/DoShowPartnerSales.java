package ggc.app.partners;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

import java.util.List;

import ggc.WarehouseManager;
import ggc.app.exceptions.UnknownPartnerKeyException;
  import ggc.exceptions.UnknownPartnerKeyException_;

/**
 * Show all transactions for a specific partner.
 */
class DoShowPartnerSales extends Command<WarehouseManager> {

  DoShowPartnerSales(WarehouseManager receiver) {
    super(Label.SHOW_PARTNER_SALES, receiver);
    addStringField("partner", Prompt.partnerKey());
  }

  @Override
  public void execute() throws CommandException {
    try {
      List<?> lst = _receiver.getSaleParnter(stringField("partner"));

      for (Object o : lst) {
        _display.popup(o);
      }

    } catch (UnknownPartnerKeyException_ e) {
      throw new UnknownPartnerKeyException(e.getId());
    }
  }

}
