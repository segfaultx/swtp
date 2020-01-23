package de.hsrm.mi.swtp.exchangeplatform.service.admin.po.filter;

import de.hsrm.mi.swtp.exchangeplatform.messaging.connectionmanager.PersonalConnectionManager;
import de.hsrm.mi.swtp.exchangeplatform.messaging.sender.PersonalMessageSender;
import de.hsrm.mi.swtp.exchangeplatform.model.data.User;
import de.hsrm.mi.swtp.exchangeplatform.model.data.enums.RestrictionType;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class PORestrictionViolationService {

	Map<Long, UserOccupancyViolation> userOccupancyViolations;
	PersonalConnectionManager personalConnectionManager;
	PersonalMessageSender personalMessageSender;

	/** Add a new violation for a given {@link User student object}. A pre-existing violation entry will be extended with the given arguments. */
	public void addViolation(User student, RestrictionType restrictionType, Object value) {
		final Long studentId = student.getId();
		final UserOccupancyViolation violationFromMap = this.userOccupancyViolations.getOrDefault(studentId, null);
		
		if(violationFromMap == null) {
			// violations for under key studentId does not exist
			// so create a new entry
			this.userOccupancyViolations.put(studentId, createViolation(student, restrictionType, value));
			return;
		}
		this.userOccupancyViolations.put(studentId, extendViolation(violationFromMap, restrictionType, value));
	}
	
	/** Creates a new {@link UserOccupancyViolation} instance. */
	private UserOccupancyViolation createViolation(User student, RestrictionType restrictionType, Object value) {
		Map<RestrictionType, Object> violation = new HashMap<>();
		violation.put(restrictionType, value);
		return UserOccupancyViolation.builder()
									 .student(student)
									 .violations(violation)
									 .build();
	}
	
	/** Extends given {@link UserOccupancyViolation} instance by adding a (new) {@link RestrictionType} and its corresponding value. */
	private UserOccupancyViolation extendViolation(UserOccupancyViolation original, RestrictionType restrictionType, Object value) {
		Map<RestrictionType, Object> violation = original.getViolations();
		violation.put(restrictionType, value);
		return UserOccupancyViolation.builder()
									 .student(original.getStudent())
									 .violations(violation)
									 .build();
	}
	
	/** Calls {@link de.hsrm.mi.swtp.exchangeplatform.messaging.connectionmanager.PersonalConnectionManager#send(de.hsrm.mi.swtp.exchangeplatform.model.data.User, java.lang.String)} to notify all students with violations. */
	public void notifyUsersWithViolations() {
		for(UserOccupancyViolation userOccupancyViolation : this.userOccupancyViolations.values()) {
			log.info(" // SEND VIOLATIONS TO STUDENT ==> " +userOccupancyViolation.getStudent().getAuthenticationInformation().getUsername());
			personalMessageSender.send(userOccupancyViolation);
//			personalMessageSender.send(userOccupancyViolation, "Violation detected. Please make sure you.");
		}
	}
	
}
