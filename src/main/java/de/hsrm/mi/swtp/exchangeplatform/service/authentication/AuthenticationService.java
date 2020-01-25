package de.hsrm.mi.swtp.exchangeplatform.service.authentication;

import de.hsrm.mi.swtp.exchangeplatform.exceptions.notfound.NotFoundException;
import de.hsrm.mi.swtp.exchangeplatform.messaging.connectionmanager.PersonalQueueManager;
import de.hsrm.mi.swtp.exchangeplatform.model.authentication.*;
import de.hsrm.mi.swtp.exchangeplatform.model.data.enums.Status;
import de.hsrm.mi.swtp.exchangeplatform.service.rest.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import java.util.Optional;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AuthenticationService implements UserDetailsService {
	
	UserService userService;
	PersonalQueueManager personalQueueManager;
	JWTTokenUtils jwtTokenUtil;
	ActiveTokens activeTokens;
	
	public boolean isLoginValid(String password, UserDetails userDetails) {
		return new BCryptPasswordEncoder().matches(password, userDetails.getPassword());
	}
	
	public LoginResponseBody loginUser(final LoginRequestBody authenticationRequest) throws NotFoundException, JMSException {
		final de.hsrm.mi.swtp.exchangeplatform.model.data.User user = userService.getByUsername(authenticationRequest.getUsername())
									 .orElseThrow(NotFoundException::new);
		
		final String token = jwtTokenUtil.generateToken(user);
		
		activeTokens.addToken(token);
		
		JWTResponse response = new JWTResponse(token);
		
		ActiveMQQueue personalQueue = personalQueueManager.createNewConnection(user);
		log.info("USER LOGIN: " + authenticationRequest.getUsername());
		return LoginResponseBody.builder()
								.tokenResponse(response)
								.queueName(personalQueue.getQueueName())
								.build();
	}
	
	public LogoutResponseBody logoutUser(de.hsrm.mi.swtp.exchangeplatform.model.data.User user, String token) throws JMSException {

		activeTokens.removeToken(token);
		
		boolean isLoggedOut = personalQueueManager.closeConnection(user);
		return LogoutResponseBody.builder()
								 .message(String.format("Logout was %ssuccessful.", isLoggedOut ? "" : "un"))
								 .status(isLoggedOut ? Status.SUCCESS : Status.FAIL)
								 .build();
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<de.hsrm.mi.swtp.exchangeplatform.model.data.User> user = userService.getByUsername(username);
		
		User.UserBuilder builder;
		if(user.isPresent()) {
			de.hsrm.mi.swtp.exchangeplatform.model.data.User found = user.get();
			builder = org.springframework.security.core.userdetails.User.withUsername(username);
			builder.password(new BCryptPasswordEncoder().encode(found.getAuthenticationInformation().getPassword()));
			builder.roles(found.getAuthenticationInformation().getRole().name());
		} else {
			throw new UsernameNotFoundException("User not found.");
		}
		
		return builder.build();
	}
}
