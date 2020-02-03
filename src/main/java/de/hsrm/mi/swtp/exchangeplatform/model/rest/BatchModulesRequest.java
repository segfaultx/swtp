package de.hsrm.mi.swtp.exchangeplatform.model.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

/**
 * A simple container class.
 * Mainly used to prevent many calls when requesting all modules of a client's timetable. So the rest controller can receive
 * a batch of ids and work with those in a single request.
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BatchModulesRequest {
	@JsonProperty(value = "ids", required = true)
	@ArraySchema(minItems = 0, uniqueItems = true, schema = @Schema(type = "integer", format = "int64"))
	List<Long> modulesIDs;
}
