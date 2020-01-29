package de.hsrm.mi.swtp.exchangeplatform.service.filter;

import de.hsrm.mi.swtp.exchangeplatform.model.data.TradeOffer;
import de.hsrm.mi.swtp.exchangeplatform.model.data.User;
import de.hsrm.mi.swtp.exchangeplatform.service.filter.utils.FilterUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FilterUtilsTest {
	
	private final Logger log = LoggerFactory.getLogger(FilterUtilsTest.class);
	
	@Autowired
	private FilterUtils filterUtils;

	private List<String> filterNames;
	private Map<String, Filter> filterMap;
	
	@BeforeEach
	public void getAllAvailableFiltersToTestWith() {
		filterNames = filterUtils.getActiveFilterList();
		filterMap = filterUtils.getMap();
	}

	@Test
	public void testFilterExistsWithValidFilter() {
		String existingFilterName = filterNames.get(0);
		boolean filterExists = filterUtils.filterExists(existingFilterName);
		assertTrue(filterExists);
	}
	
	@Test
	public void testFilterExistsWithNonValidFilter() {
		String garbageFilterName = UUID.randomUUID().toString();
		boolean filterExists = filterUtils.filterExists(garbageFilterName);
		assertFalse(filterExists);
	}
	
	@Test
	public void testAddFilterWithValidFilter() {
		String expected = "MockFilter";
		Filter filter = mock(Filter.class);
		filterUtils.addFilter(expected, filter);
		List<String> listOfFiltersWithMockFilter = filterUtils.getAllAvailableFilters();
		for (String filterName: listOfFiltersWithMockFilter) {
			if(filterName.equals(expected)) assertTrue("Filter successfully added", true);
			return;
		}
		fail("Filter not added");
	}
	
	@Test
	public void testGetFilterByNameWithValidInput() {
		String expectedKey = filterNames.get(0);
		Filter expectedFilter = filterMap.get(expectedKey);
		
		Filter actual = filterUtils.getFilterByName(expectedKey);
		assertEquals(expectedFilter, actual);
	}
	
	@Test
	public void testGetAllAvailableFiltersWithNewPythonFilter() {
		String name = "CustomMockPythonFilter";
		String code = "print('Hello Test')";
		filterUtils.addPythonFilter(name, code);
		
		List<String> actual = filterUtils.getAllAvailableFilters();
		for(String elem : actual) {
			if(elem.equals(name)) assertTrue("Custom Python Filter Successfully added", true);
			return;
		}
		fail("Custom Python Filter not in List of available Filters");
	}
	
	@Test
	public void testGetFilterByNameWithNewPythonFilter() {
		String name = "CustomMockTestPythonFilter";
		String code = "print('Hello Test')";
		filterUtils.addPythonFilter(name, code);
		
		Filter actual = filterUtils.getFilterByName(name);
		assertNotNull(actual);
	}
	
	@Test
	public void removeFilterFromActiveFilters() {
		String removed = filterNames.get(1);
		filterUtils.removeFilter(removed);
		
		List<String> activeFilters = filterUtils.getActiveFilterList();
		for(String elem: activeFilters) {
			if(elem.equals(removed)) fail("Filter was not removed from active Filters");
		}
		assertTrue("Successfully removed filter from active Filters", true);
	}
	
	@Test
	public void testSetActiveFiltersWithEmptyListByComparingUnfilteredTradeOffers() {
		List<String> expected = new ArrayList<>();
		User seeker = null;
		
		filterUtils.setActiveFilters(expected);
		
		assertEquals(filterUtils.getActiveFilterList().size(), expected.size());
		
		TradeOffer to1 = mock(TradeOffer.class);
		TradeOffer to2 = mock(TradeOffer.class);
		TradeOffer to3 = mock(TradeOffer.class);
		List<TradeOffer> tradeOffers = List.of(to1, to2, to3);
		Principal principal = null;
		assertEquals(tradeOffers, filterUtils.getFilteredTradeOffers(tradeOffers, seeker));
		
		//clean up
		filterUtils.setActiveFilters(filterUtils.getAllAvailableFilters());
		
	}
	
}
