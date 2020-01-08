package de.hsrm.mi.swtp.exchangeplatform.messaging;

import com.github.javafaker.Faker;
import de.hsrm.mi.swtp.exchangeplatform.messaging.connectionmanager.PersonalConnectionManager;
import de.hsrm.mi.swtp.exchangeplatform.model.data.AuthenticationInformation;
import de.hsrm.mi.swtp.exchangeplatform.model.data.User;
import de.hsrm.mi.swtp.exchangeplatform.model.data.UserType;
import de.hsrm.mi.swtp.exchangeplatform.model.data.enums.Roles;
import de.hsrm.mi.swtp.exchangeplatform.model.data.enums.TypeOfUsers;
import de.hsrm.mi.swtp.exchangeplatform.service.rest.UserService;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Rule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.jms.JMSException;

import static org.junit.Assert.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class PersonalConnectionManagerTest {
	
	@Autowired
	PersonalConnectionManager personalConnectionManager;
	@Autowired
	UserService userService;
	@Autowired
	ActiveMQConnectionFactory connectionFactory;
	
	private User adminUser;
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@BeforeEach
	void setUp() {
		Faker faker = Faker.instance();
		
		String firstName = faker.name().firstName();
		String lastName = faker.funnyName().name();
		
		User dummyUser = new User();
		dummyUser.setFirstName(firstName);
		dummyUser.setLastName(lastName);
		dummyUser.setStudentNumber(null);
		dummyUser.setStaffNumber(faker.number().randomNumber(7, true));
		dummyUser.setEmail(String.format("%s.%s@hs-rm.de", firstName, lastName));
		
		AuthenticationInformation dummyUserInformation = new AuthenticationInformation();
		String username = String.format("%s%s001", firstName.charAt(0), lastName.substring(0, lastName.length()-2));
		dummyUserInformation.setUsername(username);
		dummyUserInformation.setPassword(username);
		dummyUserInformation.setRole(Roles.ADMIN);
		dummyUserInformation.setUser(dummyUser);
		dummyUser.setAuthenticationInformation(dummyUserInformation);
		
		UserType dummyUserType = new UserType();
		dummyUserType.setType(TypeOfUsers.LECTURER);
		dummyUserType.setUser(dummyUser);
		dummyUser.setUserType(dummyUserType);
		
		this.adminUser = dummyUser;
	}
	
	@Test
	@Order(1)
	public void testCreateNewConnection() {
		Assertions.assertThatCode(() -> {
			ActiveMQQueue personalQueue = personalConnectionManager.createNewConnection(adminUser);
			assertNotNull(personalQueue);
			assertTrue(personalQueue.isQueue());
		}).doesNotThrowAnyException();
	}
	
	@Test
	@Order(2)
	public void testCreateNewConnectionNullUser() {
		Assertions.assertThatCode(() -> {
			ActiveMQQueue personalQueue = personalConnectionManager.createNewConnection(null);
			assertNull(personalQueue);
		}).doesNotThrowAnyException();
	}
	
	@Test
	@Order(3)
	public void testCreateNewConnectionNullUsername() {
		Assertions.assertThatCode(() -> {
			adminUser.getAuthenticationInformation().setUsername(null);
			ActiveMQQueue personalQueue = personalConnectionManager.createNewConnection(adminUser);
			assertNull(personalQueue);
		}).doesNotThrowAnyException();
	}
	
	@Test
	@Order(4)
	public void testCreateNewConnectionNullAuthInformation() {
		Assertions.assertThatCode(() -> {
			adminUser.setAuthenticationInformation(null);
			ActiveMQQueue personalQueue = personalConnectionManager.createNewConnection(adminUser);
			assertNull(personalQueue);
		}).doesNotThrowAnyException();
	}
	
	@Test
	@Order(5)
	public void testGetConnection() throws JMSException {
		personalConnectionManager.createNewConnection(this.adminUser);
		ActiveMQQueue personalQueue = personalConnectionManager.getConnection(this.adminUser);
		assertNotNull(personalQueue);
		assertTrue(personalQueue.isQueue());
	}
	
	@Test
	@Order(6)
	public void testGetConnectionNullUser() {
		assertNull(personalConnectionManager.getConnection(null));
	}
	
	@Test
	@Order(7)
	public void testGetConnectionNonExistent() {
		assertNull(personalConnectionManager.getConnection(this.adminUser));
	}
	
	@Test
	@Order(8)
	public void testCloseConnection() throws JMSException {
		personalConnectionManager.createNewConnection(this.adminUser);
		assertTrue(personalConnectionManager.closeConnection(this.adminUser));
	}
	
	@Test
	@Order(9)
	public void testCloseConnectionNull() throws JMSException {
		assertFalse(personalConnectionManager.closeConnection(null));
	}
	
	@Test
	@Order(10)
	public void testCloseConnectionNonExistent() throws JMSException {
		assertFalse(personalConnectionManager.closeConnection(this.adminUser));
	}
	
}
