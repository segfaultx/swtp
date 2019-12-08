package de.hsrm.mi.swtp.exchangeplatform.model.rest_models;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;

/**
 * PossibleTradesResponse
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2019-12-08T16:41:50.727112+01:00[Europe/Berlin]")

public class PossibleTradesResponse   {
  @JsonProperty("instant")
  @Valid
  private List<Timeslot> instant = null;

  @JsonProperty("tradesAvailable")
  @Valid
  private List<Timeslot> tradesAvailable = null;

  @JsonProperty("remaining")
  @Valid
  private List<Timeslot> remaining = null;

  public PossibleTradesResponse instant(List<Timeslot> instant) {
    this.instant = instant;
    return this;
  }

  public PossibleTradesResponse addInstantItem(Timeslot instantItem) {
    if (this.instant == null) {
      this.instant = new ArrayList<>();
    }
    this.instant.add(instantItem);
    return this;
  }

  /**
   * Get instant
   * @return instant
  */
  @ApiModelProperty(value = "")

  @Valid

  public List<Timeslot> getInstant() {
    return instant;
  }

  public void setInstant(List<Timeslot> instant) {
    this.instant = instant;
  }

  public PossibleTradesResponse tradesAvailable(List<Timeslot> tradesAvailable) {
    this.tradesAvailable = tradesAvailable;
    return this;
  }

  public PossibleTradesResponse addTradesAvailableItem(Timeslot tradesAvailableItem) {
    if (this.tradesAvailable == null) {
      this.tradesAvailable = new ArrayList<>();
    }
    this.tradesAvailable.add(tradesAvailableItem);
    return this;
  }

  /**
   * Get tradesAvailable
   * @return tradesAvailable
  */
  @ApiModelProperty(value = "")

  @Valid

  public List<Timeslot> getTradesAvailable() {
    return tradesAvailable;
  }

  public void setTradesAvailable(List<Timeslot> tradesAvailable) {
    this.tradesAvailable = tradesAvailable;
  }

  public PossibleTradesResponse remaining(List<Timeslot> remaining) {
    this.remaining = remaining;
    return this;
  }

  public PossibleTradesResponse addRemainingItem(Timeslot remainingItem) {
    if (this.remaining == null) {
      this.remaining = new ArrayList<>();
    }
    this.remaining.add(remainingItem);
    return this;
  }

  /**
   * Get remaining
   * @return remaining
  */
  @ApiModelProperty(value = "")

  @Valid

  public List<Timeslot> getRemaining() {
    return remaining;
  }

  public void setRemaining(List<Timeslot> remaining) {
    this.remaining = remaining;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PossibleTradesResponse possibleTradesResponse = (PossibleTradesResponse) o;
    return Objects.equals(this.instant, possibleTradesResponse.instant) &&
        Objects.equals(this.tradesAvailable, possibleTradesResponse.tradesAvailable) &&
        Objects.equals(this.remaining, possibleTradesResponse.remaining);
  }

  @Override
  public int hashCode() {
    return Objects.hash(instant, tradesAvailable, remaining);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PossibleTradesResponse {\n");
    
    sb.append("    instant: ").append(toIndentedString(instant)).append("\n");
    sb.append("    tradesAvailable: ").append(toIndentedString(tradesAvailable)).append("\n");
    sb.append("    remaining: ").append(toIndentedString(remaining)).append("\n");
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

