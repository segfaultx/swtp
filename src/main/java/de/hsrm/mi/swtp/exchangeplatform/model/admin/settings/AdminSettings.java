package de.hsrm.mi.swtp.exchangeplatform.model.admin.settings;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.hsrm.mi.swtp.exchangeplatform.exceptions.notfound.NotFoundException;
import de.hsrm.mi.swtp.exchangeplatform.service.filter.*;
import de.hsrm.mi.swtp.exchangeplatform.service.filter.TradeFilter.CollisionFilter;
import de.hsrm.mi.swtp.exchangeplatform.service.filter.TradeFilter.NoOfferFilter;
import de.hsrm.mi.swtp.exchangeplatform.service.filter.TradeFilter.OfferFilter;
import lombok.*;
import de.hsrm.mi.swtp.exchangeplatform.service.filter.utils.FilterUtils;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.Arrays;
import java.util.List;


//TODO: javadoc
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
		return tradesActive;
	}

	@Override
	public boolean equals(Object other) {
		if(!(other instanceof AdminSettings)) return false;
		return this.tradesActive == ((AdminSettings) other).tradesActive
				&& Arrays.equals(this.activeFilters.toArray(), ((AdminSettings) other).activeFilters.toArray());
	}
}
