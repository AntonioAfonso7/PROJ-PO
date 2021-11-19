package ggc.states;

import ggc.Partner;
import ggc.Product;
import ggc.visitState.VisitorState;

public class SelectionState extends PartnerState {
    //Name of the state
    private String _name = "SELECTION";

    public String getName() { 
        return _name; 
    }

    /**
     * Simple constructor that creates a status associated with a partner
     * @param partner associated with this state
     */
    public SelectionState(Partner partner) { 
        super(partner);
    }

    public void refresh(double points, int dateDiff){
        if (dateDiff < -2){     //if there is a delay bigger than 2 days
            points *= 0.1;      //looses 90% of the points accumulated
            _partner.setState(new NormalState(_partner)); // downgrades to NormalState
        }
        else{
            double updatedPoints = points + getPoints();
            if (updatedPoints >= 25000)
                _partner.setState(new EliteState(_partner));  //promoted to EliteState
        }
        _partner.addPoints(points);
    }

    public float accept (VisitorState v, int date, float price, Product prod){
        return v.visitNormalState(this, date, price, prod); 
    }
    
}
