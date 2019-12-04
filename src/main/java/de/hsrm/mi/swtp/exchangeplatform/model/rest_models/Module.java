package de.hsrm.mi.swtp.exchangeplatform.model.rest_models;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;

/**
 * Module
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2019-12-04T10:25:24.919398+01:00[Europe/Berlin]")

public class Module   {
  @JsonProperty("id")
  private Long id;

  @JsonProperty("name")
  private String name;

  @JsonProperty("timeslots")
  @Valid
  private List<Timeslot> timeslots = null;

  public Module id(Long id) {
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

  public Module name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
  */
  @ApiModelProperty(value = "")


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Module timeslots(List<Timeslot> timeslots) {
    this.timeslots = timeslots;
    return this;
  }

  public Module addTimeslotsItem(Timeslot timeslotsItem) {
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
    Module module = (Module) o;
    return Objects.equals(this.id, module.id) &&
        Objects.equals(this.name, module.name) &&
        Objects.equals(this.timeslots, module.timeslots);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, timeslots);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Module {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
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

