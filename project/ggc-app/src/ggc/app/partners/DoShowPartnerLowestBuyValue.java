package ggc.app.partners;

import ggc.WarehouseManager;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

public class DoShowPartnerLowestBuyValue extends Command<WarehouseManager> {
    DoShowPartnerLowestBuyValue(WarehouseManager receiver){
        super(Label.SHOW_LOWEST_BUY_VALUE, receiver);
    }

    @Override
    protected void execute() throws CommandException {
        _display.popup( _receiver.showPartnerLowestBuyValue() );        
    }
}


