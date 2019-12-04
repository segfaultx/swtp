package de.hsrm.mi.swtp.exchangeplatform.repository;

import de.hsrm.mi.swtp.exchangeplatform.model.data.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * UserRepository retrieves one user by its username and communicates with UserService
 */

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    //List<User> findAll();

    User findByUsername(String username);
}
