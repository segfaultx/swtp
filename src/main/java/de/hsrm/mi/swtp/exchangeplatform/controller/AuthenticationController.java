package de.hsrm.mi.swtp.exchangeplatform.controller;

import de.hsrm.mi.swtp.exchangeplatform.exceptions.notfound.NotFoundException;
import de.hsrm.mi.swtp.exchangeplatform.model.authentication.LoginRequestBody;
import de.hsrm.mi.swtp.exchangeplatform.model.authentication.LoginResponseBody;
import de.hsrm.mi.swtp.exchangeplatform.model.authentication.LogoutRequestBody;
import de.hsrm.mi.swtp.exchangeplatform.model.authentication.LogoutResponseBody;
import de.hsrm.mi.swtp.exchangeplatform.model.authentication.WhoAmI;
import de.hsrm.mi.swtp.exchangeplatform.model.data.User;
import de.hsrm.mi.swtp.exchangeplatform.model.data.enums.Status;
import de.hsrm.mi.swtp.exchangeplatform.service.authentication.AuthenticationService;
import de.hsrm.mi.swtp.exchangeplatform.service.authentication.JWTTokenUtils;
import de.hsrm.mi.swtp.exchangeplatform.service.rest.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@Slf4j
@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@SecurityScheme(name = "Authorization",
        type = SecuritySchemeType.APIKEY,
		in = SecuritySchemeIn.HEADER)
@SecurityRequirement(name = "Authorization")
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthenticationController {
	
	AuthenticationService authenticationService;
	UserService userService;
	AuthenticationManager authenticationManager;
	JWTTokenUtils jwtTokenUtil;
	
	@PostMapping("/login")
	@ApiOperation(value = "login to application", nickname = "login")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "successfully logged in"),
							@ApiResponse(code = 400, message = "malformed login request") })
	public ResponseEntity<?> login(@RequestBody LoginRequestBody authenticationRequest) throws Exception {
		authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
		
		UserDetails userDetails = authenticationService.loadUserByUsername(authenticationRequest.getUsername());
		String password = authenticationRequest.getPassword();
		
		if(!authenticationService.isLoginValid(password, userDetails))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Credentials");
		try {
			LoginResponseBody responseBody = authenticationService.loginUser(authenticationRequest);
			return ResponseEntity.ok(responseBody);
		} catch(NotFoundException e) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
	}
	
	@PostMapping("/logout")
	public ResponseEntity<?> logout(@RequestHeader("Authorization") String token) throws Exception {
		if(!JWTTokenUtils.isValidToken(token)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		
		String jwtToken = JWTTokenUtils.tokenWithoutPrefix(token);
		String usernameFromToken = jwtTokenUtil.getUsernameFromToken(jwtToken);
		User found = userService.getByUsername(usernameFromToken).orElseThrow(NotFoundException::new);
		

		LogoutResponseBody logoutResponseBody = authenticationService.logoutUser(found, token);
		
		if(logoutResponseBody.getStatus().equals(Status.FAIL)) return ResponseEntity.badRequest().body(logoutResponseBody);
		return ResponseEntity.ok(logoutResponseBody);
		
	}
	
	@GetMapping("/whoami")
	@ApiOperation(value = "whoami", nickname = "whoami")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "successfully fetched authentitacion info"),
							@ApiResponse(code = 400, message = "malformed authinfo fetch request") })
	public ResponseEntity<WhoAmI> getUser(@RequestHeader("Authorization") String token) throws Exception {
		if(!JWTTokenUtils.isValidToken(token)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		
		String jwtToken = JWTTokenUtils.tokenWithoutPrefix(token);
		String usernameFromToken = jwtTokenUtil.getUsernameFromToken(jwtToken);
		User found = userService.getByUsername(usernameFromToken).orElseThrow(NotFoundException::new);
		WhoAmI whoAmI = userService.getWhoAmI(found);
		return ResponseEntity.ok(whoAmI);
	}

	private void authenticate(String username, String password) throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
	}
	

}
