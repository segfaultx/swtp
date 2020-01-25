package de.hsrm.mi.swtp.exchangeplatform.model.data;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreType;
import de.hsrm.mi.swtp.exchangeplatform.model.data.enums.Roles;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Data
@ToString(exclude = {"user"})
@RequiredArgsConstructor
@JsonIgnoreType
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
	@JsonBackReference("user-authinformation")
    User user;
	
}
