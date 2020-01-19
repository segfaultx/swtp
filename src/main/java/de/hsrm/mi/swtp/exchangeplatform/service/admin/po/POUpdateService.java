package de.hsrm.mi.swtp.exchangeplatform.service.admin.po;

import de.hsrm.mi.swtp.exchangeplatform.exceptions.notfound.NotFoundException;
import de.hsrm.mi.swtp.exchangeplatform.model.data.PO;
import de.hsrm.mi.swtp.exchangeplatform.model.data.PORestriction;
import de.hsrm.mi.swtp.exchangeplatform.service.rest.POService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class POUpdateService {
	
	POService poService;
	List<PO> updatedPOs;
	
	public boolean hasRestrictionChanges(final PO updatedPO) throws NotFoundException {
		final PO original = poService.getById(updatedPO.getId());
		boolean differ = areDifferent(original, updatedPO);
		log.info(" == ARE SAME: " + differ);
		
		return differ;
	}
	
	/**
	 * Compares restrictions and tells whether they are the same
	 *
	 * @param original  the {@link PO} instance which is used as a reference.
	 * @param updatedPO the {@link PORestriction} which has to be applied to the original restrictions if they differ.
	 *
	 * @return a boolean which tells whether the restrictions of the given {@link PO POs} are identical or not.
	 */
	private boolean areDifferent(final PO original, final PO updatedPO) {
		final PORestriction originalRestrictions = original.getPoRestriction();
		final PORestriction updatedRestrictions = updatedPO.getPoRestriction();
		
		return !(originalRestrictions.getByCP().equals(updatedRestrictions.getByCP()) &&
				originalRestrictions.getByProgressiveRegulation().equals(updatedRestrictions.getByProgressiveRegulation()) &&
				originalRestrictions.getBySemester().equals(updatedRestrictions.getBySemester()) &&
				originalRestrictions.getDualPO().equals(updatedRestrictions.getDualPO()));
	}
	
}
