package de.hsrm.mi.swtp.exchangeplatform.model.factory;

import de.hsrm.mi.swtp.exchangeplatform.model.data.AuthenticationInformation;
import de.hsrm.mi.swtp.exchangeplatform.model.data.User;
import de.hsrm.mi.swtp.exchangeplatform.model.data.UserType;
import de.hsrm.mi.swtp.exchangeplatform.model.data.enums.Roles;
import de.hsrm.mi.swtp.exchangeplatform.model.data.enums.TypeOfUsers;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component("userFactory")
public class UserFactory {
	
	private final static String USERNAME_BASE = "%s%s001";
	private final static Integer USERNAME_LNAME_SUBSTR = 4;
	private final static String EMAIL_BASE = "%s.%s@hs-rm.de";
	
	public static User createStudent(@NonNull final String fName, @NonNull final String lName, @NonNull final Long studentNumber) {
		return createUser(Roles.MEMBER, TypeOfUsers.STUDENT, fName, lName, studentNumber);
	}
	
	public static User createLecturer(@NonNull final String fName, @NonNull final String lName, @NonNull final Long staffNumber) {
		return createUser(Roles.MEMBER, TypeOfUsers.LECTURER, fName, lName, staffNumber);
	}
	
	public static User createLecturerADMIN(@NonNull final String fName, @NonNull final String lName, @NonNull final Long staffNumber) {
		return createUser(Roles.ADMIN, TypeOfUsers.LECTURER, fName, lName, staffNumber);
	}
	
	public static User createUser(@NonNull final Roles role, @NonNull final TypeOfUsers typeOfUser, @NonNull final String fName, @NonNull final String lName,
								  @NonNull final Long id
								 ) {
		User user = new User();
		AuthenticationInformation userAuthInformation = new AuthenticationInformation();
		UserType userType = new UserType();
		
		final String username = genUsername(fName, lName);
		
		userAuthInformation.setRole(role);
		userAuthInformation.setUsername(username);
		userAuthInformation.setPassword(username);
		
		userType.setType(typeOfUser);
		
		user.setTradeoffers(new ArrayList<>());
		user.setTimeslots(new ArrayList<>());
		user.setCompletedModules(new ArrayList<>());
		user.setEmail(genEmail(fName, lName));
		user.setFirstName(fName);
		user.setLastName(lName);
		user.setStudentNumber(typeOfUser.equals(TypeOfUsers.STUDENT) ? id : null);
		user.setStaffNumber(typeOfUser.equals(TypeOfUsers.LECTURER) ? id : null);
		user.setCp(0);
		user.setFairness(0);
		user.setAuthenticationInformation(userAuthInformation);
		user.setUserType(userType);
		
		userType.setUser(user);
		userAuthInformation.setUser(user);
		
		return user;
	}
	
	private static String genUsername(@NonNull final String fName, @NonNull final String lName) {
		return replaceUlauts(String.format(USERNAME_BASE, fName.charAt(0), lName.substring(0, USERNAME_LNAME_SUBSTR)).toLowerCase());
	}
	
	private static String genEmail(@NonNull final String fName, @NonNull final String lName) {
		return replaceUlauts(String.format(EMAIL_BASE, fName, lName).toLowerCase());
	}
	
	private static String replaceUlauts(String str) {
		return str.replace("ä", "ae").replace("ö", "oe").replace("ü", "ue").replace("ß", "ss").replace("Ä", "Ae").replace("Ö", "Oe").replace("Ü", "Ue");
	}
	
}
