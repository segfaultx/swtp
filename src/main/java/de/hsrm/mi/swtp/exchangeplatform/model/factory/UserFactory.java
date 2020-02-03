package de.hsrm.mi.swtp.exchangeplatform.model.factory;

import de.hsrm.mi.swtp.exchangeplatform.model.data.AuthenticationInformation;
import de.hsrm.mi.swtp.exchangeplatform.model.data.User;
import de.hsrm.mi.swtp.exchangeplatform.model.data.UserType;
import de.hsrm.mi.swtp.exchangeplatform.model.data.enums.Roles;
import de.hsrm.mi.swtp.exchangeplatform.model.data.enums.TypeOfUsers;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
 * A simple factory with three main creation methods:
 * • {@link #createUser(Roles, TypeOfUsers, String, String, Long, String)}: returns a new generic {@link User} which accepts any {@link TypeOfUsers},
 * • {@link #createStudent(String, String, Long, String, Long)}: returns a new {@link User} of {@link TypeOfUsers#STUDENT type STUDENT},
 * • {@link #createLecturer(String, String, Long, String)}: returns a new {@link User} of {@link TypeOfUsers#LECTURER type LECTURER}
 * • {@link #createLecturerADMIN(String, String, Long, String)}: returns a new {@link User} of {@link TypeOfUsers#LECTURER type LECTURER}
 */
@Component("userFactory")
public class UserFactory {
	
	private final static String USERNAME_BASE = "%s%s001";
	private final static Integer USERNAME_LNAME_SUBSTR = 4;
	private final static String EMAIL_BASE = "%s.%s@hs-rm.de";
	
	/** @see UserFactory */
	public User createStudent(@NonNull final String fName, @NonNull final String lName, @NonNull final Long studentNumber, @NonNull final Long currentSemester) {
		return createUser(Roles.MEMBER, TypeOfUsers.STUDENT, fName, lName, studentNumber, null, currentSemester);
	}
	
	/** @see UserFactory */
	public User createLecturer(@NonNull final String fName, @NonNull final String lName, @NonNull final Long staffNumber, @NonNull final String initials) {
		return createUser(Roles.MEMBER, TypeOfUsers.LECTURER, fName, lName, staffNumber, initials);
	}
	
	/** @see UserFactory */
	public User createLecturerADMIN(@NonNull final String fName, @NonNull final String lName, @NonNull final Long staffNumber, @NonNull final String initials) {
		return createUser(Roles.ADMIN, TypeOfUsers.LECTURER, fName, lName, staffNumber, initials);
	}
	
	/** @see UserFactory */
	public User createUser(@NonNull final Roles role, @NonNull final TypeOfUsers typeOfUser, @NonNull final String fName, @NonNull final String lName,
						   @NonNull final Long id, final String initials
						  ) {
		return createUser(role, typeOfUser, fName, lName, id, initials, 0L);
	}
	
	/** @see UserFactory */
	public User createUser(@NonNull final Roles role, @NonNull final TypeOfUsers typeOfUser, @NonNull final String fName, @NonNull final String lName,
								  @NonNull final Long id, final String initials, @NonNull final Long currentSemester
								 ) {
		User user = new User();
		AuthenticationInformation userAuthInformation = new AuthenticationInformation();
		UserType userType = new UserType();
		
		final String username = genUsername(fName, lName);
		
		userAuthInformation.setRole(role);
		userAuthInformation.setUsername(username);
		userAuthInformation.setPassword(username);
		
		userType.setType(typeOfUser);
		
		user.setInitials(initials);
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
		user.setCurrentSemester(typeOfUser.equals(TypeOfUsers.STUDENT) ? currentSemester : 0L);
		
		userType.setUser(user);
		userAuthInformation.setUser(user);
		
		return user;
	}
	
	/**
	 * Generates a username.
	 *
	 * @param fName first name of the user.
	 * @param lName last name of the user.
	 *
	 * @return a new username built like {@link #USERNAME_BASE}
	 */
	private String genUsername(@NonNull final String fName, @NonNull final String lName) {
		return replaceUlauts(String.format(USERNAME_BASE, fName.charAt(0), lName.substring(0, USERNAME_LNAME_SUBSTR)).toLowerCase());
	}
	
	/**
	 * Generates an email for a user.
	 *
	 * @param fName first name of the user.
	 * @param lName last name of the user.
	 *
	 * @return a new email built like {@link #EMAIL_BASE}
	 */
	private String genEmail(@NonNull final String fName, @NonNull final String lName) {
		return replaceUlauts(String.format(EMAIL_BASE, fName, lName).toLowerCase());
	}
	
	/** Small helper: Replaces German umlauts. */
	private String replaceUlauts(String str) {
		return str.replace("ä", "ae").replace("ö", "oe").replace("ü", "ue").replace("ß", "ss").replace("Ä", "Ae").replace("Ö", "Oe").replace("Ü", "Ue");
	}
	
}
