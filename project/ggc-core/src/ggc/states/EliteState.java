package ggc.states;

import ggc.Partner;
import ggc.Product;
import ggc.visitState.VisitorState;

public class EliteState extends PartnerState {
    //Name of the state
    private String _name = "ELITE";

    public String getName() { return _name; }

    /**
     * Simple constructor that creates a status associated with a partner
     * @param partner associated with this partner
     */
    public EliteState(Partner partner) {
        super(partner);
    }

    public void refresh(double points, int dateDiff) {
        if (dateDiff < - 15){       // 15 day delay
            points *= 0.25;         // looses 75% of the points earned
            _partner.setState(new SelectionState(_partner));  //gets downgraded to SelectionState
        } 
        _partner.addPoints(points);

    }

    public float accept (VisitorState v, int date, float price, Product prod){
        return v.visitElitState(this, date, price, prod); 
    }


}
