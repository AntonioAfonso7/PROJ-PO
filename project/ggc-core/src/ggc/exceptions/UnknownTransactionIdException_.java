package ggc.exceptions;

public class UnknownTransactionIdException_ extends Exception{

    private int _id;  

  	public UnknownTransactionIdException_(int id) {
  		_id = id;
  	}

    public int getId() {
      return _id;
    }

}