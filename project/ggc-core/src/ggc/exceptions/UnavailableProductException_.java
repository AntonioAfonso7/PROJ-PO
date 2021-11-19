package ggc.exceptions;

public class UnavailableProductException_ extends Exception {
  public String _key;
  public int _requested;
  public int _available;


  public UnavailableProductException_(String name, int requested, int available) {
    _key = name;
    _requested = requested;
    _available = available;
  }
  
  public String getKey() {
    return _key;
  }

  public int getRequested(){
      return _requested;
  }

  public int getAvailable(){
      return _available;
  }
}
