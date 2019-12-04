package de.hsrm.mi.swtp.exchangeplatform.model.rest_models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Student
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2019-12-04T10:25:24.919398+01:00[Europe/Berlin]")

public class Student   {
  @JsonProperty("student_id")
  private Long matriculationNumber;

  @JsonProperty("username")
  private String username;

  @JsonProperty("timeslots")
  @Valid
  private List<Timeslot> timeslots = null;

  public Student matriculationNumber(Long matriculationNumber) {
    this.matriculationNumber = matriculationNumber;
    return this;
  }

  /**
   * Get matriculationNumber
   * @return matriculationNumber
  */
  @ApiModelProperty(value = "")


  public Long getMatriculationNumber() {
    return matriculationNumber;
  }

  public void setMatriculationNumber(Long matriculationNumber) {
    this.matriculationNumber = matriculationNumber;
  }

  public Student username(String username) {
    this.username = username;
    return this;
  }

  /**
   * Get username
   * @return username
  */
  @ApiModelProperty(value = "")


  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public Student timeslots(List<Timeslot> timeslots) {
    this.timeslots = timeslots;
    return this;
  }

  public Student addTimeslotsItem(Timeslot timeslotsItem) {
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
    Student student = (Student) o;
    return Objects.equals(this.matriculationNumber, student.matriculationNumber) &&
        Objects.equals(this.username, student.username) &&
        Objects.equals(this.timeslots, student.timeslots);
  }

  @Override
  public int hashCode() {
    return Objects.hash(matriculationNumber, username, timeslots);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Student {\n");
    
    sb.append("    matriculationNumber: ").append(toIndentedString(matriculationNumber)).append("\n");
    sb.append("    username: ").append(toIndentedString(username)).append("\n");
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

