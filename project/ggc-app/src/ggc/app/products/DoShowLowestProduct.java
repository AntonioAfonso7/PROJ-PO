package ggc.app.products;

import ggc.WarehouseManager;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

public class DoShowLowestProduct extends Command<WarehouseManager> {

    DoShowLowestProduct(WarehouseManager receiver){
        super(Label.SHOW_LOWEST_PRODUCT, receiver);
    }
    

    @Override
    protected void execute() throws CommandException {
        _display.popup(_receiver.showLowestProduct());
    }
    
}