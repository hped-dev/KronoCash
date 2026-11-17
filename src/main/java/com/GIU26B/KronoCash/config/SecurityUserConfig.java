package com.GIU26B.KronoCash.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

/**
 * Usuario en memoria para poder acceder a las rutas MVC protegidas
 * sin configurar un repositorio de usuarios completo.
 *
 * Usuario por defecto: kronocash / krono123
 */
@Configuration
public class SecurityUserConfig {

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.builder()
                .username("kronocash")
                .password("{noop}krono123")
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(user);
    }
}
