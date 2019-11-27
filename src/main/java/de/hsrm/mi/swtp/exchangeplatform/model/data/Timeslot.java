package de.hsrm.mi.swtp.exchangeplatform.model.data;

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
public class Timeslot implements Model {

    /** A unique identifier of an timeslot by which it can be found. */
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    /** The day on which the timeslot is scheduled for. */
    @JsonProperty
    @Column(name = "weekday")
    private Integer day;

    /**
     * The type of the timeslot. Is of type {@link}.
     */
    @JsonProperty
    @Column(name ="timeslot_type")
    @Enumerated(EnumType.STRING)
    private Type type;

    /** The scheduled beginning of the timeslot. */
    @JsonIgnore
    @JsonProperty("time_start")
    @JsonSerialize(using = LocalTimeSerializer.class)
    @Column(name = "time_start")
    private LocalTime timeStart;

    /** The scheduled end time of the timeslot. */
    @JsonIgnore
    @JsonProperty("time_end")
    @JsonSerialize(using = LocalTimeSerializer.class)
    @Column(name = "time_end")
    private LocalTime timeEnd;

    /** The lecturer of the timeslot. */
    @JsonIgnore
    @ManyToOne
    private Lecturer lecturer;

    /** The maximum amount of {@link Student Students} which can join an timeslot. */
    @JsonIgnore
    @Column(name = "max_capacity")
    private int capacity;

    /** The room in which the timeslot will take place. */
    @JsonIgnore
    @ManyToOne
    @JsonProperty
    @JoinColumn(name = "room")
    private Room room;

    //    @JsonIgnore
    @ManyToOne
    @JsonProperty
    @JoinColumn(name = "module")
    private Module module;

    @JsonIgnore
    @ManyToOne
    @JsonProperty
    @JoinColumn(name = "courseplan")
    private TimeTable timeTable;

    /** A list of {@link Student Students} which have joined an timeslot. */
    @JsonIgnore
    @JsonProperty
    @ManyToMany(fetch = FetchType.LAZY)
    private List<Student> attendees;

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
     * Removes a student from the list of {@link #attendees} of an timeslot.
     *
     * @param student the {@link Student} which leaves an timeslot.
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

    private enum Type {VORLESUNG, PRAKTIKUM, UEBUNG}

}
