package de.hsrm.mi.swtp;

import de.hsrm.mi.swtp.exchangeplatform.configuration.AppConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@Import({
        AppConfig.class
})
@SpringBootApplication
public class SwtpApplication {

    public static void main(String[] args) {
        SpringApplication.run(SwtpApplication.class, args);
    }

}
