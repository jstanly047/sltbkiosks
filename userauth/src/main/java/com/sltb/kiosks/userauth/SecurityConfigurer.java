package com.sltb.kiosks.userauth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@ComponentScan(basePackages = "com.sltb.kioskslib.library")
@EntityScan("com.sltb.kioskslib.library.model")
public class SecurityConfigurer extends WebSecurityConfigurerAdapter {
    @Autowired
    private SLTBUserDetailsService slabUserDetailsService;
    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    public SecurityConfigurer(SLTBUserDetailsService slabUserDetailsService){
        this.slabUserDetailsService = slabUserDetailsService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder authMangerBuilder) throws Exception{
        authMangerBuilder.userDetailsService(slabUserDetailsService);
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception{
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/userauth")
                .permitAll().anyRequest().authenticated()
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
