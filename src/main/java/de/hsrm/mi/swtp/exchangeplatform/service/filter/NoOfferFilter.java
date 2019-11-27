package de.hsrm.mi.swtp.exchangeplatform.service.filter;

import de.hsrm.mi.swtp.exchangeplatform.model.data.Timeslot;
import de.hsrm.mi.swtp.exchangeplatform.model.data.TradeOffer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NoOfferFilter implements Filter{

    @Override
    public List<TradeOffer> filter(List<TradeOffer> offers){
        List<TradeOffer> noOfferList = new ArrayList<>();
        if(offers.isEmpty()){
            return noOfferList;
        }
        return offers;
    }
}