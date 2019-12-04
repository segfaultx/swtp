//package de.hsrm.mi.swtp.exchangeplatform.service.rest;
//
//
//import de.hsrm.mi.swtp.exchangeplatform.exceptions.StudentNotUpdatedException;
//
//import de.hsrm.mi.swtp.exchangeplatform.exceptions.notcreated.NotCreatedException;
//import de.hsrm.mi.swtp.exchangeplatform.exceptions.notfound.NotFoundException;
//
//import de.hsrm.mi.swtp.exchangeplatform.model.data.User;
//import de.hsrm.mi.swtp.exchangeplatform.repository.UserRepository;
//import lombok.AccessLevel;
//import lombok.RequiredArgsConstructor;
//import lombok.experimental.FieldDefaults;
//import lombok.extern.slf4j.Slf4j;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.Objects;
//import java.util.Optional;
//
///**
// * User(Detail)Service for retrieving the User object using the repository, and, if it exists, wrap it into a User(Details) object, which implements UserDetails
// */
//
//@Slf4j
//@Service
//@RequiredArgsConstructor
//@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
//public class UserService implements UserDetailsService, RestService<User, Long> {
//
//    //private static final Logger log = LoggerFactory.getLogger(UserService.class);
//
//
//    @Autowired private UserRepository userRepository;
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        User user= userRepository.findByUsername(username);
//        if (user==null){throw new UsernameNotFoundException(username);}
//
//        /**
//         * return UserObject with username, password and role.
//         */
//
//        return org.springframework.security.core.userdetails.User
//                .withUsername(username)
//                .password(user.getPassword())
//                .roles(user.getRole())
//                .build();
//    }
//
//    public List<User> getAll() {
//        return userRepository.findAll();
//    }
//
//    @Override
//    public User getById(Long matriculationNumber) {
//        Optional<User> UserOptional = this.userRepository.findById(matriculationNumber);
//        if (!UserOptional.isPresent()) throw new NotFoundException(matriculationNumber);
//        return UserOptional.get();
//    }
//
//    @Override
//    public void save(User user) throws IllegalArgumentException {
//        if (this.userRepository.existsById(user.getMatriculationNumber())) {
//            log.info(String.format("FAIL: User %s not created", user));
//            throw new NotCreatedException(user);
//        }
//        userRepository.save(user);
//        log.info(String.format("SUCCESS: User %s created", user));
//    }
//
//    @Override
//    public void delete(Long matriculationNumber) throws IllegalArgumentException {
//        this.userRepository.delete(this.getById(matriculationNumber));
//        }
//
//
//    @Override
//    public boolean update(Long matriculationNumber, User update) throws IllegalArgumentException {
//        User user = this.getById(matriculationNumber);
//
//        if (!Objects.equals(user.getMatriculationNumber(), update.getMatriculationNumber())) {
//            throw new StudentNotUpdatedException();
//        }
//
//        log.info("Updating User..");
//        log.info(user.toString() + " -> " + update.toString());
//        //user.setTimeslots(update.getTimeslots());
//        user.setUsername(update.getUsername());
//
//        this.save(user);
//
//        return true;
//    }
//}
