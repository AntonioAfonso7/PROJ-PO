package ggc.visitState;

import ggc.Product;
import ggc.states.PartnerState;

public abstract class VisitorState {
    public abstract float visitNormalState(PartnerState p, int date, float priceDiff, Product prod);
    public abstract float visitSelectionState(PartnerState p, int date, float priceDiff, Product prod);
    public abstract float visitElitState(PartnerState p, int date, float priceDiff, Product prod);
}
