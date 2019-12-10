package de.hsrm.mi.swtp.exchangeplatform.model.rest_models;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

/**
 * TradeOffer
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2019-12-08T16:41:50.727112+01:00[Europe/Berlin]")

public class TradeOffer   {
  @JsonProperty("offeredTimeslotId")
  private Long offeredTimeslotId;

  @JsonProperty("wantedTimeslotId")
  private Long wantedTimeslotId;

  @JsonProperty("id")
  private Integer id;

  @JsonProperty("instant")
  private Boolean instant;

  @JsonProperty("collides")
  private Boolean collides;

  public TradeOffer offeredTimeslotId(Long offeredTimeslotId) {
    this.offeredTimeslotId = offeredTimeslotId;
    return this;
  }

  /**
   * Get offeredTimeslotId
   * @return offeredTimeslotId
  */
  @ApiModelProperty(value = "")


  public Long getOfferedTimeslotId() {
    return offeredTimeslotId;
  }

  public void setOfferedTimeslotId(Long offeredTimeslotId) {
    this.offeredTimeslotId = offeredTimeslotId;
  }

  public TradeOffer wantedTimeslotId(Long wantedTimeslotId) {
    this.wantedTimeslotId = wantedTimeslotId;
    return this;
  }

  /**
   * Get wantedTimeslotId
   * @return wantedTimeslotId
  */
  @ApiModelProperty(value = "")


  public Long getWantedTimeslotId() {
    return wantedTimeslotId;
  }

  public void setWantedTimeslotId(Long wantedTimeslotId) {
    this.wantedTimeslotId = wantedTimeslotId;
  }

  public TradeOffer id(Integer id) {
    this.id = id;
    return this;
  }

  /**
   * Get id
   * @return id
  */
  @ApiModelProperty(value = "")


  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public TradeOffer instant(Boolean instant) {
    this.instant = instant;
    return this;
  }

  /**
   * Get instant
   * @return instant
  */
  @ApiModelProperty(value = "")


  public Boolean getInstant() {
    return instant;
  }

  public void setInstant(Boolean instant) {
    this.instant = instant;
  }

  public TradeOffer collides(Boolean collides) {
    this.collides = collides;
    return this;
  }

  /**
   * Get collides
   * @return collides
  */
  @ApiModelProperty(value = "")


  public Boolean getCollides() {
    return collides;
  }

  public void setCollides(Boolean collides) {
    this.collides = collides;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TradeOffer tradeOffer = (TradeOffer) o;
    return Objects.equals(this.offeredTimeslotId, tradeOffer.offeredTimeslotId) &&
        Objects.equals(this.wantedTimeslotId, tradeOffer.wantedTimeslotId) &&
        Objects.equals(this.id, tradeOffer.id) &&
        Objects.equals(this.instant, tradeOffer.instant) &&
        Objects.equals(this.collides, tradeOffer.collides);
  }

  @Override
  public int hashCode() {
    return Objects.hash(offeredTimeslotId, wantedTimeslotId, id, instant, collides);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TradeOffer {\n");
    
    sb.append("    offeredTimeslotId: ").append(toIndentedString(offeredTimeslotId)).append("\n");
    sb.append("    wantedTimeslotId: ").append(toIndentedString(wantedTimeslotId)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    instant: ").append(toIndentedString(instant)).append("\n");
    sb.append("    collides: ").append(toIndentedString(collides)).append("\n");
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

