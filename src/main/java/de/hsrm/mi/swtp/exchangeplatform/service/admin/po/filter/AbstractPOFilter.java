package de.hsrm.mi.swtp.exchangeplatform.service.admin.po.filter;

import de.hsrm.mi.swtp.exchangeplatform.model.data.enums.RestrictionType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * A marker interface which has a {@link RestrictionType}. This type will allow the mapping to the corresponding {@link de.hsrm.mi.swtp.exchangeplatform.model.data.PORestriction} when applying a filter.
 * Extends {@link POFilter}.
 */
@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public abstract class AbstractPOFilter implements POFilter {
	
	@Getter
	public final RestrictionType restrictionType;
	
}
