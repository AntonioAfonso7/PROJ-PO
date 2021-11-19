package ggc.simplefactory;

import ggc.Warehouse;
import ggc.exceptions.BadEntryException;

public class Factory {
    private Warehouse _warehouse;

    public Factory(Warehouse warehouse) {
        _warehouse = warehouse;
    }

    public void processLine(String line) throws BadEntryException {
      String[] fields = line.split("\\|");

      switch (fields[0]) {
      case "PARTNER" -> registerPartner(fields[1], fields[2], fields[3]);
      case "BATCH_S" -> registerBatch(fields[1], fields[2], fields[3], fields[4]);
      case "BATCH_M" -> registerDBatch(fields[1], fields[2], fields[3], fields[4], fields[5], fields[6]);
      default -> throw new BadEntryException(fields[0]);
      }
    }

    public Warehouse getWarehouse() {
        return _warehouse;
    }

    public void registerBatch(String prod, String sup, String price, String amount) {
      BatchFactory fac = new BatchFactory(_warehouse);
      fac.addBacth(prod, sup, price, amount);
    }

    public void registerPartner(String key, String name, String address) {
      PartnerFactory fac = new PartnerFactory(_warehouse);
      fac.addPartner(key, name, address);
    }

    public void registerDBatch(String prod, String sup, String price, String amount, String rate, String recipe) {
      BatchDerivedFactory fac = new BatchDerivedFactory(_warehouse);
      fac.addBacth(prod, sup, price, amount, rate, recipe);
    }
}
