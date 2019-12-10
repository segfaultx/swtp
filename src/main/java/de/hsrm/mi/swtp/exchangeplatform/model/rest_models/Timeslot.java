package de.hsrm.mi.swtp.exchangeplatform.model.rest_models;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;

/**
 * Timeslot
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2019-12-08T17:32:19.208350+01:00[Europe/Berlin]")

public class Timeslot   {
  @JsonProperty("id")
  private Long id;

  @JsonProperty("room")
  private Room room;

  @JsonProperty("day")
  private DayEnum day;

  @JsonProperty("time_start")
  private java.sql.Timestamp timeStart;

  @JsonProperty("time_end")
  private java.sql.Timestamp timeEnd;

  @JsonProperty("lecturer")
  private Lecturer lecturer;

  @JsonProperty("capacity")
  private Integer capacity;

  @JsonProperty("possibleTrades")
  private PossibleTradesResponse possibleTrades;

  @JsonProperty("attendees")
  private Integer attendees;

  @JsonProperty("module")
  private Module module;

  @JsonProperty("type")
  private TimeslotType type;

  public Timeslot id(Long id) {
    this.id = id;
    return this;
  }

  /**
   * Get id
   * @return id
  */
  @ApiModelProperty(value = "")


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Timeslot room(Room room) {
    this.room = room;
    return this;
  }

  /**
   * Get room
   * @return room
  */
  @ApiModelProperty(value = "")

  @Valid

  public Room getRoom() {
    return room;
  }

  public void setRoom(Room room) {
    this.room = room;
  }

  public Timeslot day(DayEnum day) {
    this.day = day;
    return this;
  }

  /**
   * Get day
   * @return day
  */
  @ApiModelProperty(value = "")

  @Valid

  public DayEnum getDay() {
    return day;
  }

  public void setDay(DayEnum day) {
    this.day = day;
  }

  public Timeslot timeStart(java.sql.Timestamp timeStart) {
    this.timeStart = timeStart;
    return this;
  }

  /**
   * Get timeStart
   * @return timeStart
  */
  @ApiModelProperty(value = "")

  @Valid

  public java.sql.Timestamp getTimeStart() {
    return timeStart;
  }

  public void setTimeStart(java.sql.Timestamp timeStart) {
    this.timeStart = timeStart;
  }

  public Timeslot timeEnd(java.sql.Timestamp timeEnd) {
    this.timeEnd = timeEnd;
    return this;
  }

  /**
   * Get timeEnd
   * @return timeEnd
  */
  @ApiModelProperty(value = "")

  @Valid

  public java.sql.Timestamp getTimeEnd() {
    return timeEnd;
  }

  public void setTimeEnd(java.sql.Timestamp timeEnd) {
    this.timeEnd = timeEnd;
  }

  public Timeslot lecturer(Lecturer lecturer) {
    this.lecturer = lecturer;
    return this;
  }

  /**
   * Get lecturer
   * @return lecturer
  */
  @ApiModelProperty(value = "")

  @Valid

  public Lecturer getLecturer() {
    return lecturer;
  }

  public void setLecturer(Lecturer lecturer) {
    this.lecturer = lecturer;
  }

  public Timeslot capacity(Integer capacity) {
    this.capacity = capacity;
    return this;
  }

  /**
   * Get capacity
   * @return capacity
  */
  @ApiModelProperty(value = "")


  public Integer getCapacity() {
    return capacity;
  }

  public void setCapacity(Integer capacity) {
    this.capacity = capacity;
  }

  public Timeslot possibleTrades(PossibleTradesResponse possibleTrades) {
    this.possibleTrades = possibleTrades;
    return this;
  }

  /**
   * Get possibleTrades
   * @return possibleTrades
  */
  @ApiModelProperty(value = "")

  @Valid

  public PossibleTradesResponse getPossibleTrades() {
    return possibleTrades;
  }

  public void setPossibleTrades(PossibleTradesResponse possibleTrades) {
    this.possibleTrades = possibleTrades;
  }

  public Timeslot attendees(Integer attendees) {
    this.attendees = attendees;
    return this;
  }

  /**
   * Get attendees
   * @return attendees
  */
  @ApiModelProperty(value = "")


  public Integer getAttendees() {
    return attendees;
  }

  public void setAttendees(Integer attendees) {
    this.attendees = attendees;
  }

  public Timeslot module(Module module) {
    this.module = module;
    return this;
  }

  /**
   * Get module
   * @return module
  */
  @ApiModelProperty(value = "")

  @Valid

  public Module getModule() {
    return module;
  }

  public void setModule(Module module) {
    this.module = module;
  }

  public Timeslot type(TimeslotType type) {
    this.type = type;
    return this;
  }

  /**
   * Get type
   * @return type
  */
  @ApiModelProperty(value = "")

  @Valid

  public TimeslotType getType() {
    return type;
  }

  public void setType(TimeslotType type) {
    this.type = type;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Timeslot timeslot = (Timeslot) o;
    return Objects.equals(this.id, timeslot.id) &&
        Objects.equals(this.room, timeslot.room) &&
        Objects.equals(this.day, timeslot.day) &&
        Objects.equals(this.timeStart, timeslot.timeStart) &&
        Objects.equals(this.timeEnd, timeslot.timeEnd) &&
        Objects.equals(this.lecturer, timeslot.lecturer) &&
        Objects.equals(this.capacity, timeslot.capacity) &&
        Objects.equals(this.possibleTrades, timeslot.possibleTrades) &&
        Objects.equals(this.attendees, timeslot.attendees) &&
        Objects.equals(this.module, timeslot.module) &&
        Objects.equals(this.type, timeslot.type);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, room, day, timeStart, timeEnd, lecturer, capacity, possibleTrades, attendees, module, type);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Timeslot {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    room: ").append(toIndentedString(room)).append("\n");
    sb.append("    day: ").append(toIndentedString(day)).append("\n");
    sb.append("    timeStart: ").append(toIndentedString(timeStart)).append("\n");
    sb.append("    timeEnd: ").append(toIndentedString(timeEnd)).append("\n");
    sb.append("    lecturer: ").append(toIndentedString(lecturer)).append("\n");
    sb.append("    capacity: ").append(toIndentedString(capacity)).append("\n");
    sb.append("    possibleTrades: ").append(toIndentedString(possibleTrades)).append("\n");
    sb.append("    attendees: ").append(toIndentedString(attendees)).append("\n");
    sb.append("    module: ").append(toIndentedString(module)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

