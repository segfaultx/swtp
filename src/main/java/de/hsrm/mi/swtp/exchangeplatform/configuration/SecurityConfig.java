package de.hsrm.mi.swtp.exchangeplatform.configuration;

import de.hsrm.mi.swtp.exchangeplatform.model.data.Student;
import de.hsrm.mi.swtp.exchangeplatform.service.rest.UserService;
import org.json.HTTP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired UserService userService;

    @Bean PasswordEncoder getPasswordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder authmanagerbuilder) throws Exception {

        authmanagerbuilder
                .userDetailsService(userService)
                .passwordEncoder(getPasswordEncoder());

        PasswordEncoder pwenc = getPasswordEncoder();

        authmanagerbuilder.jdbcAuthentication()
                .withUser("Student")
                .password(pwenc.encode("geheim"))
                .roles("STUDENT")
        .and()
                .withUser("admin")
                .password(pwenc.encode("admin"))
                .roles("STUDENT", "ADMIN");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("auth/admin/*").hasRole("ADMIN")
                .antMatchers("/auth/*").hasAnyRole("ADMIN", "STUDENT")
        .and()
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/usersite")
                .permitAll()
        .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .permitAll();
    }
}

