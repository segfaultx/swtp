package de.hsrm.mi.swtp.exchangeplatform.service.authentication;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class AuthenticationServiceTest {

	@Autowired
	AuthenticationService authenticationService;
	
	@Test
	public void isLoginValidTestValidCredentials() {

		User.UserBuilder builder;
		String password = "hallo";

		builder = User.withUsername("test")
					  .password(new BCryptPasswordEncoder().encode(password))
					  .roles("ADMIN");
		
		UserDetails userDetails = builder.build();

		boolean actual = authenticationService.isLoginValid(password, userDetails);

		assertTrue(actual);
	}
	
	@Test
	public void isLoginValidTestInvalidCredentials() {
		
		User.UserBuilder builder;
		String password = "hallo";
		
		builder = User.withUsername("test")
					  .password(new BCryptPasswordEncoder().encode("password"))
					  .roles("ADMIN");
		
		UserDetails userDetails = builder.build();
		
		boolean actual = authenticationService.isLoginValid(password, userDetails);
		
		assertFalse(actual);
	}

}
