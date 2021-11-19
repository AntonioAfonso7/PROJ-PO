package ggc.app.products;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.WarehouseManager;


public class DoShowHighestStockProduct extends Command<WarehouseManager> {

    public DoShowHighestStockProduct(WarehouseManager receiver) {
        super(Label.SHOW_HIGHEST_STOCK_PRODUCT, receiver);
    }

    @Override
    protected void execute() throws CommandException {
        _display.popup(_receiver.showHighestStockProduct());
        
    }
    
}
