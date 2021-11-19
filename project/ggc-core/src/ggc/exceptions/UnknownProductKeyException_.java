package ggc.exceptions;

public class UnknownProductKeyException_ extends Exception {
  public String _key;

  public UnknownProductKeyException_(String name) {
    _key = name;
  }
  
  public String getKey() {
    return _key;
  }
}
