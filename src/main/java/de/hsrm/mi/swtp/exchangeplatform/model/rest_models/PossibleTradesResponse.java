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
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2019-12-08T17:46:28.627314+01:00[Europe/Berlin]")

public class PossibleTradesResponse   {
  @JsonProperty("instant")
  @Valid
  private List<Long> instant = null;

  @JsonProperty("tradesAvailable")
  @Valid
  private List<Long> tradesAvailable = null;

  @JsonProperty("remaining")
  @Valid
  private List<Long> remaining = null;

  public PossibleTradesResponse instant(List<Long> instant) {
    this.instant = instant;
    return this;
  }

  public PossibleTradesResponse addInstantItem(Long instantItem) {
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


  public List<Long> getInstant() {
    return instant;
  }

  public void setInstant(List<Long> instant) {
    this.instant = instant;
  }

  public PossibleTradesResponse tradesAvailable(List<Long> tradesAvailable) {
    this.tradesAvailable = tradesAvailable;
    return this;
  }

  public PossibleTradesResponse addTradesAvailableItem(Long tradesAvailableItem) {
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


  public List<Long> getTradesAvailable() {
    return tradesAvailable;
  }

  public void setTradesAvailable(List<Long> tradesAvailable) {
    this.tradesAvailable = tradesAvailable;
  }

  public PossibleTradesResponse remaining(List<Long> remaining) {
    this.remaining = remaining;
    return this;
  }

  public PossibleTradesResponse addRemainingItem(Long remainingItem) {
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


  public List<Long> getRemaining() {
    return remaining;
  }

  public void setRemaining(List<Long> remaining) {
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

