package de.hsrm.mi.swtp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;

@Slf4j
@EnableJms
@SpringBootApplication
public class SwtpApplication {

    public static void main(String[] args) {
        SpringApplication.run(SwtpApplication.class, args);
    }

}
