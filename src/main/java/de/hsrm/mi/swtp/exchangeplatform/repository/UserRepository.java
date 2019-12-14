package de.hsrm.mi.swtp.exchangeplatform.repository;

import de.hsrm.mi.swtp.exchangeplatform.model.data.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserModel, Long> {
	
	@Query("select user from UserModel user join user.authenticationInformation ai where ai.username =:username")
	Optional<UserModel> findByUsername(@Param("username") String username);
}
