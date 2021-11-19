package ggc.visitState;

import ggc.Product;
import ggc.states.PartnerState;

public class EliteStateVisitor extends VisitorState {

    @Override
    public float visitNormalState(PartnerState p, int dateDiff, float price, Product prod) {
        return 0;
    }
    @Override
    public float visitSelectionState(PartnerState p, int dateDiff, float price, Product prod) {
        return 0;
    }
    @Override
    public float visitElitState(PartnerState p, int dateDiff, float price, Product prod) {
        int N; // variable for products
        if (prod.isSimple())    N = 5; // N_SIMPLE
        else                    N = 3; // N_DERIVED


        if (dateDiff >= 0){   // in this case, there are DISCOUNTS
            // case P1
            if (dateDiff >= N){
                price *= 0.9; // 10% discount
            }
            // case P2
            else if (0 <= dateDiff && dateDiff <= N){
                price *= 0.9; // 10% discount
            }
        }
        else {                // in this case, there are FINES 
            dateDiff = Math.abs(dateDiff); // get the positive diference
             // case P3
            if (0 <= dateDiff && dateDiff <= N){
                price *= 0.05; // 5% discount
            }

            //case P4
            else if (dateDiff > N){} // does nothing
        
        }
        return price;
    }
}
