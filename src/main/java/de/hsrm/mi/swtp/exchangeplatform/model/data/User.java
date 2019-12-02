package de.hsrm.mi.swtp.exchangeplatform.model.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import javax.persistence.*;


@Entity
@Data
@Table(name="Users")
public class User implements Model {
        @Id
        @JsonProperty(value = "matriculationNumber", required = true)
        @Column(name = "matr_nr", unique = true, updatable = false, insertable = false)
        private Long matriculationNumber; //student{matrikelnummer = id}

        @Column(name="username", nullable = false, unique = true)
        private String username;

        @Column(name="password")
        private String password;

        @Column(name="role")
        private String role;


}