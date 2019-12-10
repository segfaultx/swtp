package de.hsrm.mi.swtp.exchangeplatform.model.rest_models;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import org.openapitools.jackson.nullable.JsonNullable;

/**
 * TradeRequest
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2019-12-08T16:41:50.727112+01:00[Europe/Berlin]")

public class TradeRequest   {
  @JsonProperty("offeredTimeslotId")
  private JsonNullable<Long> offeredTimeslotId = JsonNullable.undefined();

  @JsonProperty("wantedTimeslotId")
  private JsonNullable<Long> wantedTimeslotId = JsonNullable.undefined();

  @JsonProperty("offeredByStudentMatriculationNumber")
  private Long offeredByStudentMatriculationNumber;

  @JsonProperty("forced")
  private Boolean forced;

  @JsonProperty("adminId")
  private JsonNullable<Long> adminId = JsonNullable.undefined();

  public TradeRequest offeredTimeslotId(Long offeredTimeslotId) {
    this.offeredTimeslotId = JsonNullable.of(offeredTimeslotId);
    return this;
  }

  /**
   * Get offeredTimeslotId
   * @return offeredTimeslotId
  */
  @ApiModelProperty(value = "")


  public JsonNullable<Long> getOfferedTimeslotId() {
    return offeredTimeslotId;
  }

  public void setOfferedTimeslotId(JsonNullable<Long> offeredTimeslotId) {
    this.offeredTimeslotId = offeredTimeslotId;
  }

  public TradeRequest wantedTimeslotId(Long wantedTimeslotId) {
    this.wantedTimeslotId = JsonNullable.of(wantedTimeslotId);
    return this;
  }

  /**
   * Get wantedTimeslotId
   * @return wantedTimeslotId
  */
  @ApiModelProperty(value = "")


  public JsonNullable<Long> getWantedTimeslotId() {
    return wantedTimeslotId;
  }

  public void setWantedTimeslotId(JsonNullable<Long> wantedTimeslotId) {
    this.wantedTimeslotId = wantedTimeslotId;
  }

  public TradeRequest offeredByStudentMatriculationNumber(Long offeredByStudentMatriculationNumber) {
    this.offeredByStudentMatriculationNumber = offeredByStudentMatriculationNumber;
    return this;
  }

  /**
   * Get offeredByStudentMatriculationNumber
   * @return offeredByStudentMatriculationNumber
  */
  @ApiModelProperty(value = "")


  public Long getOfferedByStudentMatriculationNumber() {
    return offeredByStudentMatriculationNumber;
  }

  public void setOfferedByStudentMatriculationNumber(Long offeredByStudentMatriculationNumber) {
    this.offeredByStudentMatriculationNumber = offeredByStudentMatriculationNumber;
  }

  public TradeRequest forced(Boolean forced) {
    this.forced = forced;
    return this;
  }

  /**
   * Get forced
   * @return forced
  */
  @ApiModelProperty(value = "")


  public Boolean getForced() {
    return forced;
  }

  public void setForced(Boolean forced) {
    this.forced = forced;
  }

  public TradeRequest adminId(Long adminId) {
    this.adminId = JsonNullable.of(adminId);
    return this;
  }

  /**
   * Get adminId
   * @return adminId
  */
  @ApiModelProperty(value = "")


  public JsonNullable<Long> getAdminId() {
    return adminId;
  }

  public void setAdminId(JsonNullable<Long> adminId) {
    this.adminId = adminId;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TradeRequest tradeRequest = (TradeRequest) o;
    return Objects.equals(this.offeredTimeslotId, tradeRequest.offeredTimeslotId) &&
        Objects.equals(this.wantedTimeslotId, tradeRequest.wantedTimeslotId) &&
        Objects.equals(this.offeredByStudentMatriculationNumber, tradeRequest.offeredByStudentMatriculationNumber) &&
        Objects.equals(this.forced, tradeRequest.forced) &&
        Objects.equals(this.adminId, tradeRequest.adminId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(offeredTimeslotId, wantedTimeslotId, offeredByStudentMatriculationNumber, forced, adminId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TradeRequest {\n");
    
    sb.append("    offeredTimeslotId: ").append(toIndentedString(offeredTimeslotId)).append("\n");
    sb.append("    wantedTimeslotId: ").append(toIndentedString(wantedTimeslotId)).append("\n");
    sb.append("    offeredByStudentMatriculationNumber: ").append(toIndentedString(offeredByStudentMatriculationNumber)).append("\n");
    sb.append("    forced: ").append(toIndentedString(forced)).append("\n");
    sb.append("    adminId: ").append(toIndentedString(adminId)).append("\n");
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

