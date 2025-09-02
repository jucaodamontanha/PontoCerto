package com.PontoCerto.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfiguration {

    private final JwtFilter jwtFilter;

    public SecurityConfiguration(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // 🔓 Endpoints do Swagger (liberados sem autenticação)
                        .requestMatchers(
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-resources/**",
                                "/webjars/**"
                        ).permitAll()

                        // 🔓 Endpoints públicos da aplicação
                        .requestMatchers("/usuarios/cadastrar", "/auth/**", "/empresas/cadastrar").permitAll()

                        // 🔒 Marcação de ponto
                        .requestMatchers("/ponto/marcar").hasAnyRole("FUNCIONARIO", "ADMIN", "GESTOR")
                        .requestMatchers("/ponto/admin/marcar").hasAnyRole("ADMIN", "GESTOR")

                        // 🔒 Visualização de pontos
                        .requestMatchers("/ponto/usuario/**").hasAnyRole("FUNCIONARIO", "ADMIN", "GESTOR")
                        .requestMatchers("/ponto/empresa").hasAnyRole("ADMIN", "GESTOR")

                        // 🔒 Banco de horas
                        .requestMatchers("/banco-horas/**").hasAnyRole("FUNCIONARIO", "ADMIN", "GESTOR")

                        // 🔒 Tudo o resto precisa estar autenticado
                        .anyRequest().authenticated()
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
