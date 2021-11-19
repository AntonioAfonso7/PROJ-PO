package ggc.states;

import java.io.Serializable;

import ggc.Partner;
import ggc.Product;
import ggc.visitState.VisitorState;

public abstract class PartnerState implements Serializable {
    //Partner associated with these state
    protected Partner _partner;
    /**
     * 
     * @return the name of the current state
     */
    public abstract String getName();

    public double getPoints(){
        return _partner.getPoints();
    }
    /**
     * Simple constructor for the status of the partner
     * @param partner associated with this state
     */
    public PartnerState(Partner partner) {
         _partner = partner; 
    }

    /**
     * 
     * @return the current status
     */
    public String status() { 
        return getName(); 
    }

    public abstract void refresh(double points, int dateDiff);
    public abstract float accept(VisitorState v, int date, float price, Product prod);


}
