package de.hsrm.mi.swtp.exchangeplatform.model.data;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@RequiredArgsConstructor
public class AuthenticationInformation {
	
	@Id
	Long id;
	
	@Column(unique = true, nullable = false)
	String username;
	
	String password;
	
	String role;
	
	@OneToOne(mappedBy = "authenticationInformation")
	User user;
	
}
