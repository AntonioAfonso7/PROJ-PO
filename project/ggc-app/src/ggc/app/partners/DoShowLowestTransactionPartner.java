package ggc.app.partners;

import ggc.WarehouseManager;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

public class DoShowLowestTransactionPartner extends Command<WarehouseManager> {
    
    public DoShowLowestTransactionPartner(WarehouseManager receiver) {
        super(Label.SHOW_LOWEST_TRANSACTION_PARTNER, receiver);
    }

    @Override
    protected void execute() throws CommandException {
        _display.popup(_receiver.getLowestTransactionPartner());
    }

    
}
