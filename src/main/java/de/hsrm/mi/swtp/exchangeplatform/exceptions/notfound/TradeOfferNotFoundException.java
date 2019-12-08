package de.hsrm.mi.swtp.exchangeplatform.exceptions.notfound;

public class TradeOfferNotFoundException extends RuntimeException {
	
	public TradeOfferNotFoundException() {
		super("Could not find the requested object.");
	}
	
	public TradeOfferNotFoundException(String message) {
		super(message);
	}
	
	public TradeOfferNotFoundException(Long id) {
		super(String.format("Could not find the requested object by id '%s'.", id));
	}
	
}
