package de.hsrm.mi.swtp.exchangeplatform.model.rest;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;


/**
 * @author amatus
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TradeRequest {
	long offeredByStudentMatriculationNumber;
	long wantedTimeslotId;
	long offeredTimeslotId;
	boolean isInstantTrade;
}
