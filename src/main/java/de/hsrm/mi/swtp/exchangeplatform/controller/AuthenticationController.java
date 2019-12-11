package de.hsrm.mi.swtp.exchangeplatform.controller;

import de.hsrm.mi.swtp.exchangeplatform.model.authentication.LoginRequestBody;
import de.hsrm.mi.swtp.exchangeplatform.model.data.Student;
import de.hsrm.mi.swtp.exchangeplatform.service.authentication.AuthenticationService;
import de.hsrm.mi.swtp.exchangeplatform.service.authentication.JWTTokenUtils;
import de.hsrm.mi.swtp.exchangeplatform.service.rest.StudentService;
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
	StudentService studentService;
	AuthenticationManager authenticationManager;
	
	@Autowired
	JWTTokenUtils jwtTokenUtil;
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequestBody authenticationRequest) throws Exception {
		authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
		
		if(!authenticationService.isLoginValid(authenticationRequest)) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		
		Student student = studentService.getByUsername(authenticationRequest.getUsername());
		
		final String token = jwtTokenUtil.generateToken(student);
		return ResponseEntity.ok(token);
	}
	
	@GetMapping("/whoami")
	public ResponseEntity<?> getUser(@RequestHeader("Authorization") String token) {
		return ResponseEntity.ok(jwtTokenUtil.getUsernameFromToken(token));
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
