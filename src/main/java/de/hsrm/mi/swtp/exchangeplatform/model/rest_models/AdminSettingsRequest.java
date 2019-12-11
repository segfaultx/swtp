package de.hsrm.mi.swtp.exchangeplatform.model.rest_models;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;

/**
 * AdminSettingsRequest
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2019-12-11T08:15:17.638366+01:00[Europe/Berlin]")

public class AdminSettingsRequest   {
  @JsonProperty("tradesActive")
  private Boolean tradesActive;

  @JsonProperty("activeFilters")
  @Valid
  private List<String> activeFilters = null;

  public AdminSettingsRequest tradesActive(Boolean tradesActive) {
    this.tradesActive = tradesActive;
    return this;
  }

  /**
   * Get tradesActive
   * @return tradesActive
  */
  @ApiModelProperty(value = "")


  public Boolean getTradesActive() {
    return tradesActive;
  }

  public void setTradesActive(Boolean tradesActive) {
    this.tradesActive = tradesActive;
  }

  public AdminSettingsRequest activeFilters(List<String> activeFilters) {
    this.activeFilters = activeFilters;
    return this;
  }

  public AdminSettingsRequest addActiveFiltersItem(String activeFiltersItem) {
    if (this.activeFilters == null) {
      this.activeFilters = new ArrayList<>();
    }
    this.activeFilters.add(activeFiltersItem);
    return this;
  }

  /**
   * Get activeFilters
   * @return activeFilters
  */
  @ApiModelProperty(value = "")


  public List<String> getActiveFilters() {
    return activeFilters;
  }

  public void setActiveFilters(List<String> activeFilters) {
    this.activeFilters = activeFilters;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AdminSettingsRequest adminSettingsRequest = (AdminSettingsRequest) o;
    return Objects.equals(this.tradesActive, adminSettingsRequest.tradesActive) &&
        Objects.equals(this.activeFilters, adminSettingsRequest.activeFilters);
  }

  @Override
  public int hashCode() {
    return Objects.hash(tradesActive, activeFilters);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AdminSettingsRequest {\n");
    
    sb.append("    tradesActive: ").append(toIndentedString(tradesActive)).append("\n");
    sb.append("    activeFilters: ").append(toIndentedString(activeFilters)).append("\n");
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

