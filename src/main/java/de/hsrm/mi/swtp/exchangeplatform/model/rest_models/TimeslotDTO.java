package de.hsrm.mi.swtp.exchangeplatform.model.rest_models;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import java.time.OffsetDateTime;

import org.openapitools.jackson.nullable.JsonNullable;
import javax.validation.Valid;

/**
 * TimeslotDTO
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2019-12-11T08:15:17.638366+01:00[Europe/Berlin]")

public class TimeslotDTO   {
  @JsonProperty("id")
  private Long id;

  @JsonProperty("room")
  private RoomDTO room;

  @JsonProperty("day")
  private DayEnum day;

  @JsonProperty("time_start")
  private OffsetDateTime timeStart;

  @JsonProperty("time_end")
  private OffsetDateTime timeEnd;

  @JsonProperty("lecturer")
  private LecturerDTO lecturer;

  @JsonProperty("capacity")
  private JsonNullable<Integer> capacity = JsonNullable.undefined();

  @JsonProperty("possibleTrades")
  private PossibleTradesResponse possibleTrades;

  @JsonProperty("attendees")
  private JsonNullable<Integer> attendees = JsonNullable.undefined();

  @JsonProperty("module")
  private ModuleDTO module;

  @JsonProperty("type")
  private TimeslotType type;

  public TimeslotDTO id(Long id) {
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

  public TimeslotDTO room(RoomDTO room) {
    this.room = room;
    return this;
  }

  /**
   * Get room
   * @return room
  */
  @ApiModelProperty(value = "")

  @Valid

  public RoomDTO getRoom() {
    return room;
  }

  public void setRoom(RoomDTO room) {
    this.room = room;
  }

  public TimeslotDTO day(DayEnum day) {
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

  public TimeslotDTO timeStart(OffsetDateTime timeStart) {
    this.timeStart = timeStart;
    return this;
  }

  /**
   * Get timeStart
   * @return timeStart
  */
  @ApiModelProperty(value = "")

  @Valid

  public OffsetDateTime getTimeStart() {
    return timeStart;
  }

  public void setTimeStart(OffsetDateTime timeStart) {
    this.timeStart = timeStart;
  }

  public TimeslotDTO timeEnd(OffsetDateTime timeEnd) {
    this.timeEnd = timeEnd;
    return this;
  }

  /**
   * Get timeEnd
   * @return timeEnd
  */
  @ApiModelProperty(value = "")

  @Valid

  public OffsetDateTime getTimeEnd() {
    return timeEnd;
  }

  public void setTimeEnd(OffsetDateTime timeEnd) {
    this.timeEnd = timeEnd;
  }

  public TimeslotDTO lecturer(LecturerDTO lecturer) {
    this.lecturer = lecturer;
    return this;
  }

  /**
   * Get lecturer
   * @return lecturer
  */
  @ApiModelProperty(value = "")

  @Valid

  public LecturerDTO getLecturer() {
    return lecturer;
  }

  public void setLecturer(LecturerDTO lecturer) {
    this.lecturer = lecturer;
  }

  public TimeslotDTO capacity(Integer capacity) {
    this.capacity = JsonNullable.of(capacity);
    return this;
  }

  /**
   * Get capacity
   * @return capacity
  */
  @ApiModelProperty(value = "")


  public JsonNullable<Integer> getCapacity() {
    return capacity;
  }

  public void setCapacity(JsonNullable<Integer> capacity) {
    this.capacity = capacity;
  }

  public TimeslotDTO possibleTrades(PossibleTradesResponse possibleTrades) {
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

  public TimeslotDTO attendees(Integer attendees) {
    this.attendees = JsonNullable.of(attendees);
    return this;
  }

  /**
   * Get attendees
   * @return attendees
  */
  @ApiModelProperty(value = "")


  public JsonNullable<Integer> getAttendees() {
    return attendees;
  }

  public void setAttendees(JsonNullable<Integer> attendees) {
    this.attendees = attendees;
  }

  public TimeslotDTO module(ModuleDTO module) {
    this.module = module;
    return this;
  }

  /**
   * Get module
   * @return module
  */
  @ApiModelProperty(value = "")

  @Valid

  public ModuleDTO getModule() {
    return module;
  }

  public void setModule(ModuleDTO module) {
    this.module = module;
  }

  public TimeslotDTO type(TimeslotType type) {
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
    TimeslotDTO timeslotDTO = (TimeslotDTO) o;
    return Objects.equals(this.id, timeslotDTO.id) &&
        Objects.equals(this.room, timeslotDTO.room) &&
        Objects.equals(this.day, timeslotDTO.day) &&
        Objects.equals(this.timeStart, timeslotDTO.timeStart) &&
        Objects.equals(this.timeEnd, timeslotDTO.timeEnd) &&
        Objects.equals(this.lecturer, timeslotDTO.lecturer) &&
        Objects.equals(this.capacity, timeslotDTO.capacity) &&
        Objects.equals(this.possibleTrades, timeslotDTO.possibleTrades) &&
        Objects.equals(this.attendees, timeslotDTO.attendees) &&
        Objects.equals(this.module, timeslotDTO.module) &&
        Objects.equals(this.type, timeslotDTO.type);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, room, day, timeStart, timeEnd, lecturer, capacity, possibleTrades, attendees, module, type);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TimeslotDTO {\n");
    
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

