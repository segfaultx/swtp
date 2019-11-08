package de.hsrm.mi.swtp.exchangeplatform.configuration;

import de.hsrm.mi.swtp.exchangeplatform.controller.StudentRestController;
import de.hsrm.mi.swtp.exchangeplatform.model.StudentAccessorImpl;
import de.hsrm.mi.swtp.exchangeplatform.model.StudentFactory;
import de.hsrm.mi.swtp.exchangeplatform.service.StudentService;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@AutoConfigureBefore
@Configuration
public class AppConfig {

    @Bean
    public StudentAccessorImpl studentAccessorImpl(final StudentService studentService) {
        return StudentAccessorImpl.builder()
                .studentService(studentService)
                .build();
    }

    @Bean
    public StudentRestController studentRestController(final StudentAccessorImpl studentAccessorImpl) {
        return StudentRestController.builder()
                .studentAccessor(studentAccessorImpl)
                .build();
    }

    @Bean
    public StudentFactory studentFactory() {
        return StudentFactory.builder().build();
    }

    @Bean
    public StudentService studentService(final StudentFactory studentFactory) {
        return StudentService.builder()
                .studentFactory(studentFactory)
                .build();
    }

}
