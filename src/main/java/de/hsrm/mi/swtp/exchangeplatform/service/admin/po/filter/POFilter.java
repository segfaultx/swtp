package de.hsrm.mi.swtp.exchangeplatform.service.admin.po.filter;

import de.hsrm.mi.swtp.exchangeplatform.model.data.PO;

public interface POFilter {
	
	/**
	 * The method which is called to initiate the filtering process.
	 * @param po is the {@link PO} has changed.
	 * @return
	 */
	PORestrictionFilterResult filter(final PO po, final PORestrictionFilterResult result);
	
}
