package ggc.exceptions;

public class InvalidDateException_ extends Exception {
    private int _days;

    public InvalidDateException_(int days){
        _days = days;
    }

    public int getDays(){
        return _days;
    }
}
