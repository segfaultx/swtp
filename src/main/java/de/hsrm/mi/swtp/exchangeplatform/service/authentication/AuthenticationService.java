package de.hsrm.mi.swtp.exchangeplatform.service.authentication;

import de.hsrm.mi.swtp.exchangeplatform.exceptions.notfound.NotFoundException;
import de.hsrm.mi.swtp.exchangeplatform.messaging.PersonalQueueManager;
import de.hsrm.mi.swtp.exchangeplatform.model.authentication.JWTResponse;
import de.hsrm.mi.swtp.exchangeplatform.model.authentication.LoginRequestBody;
import de.hsrm.mi.swtp.exchangeplatform.model.authentication.LoginResponseBody;
import de.hsrm.mi.swtp.exchangeplatform.model.data.UserModel;
import de.hsrm.mi.swtp.exchangeplatform.service.rest.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import java.util.Optional;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AuthenticationService implements UserDetailsService {
	
	UserService userService;
	PersonalQueueManager personalQueueManager;
	JWTTokenUtils jwtTokenUtil;
	
	public boolean isLoginValid(LoginRequestBody requestBody) {
		String username = requestBody.getUsername();
		String password = requestBody.getPassword();
		
		// TODO: Useful implementation
		return username.equals(password);
	}
	
	public LoginResponseBody loginUser(final LoginRequestBody authenticationRequest) throws NotFoundException, JMSException {
		UserModel user = userService.getByUsername(authenticationRequest.getUsername())
																		   .orElseThrow(NotFoundException::new);
		final String token = jwtTokenUtil.generateToken(user);
		JWTResponse response = new JWTResponse(token);
		
		return LoginResponseBody.builder()
								.tokenResponse(response)
								.queueName(personalQueueManager.createNewConnection(token, user).getQueueName())
								.build();
	}
	
	public boolean hasToken() {
		return false;
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<UserModel> user = userService.getByUsername(username);
		
		User.UserBuilder builder;
		if(user.isPresent()) {
			UserModel found = user.get();
			builder = org.springframework.security.core.userdetails.User.withUsername(username);
			builder.password(new BCryptPasswordEncoder().encode(found.getAuthenticationInformation().getUsername())); // TODO: add password field in User entity
			builder.roles(found.getAuthenticationInformation().getRole().name());
		} else {
			throw new UsernameNotFoundException("User not found.");
		}
		
		return builder.build();
	}
}
