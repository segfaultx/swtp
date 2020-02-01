package de.hsrm.mi.swtp.exchangeplatform.model.admin.settings;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;


/**
 * @author amatus
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@Entity
@RequiredArgsConstructor
@Slf4j
public class AdminSettings {

	@Id
	long id;
	
	boolean tradesActive = true;
	
	LocalDateTime dateStartTrades;
	
	LocalDateTime dateEndTrades;

	
	@ElementCollection
	@LazyCollection(LazyCollectionOption.FALSE)
	List<String> activeFilters;
	
	public void setActiveFilters(List<String> activeFilters) {
		this.activeFilters = activeFilters;
	}
	
	public void setTradesActive(boolean tradesActive) {
		this.tradesActive = tradesActive;
	}
	
	public boolean isTradesActive() {
		return tradesActive &&
				(LocalDateTime.now().isBefore(dateEndTrades) &&
						LocalDateTime.now().isAfter(dateStartTrades));
	}

	@Override
	public boolean equals(Object other) {
		if(!(other instanceof AdminSettings)) return false;
		return this.tradesActive == ((AdminSettings) other).tradesActive
				&& Arrays.equals(this.activeFilters.toArray(), ((AdminSettings) other).activeFilters.toArray());
	}
}
