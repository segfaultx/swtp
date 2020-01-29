package de.hsrm.mi.swtp.exchangeplatform.controller;

import de.hsrm.mi.swtp.exchangeplatform.model.data.User;
import de.hsrm.mi.swtp.exchangeplatform.service.rest.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@SecurityRequirement(name = "Authorization")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RestController
@RequestMapping("/api/v1/searchStudent")
public class SearchUserRestController {
	
	UserService userService;
	String BASEURL = "/api/v1/searchStudent";
	
	/**
	 * GET request handler
	 * Will handle any request GET request to {@code '/api/v1/searchStudent/<name,id>'}.
	 * Searching for users that meet the search criteria by matching either in name or id (or both)
	 * @param name name given, could be either first name or last name or fullname
	 * @param id id given, searching for either student or staff number, not to be confused with internal id!
	 * @return {@link HttpStatus#OK} and the requested list of users. If none are found, return empty list
	 */
	@GetMapping("")
	@Operation(description = "search for User", operationId = "searchResult")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "successfully joined appointment"),
							@ApiResponse(responseCode = "403", description = "unauthorized join attempt"),
							@ApiResponse(responseCode = "400", description = "malformed request") })
	@PreAuthorize("hasRole('MEMBER') or hasRole('ADMIN')")
	public ResponseEntity<List<User>> getSearchResult(@RequestParam("name") Optional<String> name, @RequestParam("id") Optional<String> id) {
		log.info(String.format("GET // " + BASEURL + "/%s", id, name));
		List<List<User>> lists = new ArrayList<>();
		
		// check for name
		if(name.get() != "") {
			String reqName = name.get();
			// check if a full name was given...
			if(reqName.contains(" ")) {
				List<User> exactMatch = new ArrayList<>();
				// split into parts to search first and last name individually
				String[] parts = reqName.split(" ");
				// check repo, exclude possible middle names
				List<User> firstNames = userService.getAllByFirstName(parts[0]);
				List<User> lastNames = userService.getAllByLastName(parts[parts.length-1]);
				// check lists for duplicates to find exact matches
				for(User user : firstNames) {
					if(lastNames.contains(user)) {
						exactMatch.add(user);
					}
				}
				lists.add(exactMatch);
				
			//...if not expect name to be either first or last name and check both
			} else {
				List<User> firstNames = userService.getAllByFirstName(name.get());
				if(!firstNames.isEmpty()) {lists.add(firstNames);}
				List<User> lastNames = userService.getAllByLastName(name.get());
				if(!lastNames.isEmpty()) {lists.add(lastNames);}
			}
		}
		// check by id (student number and staff number are possible, NOT internal ID)
		if(id.get() != ""){
			List<User> studNums = userService.getAllByStudentNumber(id.get());
			if(!studNums.isEmpty()){lists.add(studNums);}
			
		}
		// get rid of duplicates
		List<User> result = userService.unifyLists(lists);
		// return result
		return ResponseEntity.ok(userService.filterStaff(result));
	}
}