package de.hsrm.mi.swtp.exchangeplatform.service.authentication;

import de.hsrm.mi.swtp.exchangeplatform.configuration.authentication.SecurityConfig;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithSecurityContext;
import org.springframework.security.test.context.support.WithUserDetails;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class AuthenticationServiceTest {

	@Autowired
	AuthenticationService authenticationService;
	
	@Test
	@WithMockUser(username = "test", password = "hallo")
	@WithUserDetails
	public void isLoginValidTestValidCredentials() {
		
		User.UserBuilder builder;
		String password = "hallo";
		
		builder = User.withUsername("test").password(new BCryptPasswordEncoder().encode(password));
		
		UserDetails userDetails = builder.build();
		
		boolean actual = authenticationService.isLoginValid(password, userDetails);
		
		assertTrue(actual);
	}

}
