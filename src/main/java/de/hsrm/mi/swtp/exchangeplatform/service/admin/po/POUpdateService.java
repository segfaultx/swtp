package de.hsrm.mi.swtp.exchangeplatform.service.admin.po;

import de.hsrm.mi.swtp.exchangeplatform.exceptions.NotUpdatedException;
import de.hsrm.mi.swtp.exchangeplatform.exceptions.notfound.ExchangeplatformStillActiveException;
import de.hsrm.mi.swtp.exchangeplatform.exceptions.notfound.NotFoundException;
import de.hsrm.mi.swtp.exchangeplatform.model.admin.po.ChangedRestriction;
import de.hsrm.mi.swtp.exchangeplatform.model.data.PO;
import de.hsrm.mi.swtp.exchangeplatform.model.data.PORestriction;
import de.hsrm.mi.swtp.exchangeplatform.model.data.enums.RestrictionType;
import de.hsrm.mi.swtp.exchangeplatform.repository.PORepository;
import de.hsrm.mi.swtp.exchangeplatform.service.rest.POService;
import de.hsrm.mi.swtp.exchangeplatform.service.settings.AdminSettingsService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * A service which is used for applying and updating an original PO with updated values and restrictions.
 */
@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class POUpdateService {
	
	PORepository repository;
	POService poService;
	AdminSettingsService adminSettingsService;
	Map<Long, ChangedRestriction> updatedRestrictions;
	
	/**
	 * Applies all changes/properties from the updated PO to the original - this does NOT include the {@link PO#restriction}!
	 *
	 * @param original is the original PO which will be updated.
	 * @param update   is the new PO with the updated values
	 *
	 * @return a PO with updated values - but it keeps the original restrictions.
	 */
	private PO applyChanges(final PO original, final PO update) {
		original.setTitle(update.getTitle());
		original.setDateStart(update.getDateStart());
		original.setDateEnd(update.getDateEnd());
		original.setMajor(update.getMajor());
		original.setValidSince(update.getValidSince());
		original.setSemesterCount(update.getSemesterCount());
		return original;
	}
	
	/**
	 * Compares restrictions and tells whether they are the same
	 *
	 * @param original the {@link PO} instance which is used as a reference.
	 * @param update   the {@link PORestriction} which has to be applied to the original restrictions if those differ.
	 *
	 * @return a boolean which tells whether the restrictions of the given {@link PO POs} are identical or not.
	 */
	private boolean areRestrictionsDifferent(final PO original, final PO update) throws OriginalRestrictionIsNullException {
		final PORestriction originalRestrictions = original.getRestriction();
		final PORestriction updatedRestrictions = update.getRestriction();
		
		if(originalRestrictions == null && updatedRestrictions != null) {
			throw new OriginalRestrictionIsNullException();
		}
		
		boolean cpAreSame = originalRestrictions.getByCP().equals(updatedRestrictions.getByCP());
		boolean semesterAreSame = originalRestrictions.getBySemester().equals(updatedRestrictions.getBySemester());
		boolean progressiveRegAreSame = originalRestrictions.getByProgressiveRegulation().equals(updatedRestrictions.getByProgressiveRegulation());
		boolean dualAreSame = originalRestrictions.getDualPO().equals(updatedRestrictions.getDualPO());
		
		return !(cpAreSame && semesterAreSame && progressiveRegAreSame && dualAreSame);
	}
	
	/**
	 * Finds differences in {@link PO#restriction} between the original and updated POs and if there is a difference the updated value will be applied to the original restriction. But this will only happen if the updated restriction is active. Otherwise it will be skipped.
	 *
	 * @param original the {@link PO} instance which is used as a reference.
	 * @param update   the {@link PORestriction} which will be applied to the original restrictions if those differ.
	 *
	 * @return a PO with updated PO restrictions if there are any differences.
	 */
	private List<RestrictionType> affectedRestrictions(final PO original, final PO update) {
		final ArrayList<RestrictionType> affectedRestrictions = new ArrayList<>();
		final PORestriction originalRestrictions = original.getRestriction();
		final PORestriction updatedRestrictions = update.getRestriction();
		
		if(originalRestrictions == null) {
			return Arrays.asList(RestrictionType.values());
		}
		
		if(updatedRestrictions.getByCP().isActive() && !originalRestrictions.getByCP().getMaxCP().equals(updatedRestrictions.getByCP())) {
			affectedRestrictions.add(RestrictionType.CREDIT_POINTS);
		}
		if(updatedRestrictions.getBySemester().isActive() && !originalRestrictions.getBySemester().equals(updatedRestrictions.getBySemester())) {
			affectedRestrictions.add(RestrictionType.MINIMUM_SEMESTER);
		}
		if(updatedRestrictions.getByProgressiveRegulation().isActive() && !originalRestrictions.getByProgressiveRegulation()
																							   .equals(updatedRestrictions.getByProgressiveRegulation())) {
			affectedRestrictions.add(RestrictionType.PROGRESSIVE_REGULATION);
		}
		if(updatedRestrictions.getDualPO().isActive() && !originalRestrictions.getDualPO().equals(updatedRestrictions.getDualPO())) {
			affectedRestrictions.add(RestrictionType.DUAL);
		}
		
		log.info("--------------------------- AFFECTED: " + affectedRestrictions);
		
		return affectedRestrictions;
	}
	
	/**
	 * Will update the original {@link PO} if a) the exchangeplatform is inactive and b) there are significant differences in the updated argument.
	 *
	 * @param update a {@link PO} with updated values.
	 */
	public boolean update(final PO update) throws IllegalArgumentException, NotFoundException, NotUpdatedException {
		if(adminSettingsService.isTradesActive()) throw new ExchangeplatformStillActiveException();
		if(!repository.existsById(update.getId())) throw new NotFoundException();
		final PO original = poService.getById(update.getId());
		
		final PO updatedPO = applyChanges(original, update);
		
		boolean restrictionsAreDifferent;
		try {
			restrictionsAreDifferent = areRestrictionsDifferent(original, update);
		} catch(OriginalRestrictionIsNullException e) {
			restrictionsAreDifferent = true;
		}
		
		if(restrictionsAreDifferent) {
			log.info("PORestriction changes detected.");
			ChangedRestriction changedRestriction = ChangedRestriction.builder()
																	  .changedRestrictions(affectedRestrictions(original, update))
																	  .updatedPO(updatedPO)
																	  .build();
			original.setRestriction(update.getRestriction());
			changedRestriction.setUpdatedPO(original);
			updatedRestrictions.put(original.getId(), changedRestriction);
			log.info("PORestriction changes applied.");
		}
		
		var newPO = repository.save(updatedPO);
		log.info("PO changes applied.");
		
		return true;
	}
	
	public List<ChangedRestriction> getAllChangedPOs() {
		return new ArrayList<>(this.updatedRestrictions.values());
	}
	
	/** Clears the {@link #updatedRestrictions}. */
	public void flush() {
		this.updatedRestrictions.clear();
	}
	
	private class OriginalRestrictionIsNullException extends Throwable {}
}
