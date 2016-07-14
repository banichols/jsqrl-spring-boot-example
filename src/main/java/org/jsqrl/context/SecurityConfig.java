package org.jsqrl.context;

import org.jsqrl.security.SqrlAuthenticationProvider;
import org.jsqrl.service.SqrlAuthenticationService;
import org.jsqrl.service.SqrlUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Created by Brent Nichols on 7/10/2016.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private SqrlAuthenticationService authenticationService;

    @Autowired
    private SqrlUserService sqrlUserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/",
                        "/home",
                        "/sqrl",
                        "/js/*",
                        "/authcheck",
                        "/sqrl-login").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .permitAll()
                .and()
                .logout()
                .logoutSuccessUrl("/home")
                .permitAll();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .authenticationProvider(new SqrlAuthenticationProvider(authenticationService, sqrlUserService));
    }

}
