package de.hsrm.mi.swtp.exchangeplatform.model.settings;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.hsrm.mi.swtp.exchangeplatform.exceptions.notfound.NotFoundException;
import de.hsrm.mi.swtp.exchangeplatform.service.filter.*;
import de.hsrm.mi.swtp.exchangeplatform.service.filter.TradeFilter.CollisionFilter;
import de.hsrm.mi.swtp.exchangeplatform.service.filter.TradeFilter.CapacityFilter;
import de.hsrm.mi.swtp.exchangeplatform.service.filter.TradeFilter.NoOfferFilter;
import de.hsrm.mi.swtp.exchangeplatform.service.filter.TradeFilter.OfferFilter;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
	
	@Getter
	enum Filters {
		COLLISION(new CollisionFilter(), "COLLISION"), CAPACITY(new CapacityFilter(), "CAPACITY"), OFFER(new OfferFilter(), "OFFER"), NOOFFER(
				new NoOfferFilter(), "NOOFFER");
		
		Filters(Filter filter, String stringVal) {
			this.filter = filter;
			this.stringVal = stringVal;
		}
		
		public Filters valueFromString(String value) throws NotFoundException {
			for(Filters filter : Filters.values()) {
				if(filter.stringVal.equals(value)) return filter;
			}
			throw new NotFoundException("Unknown Filter Type");
		}
		
		private Filter filter;
		private String stringVal;
		
		@Override
		public String toString() {
			return "Filters{" + "filter=" + filter + ", stringVal='" + stringVal + '\'' + '}';
		}
		
		public Filter getFilter() {
			return filter;
		}
	}
	
	@ElementCollection
	@Enumerated(EnumType.STRING)
	List<Filters> activeFilters = new ArrayList<>();
	
	/**
	 * Method to update admin settings
	 *
	 * @param tradesActive  boolean flag to determine wether the trades rest endpoints are available or not
	 * @param activeFilters list of currently active filters
	 *
	 * @throws NotFoundException if any of the given filters are unknown to the system
	 */
	public void updateAdminSettings(boolean tradesActive, List<String> activeFilters) throws NotFoundException {
		log.info(String.format("Updating admin settings (tradesActive old value: %s, new: %s)", this.tradesActive, tradesActive));
		this.tradesActive = tradesActive;
		log.info(String.format("Updating admin settings filters from: %s to: %s", this.activeFilters.toString(), activeFilters.toString()));
		this.activeFilters.clear();
		activeFilters.forEach(filterVal -> this.activeFilters.add(Filters.valueOf(filterVal)));
	}
	
	@JsonIgnore
	public List<Filter> getCurrentActiveFilters() {
		List<Filter> out = new ArrayList<>();
		activeFilters.forEach(item -> out.add(item.getFilter()));
		return out;
	}
	
	@Override
	public boolean equals(Object other) {
		if(!(other instanceof AdminSettings)) return false;
		return this.tradesActive == ((AdminSettings) other).tradesActive
				&& Arrays.equals(this.activeFilters.toArray(), ((AdminSettings) other).activeFilters.toArray());
	}
}
