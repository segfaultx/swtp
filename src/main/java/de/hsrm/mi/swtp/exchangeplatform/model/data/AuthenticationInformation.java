package de.hsrm.mi.swtp.exchangeplatform.model.data;

import com.fasterxml.jackson.annotation.JsonBackReference;
import de.hsrm.mi.swtp.exchangeplatform.model.data.enums.Roles;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@RequiredArgsConstructor
public class AuthenticationInformation {
	
	@Id
	@GeneratedValue
	Long id;
	
	@Column(unique = true, nullable = false)
	String username;
	
	String password;
	
	@Enumerated(EnumType.STRING)
	Roles role;
	
	@OneToOne(mappedBy = "authenticationInformation")
	User user;
	
}
