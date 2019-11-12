package de.hsrm.mi.swtp.exchangeplatform.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.List;

@Entity
public class Appointment {
    @Id
    @GeneratedValue
    private long id;
    private int room;
    private int capacity;
    private String lecturer;
    private String type;
    private int timeStart;
    private int timeEnd;
    private List<Student> attendees;

    public Appointment(long id, int room, int capacity, String lecturer, String type, int timeStart, int timeEnd) {
        this.id = id;
        this.room = room;
        this.capacity = capacity;
        this.lecturer = lecturer;
        this.type = type;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
    }

    public int getCapacity() { return capacity; }

    public int getRoom() { return room; }

    public String getLecturer() { return lecturer; }

    public String getType() { return type; }

    public int getTimeStart() { return timeStart; }

    public int getTimeEnd() { return timeEnd; }

    public long getId() { return id; }

    public List<Student> getAttendees() { return attendees; }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setRoom(int room) {
        this.room = room;
    }

    public void setLecturer(String lecturer) {
        this.lecturer = lecturer;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setTimeStart(int timeStart) {
        this.timeStart = timeStart;
    }

    public void setTimeEnd(int timeEnd) {
        this.timeEnd = timeEnd;
    }

    public void setId(long id) {
        this.id = id;
    }
}
