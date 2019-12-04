package de.hsrm.mi.swtp.exchangeplatform.model.data;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import de.hsrm.mi.swtp.exchangeplatform.exceptions.notfound.ModelNotFoundException;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Data
@RequiredArgsConstructor
@Table(name = "timeslot")
public class Timeslot implements Model {

    /** A unique identifier of an appointment by which it can be found. */
    @Id
    @GeneratedValue
    private Long id;

    /** The room in which the appointment will take place. */
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Room room;

    /** The day on which the appointment is scheduled for. */
    private String day;

    /** The scheduled beginning of the appointment. */
    @JsonProperty("time_start")
    @JsonSerialize(using = LocalTimeSerializer.class)
    private LocalTime timeStart;  // string nur weil aktuell in lokalen DB ein falscher Eintrag war

    /** The scheduled end time of the appointment. */
    @JsonProperty("time_end")
    @JsonSerialize(using = LocalTimeSerializer.class)
    private LocalTime timeEnd;  // string nur weil aktuell in lokalen DB ein falscher Eintrag war

    /** The lecturer of the appointment. */
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Lecturer lecturer;

    /**
     * The type of the appointment. Is of type {@link}.
     */
    private String type;

    /** The maximum amount of {@link Student Students} which can join an appointment. */
    private int capacity;

    /** A list of {@link Student Students} which have joined an appointment. */
    @JsonBackReference
    @ManyToMany(cascade = CascadeType.ALL)
    private List<Student> attendees;

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "module_id")
    private Module module;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private TimeTable timeTable;

    /**
     * A small filter which searches {@link #attendees}.
     *
     * @return a {@link Student} if found in {@link #attendees}. Otherwise will return null.
     */
    private Student filterAttendeesForStudent(Student student) {
        List<Student> students = this.attendees
                .stream()
                .filter(student1 -> student1.getMatriculationNumber().equals(student.getMatriculationNumber()))
                .collect(Collectors.toList());
        return students.isEmpty() ? null : students.get(0);
    }

    /**
     * Adds a student to the list of {@link #attendees} if student is not an attendee already.
     *
     * @param student the student which is to be added to the list of {@link #attendees}.
     */
    public boolean addAttendee(Student student) {
        return this.filterAttendeesForStudent(student) == null && this.attendees.add(student);
    }

    /**
     * Removes a student from the list of {@link #attendees} of an appointment.
     *
     * @param student the {@link Student} which leaves an appointment.
     * @return a boolean which indicated whether the student was removed successfully.
     * @throws ModelNotFoundException if the given student was not found in the list of {@link #attendees}.
     */
    public boolean removeAttendee(Student student) {
        Student foundStudent = this.filterAttendeesForStudent(student);
        if (foundStudent == null) {
            throw new ModelNotFoundException(student);
        }
        return this.attendees.remove(student);
    }

}
