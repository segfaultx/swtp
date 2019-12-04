package de.hsrm.mi.swtp.exchangeplatform.service.filter;

import de.hsrm.mi.swtp.exchangeplatform.model.data.TradeOffer;

import java.util.List;
import java.util.Map;

public interface Filter{

    List<TradeOffer> filter(List<TradeOffer> offers);
}