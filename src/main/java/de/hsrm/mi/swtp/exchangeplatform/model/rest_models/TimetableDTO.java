package de.hsrm.mi.swtp.exchangeplatform.model.rest_models;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.openapitools.jackson.nullable.JsonNullable;
import javax.validation.Valid;

/**
 * TimetableDTO
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2019-12-11T08:15:17.638366+01:00[Europe/Berlin]")

public class TimetableDTO   {
  @JsonProperty("id")
  private Long id;

  @JsonProperty("end")
  private JsonNullable<LocalDate> end = JsonNullable.undefined();

  @JsonProperty("start")
  private JsonNullable<LocalDate> start = JsonNullable.undefined();

  @JsonProperty("timeslots")
  @Valid
  private List<TimeslotDTO> timeslots = null;

  public TimetableDTO id(Long id) {
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

  public TimetableDTO end(LocalDate end) {
    this.end = JsonNullable.of(end);
    return this;
  }

  /**
   * Get end
   * @return end
  */
  @ApiModelProperty(value = "")

  @Valid

  public JsonNullable<LocalDate> getEnd() {
    return end;
  }

  public void setEnd(JsonNullable<LocalDate> end) {
    this.end = end;
  }

  public TimetableDTO start(LocalDate start) {
    this.start = JsonNullable.of(start);
    return this;
  }

  /**
   * Get start
   * @return start
  */
  @ApiModelProperty(value = "")

  @Valid

  public JsonNullable<LocalDate> getStart() {
    return start;
  }

  public void setStart(JsonNullable<LocalDate> start) {
    this.start = start;
  }

  public TimetableDTO timeslots(List<TimeslotDTO> timeslots) {
    this.timeslots = timeslots;
    return this;
  }

  public TimetableDTO addTimeslotsItem(TimeslotDTO timeslotsItem) {
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

  public List<TimeslotDTO> getTimeslots() {
    return timeslots;
  }

  public void setTimeslots(List<TimeslotDTO> timeslots) {
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
    TimetableDTO timetableDTO = (TimetableDTO) o;
    return Objects.equals(this.id, timetableDTO.id) &&
        Objects.equals(this.end, timetableDTO.end) &&
        Objects.equals(this.start, timetableDTO.start) &&
        Objects.equals(this.timeslots, timetableDTO.timeslots);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, end, start, timeslots);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TimetableDTO {\n");
    
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

