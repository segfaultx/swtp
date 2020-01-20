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
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class POUpdateService {

	PORepository repository;
	POService poService;
	AdminSettingsService adminSettingsService;
	Map<Long, ChangedRestriction> updatedRestrictions;

	private PO applyChanges(final PO original, final PO update) throws NotFoundException {
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
	 * @param update   the {@link PORestriction} which has to be applied to the original restrictions if they differ.
	 * @return a boolean which tells whether the restrictions of the given {@link PO POs} are identical or not.
	 */
	private boolean areRestrictionsDifferent(final PO original, final PO update) {
		final PORestriction originalRestrictions = original.getRestriction();
		final PORestriction updatedRestrictions = update.getRestriction();

		return !(originalRestrictions.getByCP().equals(updatedRestrictions.getByCP()) && originalRestrictions.getByProgressiveRegulation()
				.equals(updatedRestrictions.getByProgressiveRegulation()) && originalRestrictions
				.getBySemester()
				.equals(updatedRestrictions.getBySemester()) && originalRestrictions.getDualPO().equals(updatedRestrictions.getDualPO()));
	}

	private List<RestrictionType> affectedRestrictions(final PO original, final PO update) {
		final ArrayList<RestrictionType> affectedRestrictions = new ArrayList<>();
		final PORestriction originalRestrictions = original.getRestriction();
		final PORestriction updatedRestrictions = update.getRestriction();

		if (!(originalRestrictions.getByCP().equals(updatedRestrictions.getByCP())))
			affectedRestrictions.add(RestrictionType.CREDIT_POINTS);
		if (!(originalRestrictions.getBySemester().equals(updatedRestrictions.getBySemester())))
			affectedRestrictions.add(RestrictionType.MINIMUM_SEMESTER);
		if (!(originalRestrictions.getByProgressiveRegulation().equals(updatedRestrictions.getByProgressiveRegulation())))
			affectedRestrictions.add(RestrictionType.PROGRESSIVE_REGULATION);

		return affectedRestrictions;
	}

	/**
	 * Will update the original {@link PO} if a) the exchangeplatform is inactive and b) there are significant differences in the updated argument.
	 *
	 * @param update a {@link PO} with updated values.
	 */
	public boolean update(final PO update) throws IllegalArgumentException, NotFoundException, NotUpdatedException {
		if (adminSettingsService.isTradesActive()) throw new ExchangeplatformStillActiveException();
		if (!repository.existsById(update.getId())) throw new NotFoundException();
		final PO original = poService.getById(update.getId());

		final PO updatedPO = applyChanges(original, update);

		if (areRestrictionsDifferent(original, update)) {
			log.info("PORestriction changes detected.");
			ChangedRestriction changedRestriction = ChangedRestriction.builder()
																	  .changedRestrictions(affectedRestrictions(original, update))
																	  .updatedPO(update)
																	  .build();
			original.setRestriction(update.getRestriction());
			updatedRestrictions.put(original.getId(), changedRestriction);
			log.info("PORestriction changes applied.");
		}

		repository.save(updatedPO);
		log.info("PO changes applied.");

		return true;
	}

	public List<ChangedRestriction> getAllChangedPOs() {
		return new ArrayList<>(this.updatedRestrictions.values());
	}
	
	public void flush() {
		this.updatedRestrictions.clear();
	}

}
