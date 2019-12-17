package de.hsrm.mi.swtp.exchangeplatform.controller;

import de.hsrm.mi.swtp.exchangeplatform.exceptions.notfound.NotFoundException;
import de.hsrm.mi.swtp.exchangeplatform.model.authentication.JWTResponse;
import de.hsrm.mi.swtp.exchangeplatform.model.authentication.LoginRequestBody;
import de.hsrm.mi.swtp.exchangeplatform.model.authentication.WhoAmI;
import de.hsrm.mi.swtp.exchangeplatform.model.data.User;
import de.hsrm.mi.swtp.exchangeplatform.service.authentication.AuthenticationService;
import de.hsrm.mi.swtp.exchangeplatform.service.authentication.JWTTokenUtils;
import de.hsrm.mi.swtp.exchangeplatform.service.rest.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthenticationController {
	
	AuthenticationService authenticationService;
	UserService userService;
	AuthenticationManager authenticationManager;
	JWTTokenUtils jwtTokenUtil;
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequestBody authenticationRequest) throws Exception {
		authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
		
		if(!authenticationService.isLoginValid(authenticationRequest)) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		
		User user = userService.getByUsername(authenticationRequest.getUsername())
				.orElseThrow(NotFoundException::new);
		
		final String token = jwtTokenUtil.generateToken(user);
		JWTResponse response = new JWTResponse(token);
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/whoami")
	public ResponseEntity<WhoAmI> getUser(@RequestHeader("Authorization") String token) throws Exception {
		if(!jwtTokenUtil.isValidToken(token)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		
		String jwtToken = jwtTokenUtil.tokenWithoutPrefix(token);
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
