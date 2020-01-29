package de.hsrm.mi.swtp.exchangeplatform.service.authentication;

import de.hsrm.mi.swtp.exchangeplatform.exceptions.notfound.NotFoundException;
import de.hsrm.mi.swtp.exchangeplatform.model.authentication.LoginRequestBody;
import de.hsrm.mi.swtp.exchangeplatform.model.authentication.LoginResponseBody;
import de.hsrm.mi.swtp.exchangeplatform.model.authentication.LogoutResponseBody;
import de.hsrm.mi.swtp.exchangeplatform.service.rest.UserService;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.jms.JMSException;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext
public class AuthenticationServiceTest {
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	@Autowired
	AuthenticationService authenticationService;
	@Autowired
	UserService userService;
	
	@Test
	public void isLoginValidTestValidCredentials() {
		
		User.UserBuilder builder;
		String password = "hallo";
		
		builder = User.withUsername("test").password(new BCryptPasswordEncoder().encode(password)).roles("ADMIN");
		
		UserDetails userDetails = builder.build();
		
		boolean actual = authenticationService.isLoginValid(password, userDetails);
		
		assertTrue(actual);
	}
	
	@Test
	public void isLoginValidTestInvalidCredentials() {
		
		User.UserBuilder builder;
		String password = "hallo";
		
		builder = User.withUsername("test").password(new BCryptPasswordEncoder().encode("password")).roles("ADMIN");
		
		UserDetails userDetails = builder.build();
		
		boolean actual = authenticationService.isLoginValid(password, userDetails);
		
		assertFalse(actual);
	}
	
	@Test
	public void testLoadUserByUsername() {
		String expected = "dscha001";
		UserDetails userDetails = authenticationService.loadUserByUsername(expected);
		String actual = userDetails.getUsername();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testLoadByUsernameThrowUsernameNotFoundException() {
		assertThrows(UsernameNotFoundException.class, () -> {
			authenticationService.loadUserByUsername("garbagename");
		});
	}
	
	//@Test
	public void testLoginUserThrowNotFoundException() {
		assertThrows(NotFoundException.class, () -> {
			LoginRequestBody requestBody = new LoginRequestBody();
			requestBody.setUsername("garbagename");
			authenticationService.loginUser(requestBody);
		});
	}
	
	@Test
	public void testLoginUserWithValidCredentials() throws NotFoundException, JMSException {
		LoginRequestBody requestBody = new LoginRequestBody();
		requestBody.setUsername("dscha001");
		requestBody.setPassword("dscha001");
		LoginResponseBody responseBody = authenticationService.loginUser(requestBody);
		assertNotNull(responseBody);
	}
	
	@Test
	public void testLogoutUserThrowNotFoundException() {
		assertThrows(NotFoundException.class, () -> {
			de.hsrm.mi.swtp.exchangeplatform.model.data.User user = new de.hsrm.mi.swtp.exchangeplatform.model.data.User();
			user.getAuthenticationInformation().setUsername("garbageUsername");
			authenticationService.logoutUser(user, "");
		});
	}
	
	//@Test
	public void testLogoutUserWithValidCredentials() throws NotFoundException, JMSException {
		
		// First Login
		LoginRequestBody requestBody = new LoginRequestBody();
		requestBody.setUsername("dscha001");
		requestBody.setPassword("dscha001");
		LoginResponseBody responseBody = authenticationService.loginUser(requestBody);
		String token = JWTTokenUtils.tokenWithoutPrefix(responseBody.getTokenResponse().getToken());
		
		de.hsrm.mi.swtp.exchangeplatform.model.data.User user = userService.getByUsername("dscha001").orElseThrow(NotFoundException::new);
		
		// Then Logout
		LogoutResponseBody logoutResponseBody = authenticationService.logoutUser(user, token);
		assertNotNull(logoutResponseBody);
	}
	
}
