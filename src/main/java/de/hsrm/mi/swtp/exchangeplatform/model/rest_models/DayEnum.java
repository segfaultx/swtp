package de.hsrm.mi.swtp.exchangeplatform.model.rest_models;

import com.fasterxml.jackson.annotation.JsonValue;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Gets or Sets DayEnum
 */
public enum DayEnum {
  
  MONDAY("monday"),
  
  TUESDAY("tuesday"),
  
  WEDNESDAY("wednesday"),
  
  THURSDAY("thursday"),
  
  FRIDAY("friday");

  private String value;

  DayEnum(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static DayEnum fromValue(String value) {
    for (DayEnum b : DayEnum.values()) {
      if (b.value.equals(value)) {
        return b;
      }
    }
    throw new IllegalArgumentException("Unexpected value '" + value + "'");
  }
}

