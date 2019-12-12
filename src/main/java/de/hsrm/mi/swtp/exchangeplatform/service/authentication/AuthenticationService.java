package de.hsrm.mi.swtp.exchangeplatform.service.authentication;

import de.hsrm.mi.swtp.exchangeplatform.model.authentication.LoginRequestBody;
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

import java.util.Optional;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AuthenticationService implements UserDetailsService {
	
	UserService userService;
	
	public boolean isLoginValid(LoginRequestBody requestBody) {
		String username = requestBody.getUsername();
		String password = requestBody.getPassword();
		
		// TODO: Useful implementation
		return username.equals(password);
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
			builder.password(new BCryptPasswordEncoder().encode(found.getAuthenticationInformation().getUsername())); // TODO: add password field in User entity
			builder.roles(found.getAuthenticationInformation().getRole());
		} else {
			throw new UsernameNotFoundException("User not found.");
		}
		
		return builder.build();
	}
}
