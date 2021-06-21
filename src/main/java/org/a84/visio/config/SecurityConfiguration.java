package org.a84.visio.config;

import org.a84.visio.controller.handler.AccessDenialHandler;
import org.a84.visio.controller.handler.AuthenticationHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        super.configure(auth);
    }

    /**
     * Security config
     * @param http - http
     * @throws Exception - exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/sadmin/**").hasRole("SADMIN")
                .antMatchers("/manager").hasRole("MANAGER")
                .antMatchers("/", "/login", "/logout", "/bash").permitAll()
                .antMatchers("/resources/**").permitAll()
                .antMatchers("/*.css").permitAll()
                .and()
                .formLogin()
                    .loginPage("/login") // Change login page for a custom login page
                    .loginProcessingUrl("/login")
                    .successHandler(myAuthenticationSuccessHandler()) // Redirect successful logins (depending on the role)
                    .permitAll()
                .and()
                .exceptionHandling().accessDeniedPage("/accessDenied") // redirect user if they try to access unauthorized areas
                .accessDeniedHandler(accessDeniedHandler());
    }

    /**
     * AuthenticationSuccessHandler Bean.
     * @return bean
     */
    @Bean
    public AuthenticationSuccessHandler myAuthenticationSuccessHandler(){
        return new AuthenticationHandler();
    }

    /**
     * AccessDeniedHandler Bean.
     * @return bean
     */
    @Bean
    public AccessDeniedHandler accessDeniedHandler(){
        return new AccessDenialHandler();
    }

    /**
     * PasswordEncoder Bean.
     * @return bean
     */
    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
