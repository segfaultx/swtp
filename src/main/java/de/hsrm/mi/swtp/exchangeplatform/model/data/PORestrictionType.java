package de.hsrm.mi.swtp.exchangeplatform.model.data;

public interface PORestrictionType {
	
	boolean canAllocate(User user, Module module);
}
