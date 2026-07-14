package com.ServiGo.servigo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;
    private final CustomAuthenticationSuccessHandler customAuthSuccessHandler;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService,
                          CustomAuthenticationSuccessHandler customAuthSuccessHandler) {
        this.customUserDetailsService = customUserDetailsService;
        this.customAuthSuccessHandler = customAuthSuccessHandler;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new LegacyCompatiblePasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(customUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(authenticationProvider());
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/",
                                "/landing",
                                "/registro",
                                "/iniciar-sesion",
                                "/contactanos",
                                "/quienes-somos",
                                "/servicios",
                                "/servicios/**",
                                "/css/**",
                                "/CSS/**",
                                "/img/**",
                                "/images/**",
                                "/js/**",
                                "/webjars/**",
                                "/h2-console/**"
                        ).permitAll()
                        .requestMatchers("/perfil", "/cambiar-contrasena", "/mensajes/**", "/notificaciones/**", "/solicitudes/**", "/favoritos/**", "/calificaciones/**").authenticated()
                        .requestMatchers("/tecnico/**").hasRole("TECNICO")
                        .requestMatchers("/cliente/**").hasRole("CLIENTE")
                        .anyRequest().permitAll()
                )
                .formLogin(form -> form
                        .loginPage("/iniciar-sesion")
                        .loginProcessingUrl("/iniciar-sesion")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .successHandler(customAuthSuccessHandler)
                        .failureUrl("/iniciar-sesion?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .permitAll()
                )
                .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**"))
                .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()));

        return http.build();
    }

    
}
