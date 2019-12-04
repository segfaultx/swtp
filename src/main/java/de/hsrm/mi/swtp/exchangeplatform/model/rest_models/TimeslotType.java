package de.hsrm.mi.swtp.exchangeplatform.model.rest_models;

import com.fasterxml.jackson.annotation.JsonValue;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Gets or Sets TimeslotType
 */
public enum TimeslotType {
  
  LECTURE("Lecture"),
  
  EXERCISE("Exercise"),
  
  PRACTICALTRAINING("PracticalTraining");

  private String value;

  TimeslotType(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static TimeslotType fromValue(String value) {
    for (TimeslotType b : TimeslotType.values()) {
      if (b.value.equals(value)) {
        return b;
      }
    }
    throw new IllegalArgumentException("Unexpected value '" + value + "'");
  }
}

