package de.hsrm.mi.swtp.exchangeplatform.service.admin.po.filter;

import de.hsrm.mi.swtp.exchangeplatform.model.data.PO;
import de.hsrm.mi.swtp.exchangeplatform.model.data.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class POFilterChain {
	
	List<AbstractPOFilter> filterList;
	
	@Builder
	public POFilterChain() {
		this.filterList = new ArrayList<>();
	}
	
	public void appendFilter(final AbstractPOFilter poFilter) {
		this.filterList.add(poFilter);
	}
	
	/**
	 * Go through the List of POFilters and apply the restrictions of the given PO onto the given student.
	 *
	 * @param po      contains the {@link de.hsrm.mi.swtp.exchangeplatform.model.data.PORestriction} which are to be applied and processed.
	 * @param student is the {@link User} which is to be processed.
	 *
	 * @return the result containing a map with all violations and messages of the given student.
	 */
	public PORestrictionFilterResult processAllForStudent(final PO po, final User student) {
		PORestrictionFilterResult result = PORestrictionFilterResult.builder().student(student).build();
		this.filterList.forEach(filter -> result.extend(filter.getRestrictionType(), filter.filter(po, result)));
		return result;
	}
	
	public void flush() {
		this.filterList.clear();
	}
	
}
