package de.hsrm.mi.swtp.exchangeplatform.controller;

import com.google.common.base.Joiner;
import de.hsrm.mi.swtp.exchangeplatform.model.data.User;
import de.hsrm.mi.swtp.exchangeplatform.repository.UserRepository;
import de.hsrm.mi.swtp.exchangeplatform.service.UserSpecs.SearchOperation;
import de.hsrm.mi.swtp.exchangeplatform.service.UserSpecs.UserSpecificationsBuilder;
import de.hsrm.mi.swtp.exchangeplatform.service.rest.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RestController
@RequestMapping("/api/v1/searchStudent")
public class SearchUserRestController {
	
	UserRepository userRepository;
	UserService userService;
	
	@GetMapping("")
	public ResponseEntity<List<User>> getSearchResult(@RequestParam("name") Optional<String> name, @RequestParam("id") Optional<Long> id)
	{
		
		List<List<User>> lists = new ArrayList<>();
		
		if(name.isPresent()){
			List<User> firstNames = userService.getAllByFirstName(name.get());
			if(!firstNames.isEmpty()){lists.add(firstNames);}
			List<User> lastNames = userService.getAllByLastName(name.get());
			if(!lastNames.isEmpty()){lists.add(lastNames);}
		}
		
		if(id.isPresent()){
			List<User> studNums = userService.getAllByStudentNumber(id.get());
			if(!studNums.isEmpty()){lists.add(studNums);}
			List<User> staffNums = userService.getAllByStaffNumber(id.get());
			if(!staffNums.isEmpty()){lists.add(staffNums);}
		}
		
		List<User> result = userService.unifyLists(lists);
		
		
		
		
		return ResponseEntity.ok(result);
				
	}
	
}
