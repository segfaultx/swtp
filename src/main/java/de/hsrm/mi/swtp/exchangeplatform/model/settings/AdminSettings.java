package de.hsrm.mi.swtp.exchangeplatform.model.settings;

import de.hsrm.mi.swtp.exchangeplatform.service.filter.utils.FilterUtils;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.Arrays;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@Entity
@RequiredArgsConstructor
@Slf4j
public class AdminSettings {
	
	@Transient
	FilterUtils filterUtils = FilterUtils.getInstance();
	
	@Id
	long id;
	
	boolean tradesActive = true;
	
	@ElementCollection
	List<String> activeFilters;
	
	public void setActiveFilters(List<String> activeFilters) {
		this.activeFilters = activeFilters;
	}
	
	public void setTradesActive(boolean tradesActive) {
		this.tradesActive = tradesActive;
	}
	
	public boolean isTradesActive() {
		return tradesActive;
	}

	@Override
	public boolean equals(Object other) {
		if(!(other instanceof AdminSettings)) return false;
		return this.tradesActive == ((AdminSettings) other).tradesActive
				&& Arrays.equals(this.activeFilters.toArray(), ((AdminSettings) other).activeFilters.toArray());
	}
}
