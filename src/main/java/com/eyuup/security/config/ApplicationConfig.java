package com.eyuup.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class ApplicationConfig {




        /*  how authentication provider works?
        *  user attempt to login so he'll enter the email(username) and password .
        * in services layer the login method : first of all we create authentication object with new UsernamePasswordauthenticationToken and we passe it to AuthenticationManger.authenticate .
        *  so authentication manager passe the authentication object into authenticationProvider containing the user credentials .
        * authenticationProvider verifies  the user credentiales  by applying the bean of UserDetailsService  to fetch the user from db and compare it with the user from request 
        * if the credentials are valid  it creates an Authentication object with the user’s details and granted authorities (roles/permissions).
        *
        */ 
        @Bean
        public AuthenticationProvider authenticationProvider (UserDetailsService userDetailsService){
            DaoAuthenticationProvider authProvider=new DaoAuthenticationProvider();
            authProvider.setUserDetailsService(userDetailsService);
            authProvider.setPasswordEncoder(passwordEncoder());
            return authProvider;
        }

        @Bean 
        public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception{
            return config.getAuthenticationManager();
        }

       @Bean 
       public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
       }
}
