package de.hsrm.mi.swtp.exchangeplatform.service.authentication;

import de.hsrm.mi.swtp.exchangeplatform.exceptions.notfound.NotFoundException;
import de.hsrm.mi.swtp.exchangeplatform.messaging.connectionmanager.PersonalConnectionManager;
import de.hsrm.mi.swtp.exchangeplatform.messaging.message.LoginSuccessfulMessage;
import de.hsrm.mi.swtp.exchangeplatform.model.authentication.*;
import de.hsrm.mi.swtp.exchangeplatform.model.data.enums.Status;
import de.hsrm.mi.swtp.exchangeplatform.service.rest.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AuthenticationService implements UserDetailsService {
	
	UserService userService;
	PersonalConnectionManager personalConnectionManager;
	JWTTokenUtils jwtTokenUtil;
	JmsTemplate jmsTemplate;
	
	public boolean isLoginValid(LoginRequestBody requestBody) {
		String username = requestBody.getUsername();
		String password = requestBody.getPassword();
		
		UserDetails userDetails = loadUserByUsername(username);
		
		return new BCryptPasswordEncoder().matches(password, userDetails.getPassword());
	}
	
	public LoginResponseBody loginUser(final LoginRequestBody authenticationRequest) throws NotFoundException, JMSException {
		final de.hsrm.mi.swtp.exchangeplatform.model.data.User user = userService.getByUsername(authenticationRequest.getUsername())
									 .orElseThrow(NotFoundException::new);
		final String token = jwtTokenUtil.generateToken(user);
		JWTResponse response = new JWTResponse(token);
		
		ActiveMQQueue personalQueue = personalConnectionManager.createNewConnection(user);
		LoginSuccessfulMessage message = LoginSuccessfulMessage.builder()
															   .message(String.format("Log in at %s successful.", LocalDateTime.now().toString()))
															   .build();
		jmsTemplate.send(personalQueue, session -> session.createObjectMessage(message));
		return LoginResponseBody.builder()
								.tokenResponse(response)
								.queueName(personalQueue.getQueueName())
								.build();
	}
	
	public LogoutResponseBody logoutUser(final LogoutRequestBody logoutRequestBody) throws NotFoundException, JMSException {
		final de.hsrm.mi.swtp.exchangeplatform.model.data.User user = userService.getByUsername(logoutRequestBody.getUsername())
									 .orElseThrow(NotFoundException::new);
		boolean isLoggedOut = personalConnectionManager.closeConnection(user);
		return LogoutResponseBody.builder()
								 .message(String.format("Logout was %ssuccessful.", isLoggedOut ? "" : "un"))
								 .status(isLoggedOut ? Status.SUCCESS : Status.FAIL)
								 .build();
	}
	
	public boolean hasToken() {
		return false;
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
