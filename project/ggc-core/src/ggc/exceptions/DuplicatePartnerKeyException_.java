package ggc.exceptions;

public class DuplicatePartnerKeyException_ extends PartnerException {

    /**
  	 * @param id
  	 * @param name
     * @param address
  	 */
    public DuplicatePartnerKeyException_(String id, String name, String address) {
        super(id, name, address);
    }
}