package ggc.exceptions;

public abstract class PartnerException extends Exception {

  /** The partner id */
  private String _id;

  /** The partner name */
  private String _name;

  /** The partner name */
  private String _address;

  /**
   * @param id
   * @param name
   * @param address
   */
  public PartnerException(String id, String name, String address) {
    _id = id;
    _name = name;
    _address = address;
  }

  /**
   * @return the partner id
   */
  public String getId() {
    return _id;
  }

  /**
   * @return the partner id
   */
  public String getName() {
    return _name;
  }

  /**
   * @return the partner id
   */
  public String getAddress() {
    return _address;
  }
}
