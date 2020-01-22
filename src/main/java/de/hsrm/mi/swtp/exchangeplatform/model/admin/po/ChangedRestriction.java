package de.hsrm.mi.swtp.exchangeplatform.model.admin.po;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.hsrm.mi.swtp.exchangeplatform.model.data.PO;
import de.hsrm.mi.swtp.exchangeplatform.model.data.enums.RestrictionType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Slf4j
public class ChangedRestriction {

	@JsonProperty("updated_po")
	PO updatedPO;
	
	@JsonProperty("changed_restrictions")
	List<RestrictionType> changedRestrictions;

}
