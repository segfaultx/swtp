package de.hsrm.mi.swtp.exchangeplatform.model.settings;

import de.hsrm.mi.swtp.exchangeplatform.service.filter.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Entity
public class AdminSettings {
	
	@Id
	@GeneratedValue
	long id;
	
	boolean tradesActive = true;
	
	@Getter
	enum Filters {
		COLLISION(new CollisionFilter()), CAPACITY(new CapacityFilter()), OFFER(new OfferFilter()), NOOFFER(new NoOfferFilter());
		
		Filters(Filter filter) {
			this.filter = filter;
		}
		
		private Filter filter;
	}
	
	@ElementCollection
	@Enumerated(EnumType.STRING)
	List<Filters> activeFilters = new ArrayList<>();
}
