package com.GIU26B.KronoCash.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Configuración de rutas públicas y protegidas
            .authorizeHttpRequests(auth -> auth
                // Permitir acceso libre a recursos estáticos (CSS, JS, imágenes) y páginas públicas
                .requestMatchers("/", "/css/**", "/js/**", "/img/**", "/register", "/login", "/error").permitAll()
                // Cualquier otra vista (dashboard, simulador, ajustes) requiere autenticación
                .anyRequest().authenticated()
            )
            // Configuración del formulario de inicio de sesión
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/dashboard", true)
                .permitAll()
            )
            // Configuración de cierre de sesión
            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            )
            // Criterio de Accesibilidad (WCAG 2.2 - Accessible Authentication):
            // NO BLOQUEAR extensiones de navegador ni gestores de contraseñas (permite autocompletado)
            .csrf(csrf -> csrf.ignoringRequestMatchers("/api/**")); // Si usas endpoints REST asíncronos para la mascota o simulador

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Encriptador robusto de contraseñas compatible con Spring Security 6 / Java 25
        return new BCryptPasswordEncoder();
    }
}