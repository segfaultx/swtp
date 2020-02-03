package de.hsrm.mi.swtp.exchangeplatform.model.rest;

import de.hsrm.mi.swtp.exchangeplatform.model.data.Timeslot;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TradeWrapper {
	
	Timeslot timeslot;
	
	boolean instantTrade = false;
	
	boolean ownOffer = false;
	
	boolean trade = false;
	
	boolean remaining = false;
	
	boolean collision = false;
	
	boolean collisionLecture = false;
}
