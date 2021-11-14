package com.apus.drones.apusdronesbackend.config;

import com.apus.drones.apusdronesbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        //Para desabilitar a autenticação, comentar até o código até o ...
        http.cors().and().csrf().disable().authorizeRequests()
                .antMatchers("/api/v1/authenticate/login", "/h2/**", "/csrf", "/v2/api-docs",
                        "/v3/api-docs", "/configuration/ui", "/configuration/security", "/swagger-resources",
                        "/swagger-resources/configuration/**", "/swagger-ui.html",
                        "/swagger-ui/*", "/webjars/springfox-swagger-ui/**")
                .permitAll().anyRequest().authenticated().and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);


        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        http.headers().frameOptions().sameOrigin();
        //... (comentar até aqui)


        //Para desabilitar a autenticação, tirar o comentário da linha abaixo
        //http.authorizeRequests().anyRequest().permitAll();
    }
}