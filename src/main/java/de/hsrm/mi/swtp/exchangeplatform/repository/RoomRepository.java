package de.hsrm.mi.swtp.exchangeplatform.repository;

import de.hsrm.mi.swtp.exchangeplatform.model.data.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public interface RoomRepository extends JpaRepository<Room, Long> {
}
