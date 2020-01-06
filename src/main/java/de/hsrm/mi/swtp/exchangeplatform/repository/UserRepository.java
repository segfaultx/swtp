package de.hsrm.mi.swtp.exchangeplatform.repository;

import de.hsrm.mi.swtp.exchangeplatform.model.data.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor {
	
	@Query("select user from User user join user.authenticationInformation ai where ai.username =:username")
	Optional<User> findByUsername(@Param("username") String username);
	
	@Query(value = "SELECT * from User where cast(student_number as char) like CONCAT('%', :studentNumber, '%')" , nativeQuery = true)
	List<User> findByStudentNumberContaining(@Param("studentNumber") String studentNumber);
	
	@Query(value = "SELECT * from User where cast(staff_number as char) like CONCAT('%', :staffNumber, '%')" , nativeQuery = true)
	List<User> findByStaffNumberContaining(@Param("staffNumber") String staffNumber);
	
	List<User> findAllByFirstNameContainingIgnoreCase(String firstName);
	
	List<User> findAllByLastNameContainingIgnoreCase(String lastName);
	
	
	
	User findByStudentNumber(Long studentnumber);
}
