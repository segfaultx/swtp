package de.hsrm.mi.swtp.exchangeplatform.service.rest;

import de.hsrm.mi.swtp.exchangeplatform.model.data.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class UserServiceTest {

	@Autowired
	private UserService service;
	
	private List<User> users = new ArrayList<>();
	
	@BeforeEach
	public void getAllUsers() {
		users = service.getAll();
	}
	
	@Test
	public void testGetUsersByValidUsername() {
		User expected = users.get(0);
		Optional<User> actual = service.getByUsername(expected.getAuthenticationInformation().getUsername());
		
		if(actual.isEmpty()) fail("No Users in Database");
		
		assertEquals(expected, actual.get());
	}

}
