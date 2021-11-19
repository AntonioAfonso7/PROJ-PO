package ggc.states;

import ggc.Partner;
import ggc.Product;
import ggc.visitState.VisitorState;

public class NormalState extends PartnerState {
    //The name of the current state
    private String _name = "NORMAL";

    public String getName() { return _name; }
    
    /**
     * Simple constructor that creates a status associated with a partner
     * @param partner associated with this state
     */

    public NormalState(Partner partner) {
        super(partner);
    }

    public void refresh(double points, int dateDiff){
        if (dateDiff < 0){
            points = 0; //if simple partner delays in a payment, he looses all his points
        }
        else{
            double updatedPoints = points + getPoints();
            if (updatedPoints >= 2000)
                _partner.setState(new SelectionState(_partner));
        }
        _partner.addPoints(points);
    }

    public float accept (VisitorState v, int date, float price, Product prod){
        return v.visitNormalState(this, date, price, prod);  
    }

}
