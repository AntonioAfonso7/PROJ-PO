package ggc.app.partners;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.WarehouseManager;
import ggc.app.exceptions.UnknownPartnerKeyException;
import ggc.exceptions.UnknownPartnerKeyException_;

/**
 * Show partner.
 */
class DoShowPartner extends Command<WarehouseManager> {

  DoShowPartner(WarehouseManager receiver) {
    super(Label.SHOW_PARTNER, receiver);
    addStringField("id", Prompt.partnerKey());
  }



  @Override
  public void execute() throws CommandException {
    try{
      String id = stringField("id");
      _display.popup(_receiver.showPartner(id));
      String notifications = _receiver.showPartner(id).showNotifications();
      if (!notifications.equals("null")) {
        _display.popup(notifications);
      }
    }
    catch (UnknownPartnerKeyException_ e) {
      throw new UnknownPartnerKeyException((e.getId()).toString());
    }

  }


}
