package de.hsrm.mi.swtp.exchangeplatform.model.rest_models;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;

/**
 * Timetable
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2019-12-08T16:41:50.727112+01:00[Europe/Berlin]")

public class Timetable   {
  @JsonProperty("id")
  private Long id;

  @JsonProperty("end")
  private LocalDate end;

  @JsonProperty("start")
  private LocalDate start;

  @JsonProperty("timeslots")
  @Valid
  private List<Timeslot> timeslots = null;

  public Timetable id(Long id) {
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

  public Timetable end(LocalDate end) {
    this.end = end;
    return this;
  }

  /**
   * Get end
   * @return end
  */
  @ApiModelProperty(value = "")

  @Valid

  public LocalDate getEnd() {
    return end;
  }

  public void setEnd(LocalDate end) {
    this.end = end;
  }

  public Timetable start(LocalDate start) {
    this.start = start;
    return this;
  }

  /**
   * Get start
   * @return start
  */
  @ApiModelProperty(value = "")

  @Valid

  public LocalDate getStart() {
    return start;
  }

  public void setStart(LocalDate start) {
    this.start = start;
  }

  public Timetable timeslots(List<Timeslot> timeslots) {
    this.timeslots = timeslots;
    return this;
  }

  public Timetable addTimeslotsItem(Timeslot timeslotsItem) {
    if (this.timeslots == null) {
      this.timeslots = new ArrayList<>();
    }
    this.timeslots.add(timeslotsItem);
    return this;
  }

  /**
   * Get timeslots
   * @return timeslots
  */
  @ApiModelProperty(value = "")

  @Valid

  public List<Timeslot> getTimeslots() {
    return timeslots;
  }

  public void setTimeslots(List<Timeslot> timeslots) {
    this.timeslots = timeslots;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Timetable timetable = (Timetable) o;
    return Objects.equals(this.id, timetable.id) &&
        Objects.equals(this.end, timetable.end) &&
        Objects.equals(this.start, timetable.start) &&
        Objects.equals(this.timeslots, timetable.timeslots);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, end, start, timeslots);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Timetable {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    end: ").append(toIndentedString(end)).append("\n");
    sb.append("    start: ").append(toIndentedString(start)).append("\n");
    sb.append("    timeslots: ").append(toIndentedString(timeslots)).append("\n");
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

