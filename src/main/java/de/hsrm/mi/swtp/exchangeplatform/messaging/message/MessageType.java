package de.hsrm.mi.swtp.exchangeplatform.messaging.message;

public enum MessageType {
	//TODO: check if messagetypes are needed, only one being used right now
	LOGIN,
	TRADE_OFFER_SUCCESS,
	TRADE_OFFER_DENIED,
	FORCED_TRADE_OFFER,
	PO_VIOLATION_CP,
	PO_VIOLATION_SEMESTER,
	PO_VIOLATION_PROGRESSIVE_REGULATION,
	PO_VIOLATION_DUAL,
}
