package de.hsrm.mi.swtp.exchangeplatform.model.rest_models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * User
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2019-12-04T10:25:24.919398+01:00[Europe/Berlin]")

public class User {
	@JsonProperty("userId")
	private Long userId;
	
	@JsonProperty("roles")
	@Valid
	private List<String> roles = null;
	
	public User userId(Long userId) {
		this.userId = userId;
		return this;
	}
	
	/**
	 * Get userId
	 *
	 * @return userId
	 */
	@ApiModelProperty(value = "")
	
	
	public Long getUserId() {
		return userId;
	}
	
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	public User roles(List<String> roles) {
		this.roles = roles;
		return this;
	}
	
	public User addRolesItem(String rolesItem) {
		if(this.roles == null) {
			this.roles = new ArrayList<>();
		}
		this.roles.add(rolesItem);
		return this;
	}
	
	/**
	 * Get roles
	 *
	 * @return roles
	 */
	@ApiModelProperty(value = "")
	
	
	public List<String> getRoles() {
		return roles;
	}
	
	public void setRoles(List<String> roles) {
		this.roles = roles;
	}
	
	
	@Override
	public boolean equals(java.lang.Object o) {
		if(this == o) {
			return true;
		}
		if(o == null || getClass() != o.getClass()) {
			return false;
		}
		User user = (User) o;
		return Objects.equals(this.userId, user.userId) && Objects.equals(this.roles, user.roles);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(userId, roles);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class User {\n");
		
		sb.append("    userId: ").append(toIndentedString(userId)).append("\n");
		sb.append("    roles: ").append(toIndentedString(roles)).append("\n");
		sb.append("}");
		return sb.toString();
	}
	
	/**
	 * Convert the given object to string with each line indented by 4 spaces
	 * (except the first line).
	 */
	private String toIndentedString(java.lang.Object o) {
		if(o == null) {
			return "null";
		}
		return o.toString().replace("\n", "\n    ");
	}
}

