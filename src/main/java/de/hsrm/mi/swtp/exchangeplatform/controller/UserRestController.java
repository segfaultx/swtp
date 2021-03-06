package de.hsrm.mi.swtp.exchangeplatform.controller;

import de.hsrm.mi.swtp.exchangeplatform.exceptions.notfound.NotFoundException;
import de.hsrm.mi.swtp.exchangeplatform.messaging.connectionmanager.PersonalQueueManager;
import de.hsrm.mi.swtp.exchangeplatform.model.data.User;
import de.hsrm.mi.swtp.exchangeplatform.service.rest.TradeOfferService;
import de.hsrm.mi.swtp.exchangeplatform.service.rest.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.jms.JMSException;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@SecurityRequirement(name = "Authorization")
@RestController
@RequestMapping("/api/v1/users")
public class UserRestController {
	
	String BASEURL = "/api/v1/users";
	UserService userService;
	TradeOfferService tradeOfferService;
	PersonalQueueManager personalQueueManager;
	
	@GetMapping("")
	@Operation(description = "get all users", operationId = "getAllUsers", tags = {"users"})
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "successfully retrieved users"),
							@ApiResponse(responseCode = "403", description = "unauthorized fetch attempt"),
							@ApiResponse(responseCode = "400", description = "malformed fetch request") })
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> getAll(@RequestParam(value = "username", required = false) String username) throws NotFoundException {
		log.info("GET // " + BASEURL);
		if(username != null && username.length() > 0) {
			User user = userService.getByUsername(username)
										.orElseThrow(NotFoundException::new);
			return ResponseEntity.ok(user);
		}
		return ResponseEntity.ok(userService.getAll());
	}
	
	@GetMapping("/{userId}")
	@Operation(description = "get user by id", operationId = "getUserById", tags = {"users"})
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "successfully retrieved user"),
							@ApiResponse(responseCode = "403", description = "unauthorized fetch attempt"),
							@ApiResponse(responseCode = "400", description = "malformed fetch request") })
	@PreAuthorize("hasRole('MEMBER') or hasRole('ADMIN')")
	public ResponseEntity<User> getById(@PathVariable Long userId) throws NotFoundException, JMSException {
		log.info(String.format("GET // " + BASEURL + "/%s", userId));
		User user = userService.getById(userId)
									.orElseThrow(NotFoundException::new);
		return ResponseEntity.ok(user);
	}
	
}
