package de.hsrm.mi.swtp.exchangeplatform.model.data;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import javax.persistence.*;


@Entity
@Data
//@Table(name="Users")
public class User implements Model {
        @Id
        @JsonProperty(value = "matriculationNumber", required = true)
        @Column(unique = true, updatable = false, insertable = false)
        private Long matriculationNumber; //student{matrikelnummer = id}

        @JsonProperty(value="username")
        @Column(nullable = false, unique = true)
        //@Column(name="username" ,nullable = false, unique = true)
        private String username;

        @JsonProperty(value = "password")
        //@Column(name = "password")
        private String password;

        @JsonProperty(value= "role")
        //@Column(name="role")
        private String role;


}