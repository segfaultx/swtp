package de.hsrm.mi.swtp.exchangeplatform.model.admin.po.enums;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum ProgressiveRegulationSpan {
	NONE(0L),
	DEFAULT(3L),
	ONE(1L),
	TWO(2L),
	THREE(4L),
	FOUR(5L);
	
	Long semesterSpan;
	
	ProgressiveRegulationSpan(Long span) {
		this.semesterSpan = span;
	}
}
