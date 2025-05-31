package com.bemtivi.bemtivi.configuration.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final FilterToken filterToken;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .cors(withDefaults())
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.DELETE, "/servicos/*/deletar", "/servicos/*/desativar").hasRole("ADMINISTRATOR")
                        .requestMatchers(HttpMethod.PUT, "/servicos/*/atualizar").hasRole("ADMINISTRATOR")
                        .requestMatchers(HttpMethod.POST, "/servicos/inserir").hasRole("ADMINISTRATOR")
                        .requestMatchers(HttpMethod.GET, "/servicos/*/buscar", "/servicos/paginacao").permitAll()

                        .requestMatchers(HttpMethod.DELETE, "/produtos/*/deletar", "/produtos/*/desativar").hasRole("ADMINISTRATOR")
                        .requestMatchers(HttpMethod.PUT, "/produtos/*/atualizar").hasRole("ADMINISTRATOR")
                        .requestMatchers(HttpMethod.POST, "/produtos/inserir").hasRole("ADMINISTRATOR")
                        .requestMatchers(HttpMethod.GET, "/produtos/*/buscar", "/produtos/paginacao").permitAll()

                        .requestMatchers(HttpMethod.DELETE, "/categorias/*/deletar", "/categorias/*/desativar").hasRole("ADMINISTRATOR")
                        .requestMatchers(HttpMethod.PUT, "/categorias/*/atualizar").hasRole("ADMINISTRATOR")
                        .requestMatchers(HttpMethod.POST, "/categorias/inserir").hasRole("ADMINISTRATOR")
                        .requestMatchers(HttpMethod.GET, "/categorias/*/buscar", "/categorias/buscartodos", "/categorias/paginacao").permitAll()


                        .requestMatchers(HttpMethod.DELETE, "/clientes/*/deletar", "/clientes/*/desativar").hasRole("CLIENT")
                        .requestMatchers(HttpMethod.PUT, "/clientes/*/atualizar").hasRole("CLIENT")
                        .requestMatchers(HttpMethod.POST, "/clientes/inserir").permitAll()
                        .requestMatchers(HttpMethod.GET, "/clientes/*/buscar", "/clientes/paginacao").hasRole("CLIENT")

                        .requestMatchers(HttpMethod.DELETE, "/administradores/*/deletar", "/administradores/*/desativar").hasRole("ADMINISTRATOR")
                        .requestMatchers(HttpMethod.PUT, "/administradores/*/atualizar").hasRole("ADMINISTRATOR")
                        .requestMatchers(HttpMethod.POST, "/administradores/inserir").permitAll()
                        .requestMatchers(HttpMethod.GET, "/administradores/*/buscar", "/administradores/buscartodos").hasRole("ADMINISTRATOR")

                        .requestMatchers(HttpMethod.DELETE, "/pedidos/*/deletar", "/pedidos/*/desativar").hasRole("ADMINISTRATOR")
                        .requestMatchers(HttpMethod.PUT, "/pedidos/*/atualizar").hasRole("ADMINISTRATOR")
                        .requestMatchers(HttpMethod.POST, "/pedidos/inserir").hasRole("CLIENT")
                        .requestMatchers(HttpMethod.GET, "/pedidos/paginacao").hasRole("CLIENT")

                        .requestMatchers(HttpMethod.DELETE, "/agendamentos/*/deletar", "/agendamentos/*/desativar").hasRole("ADMINISTRATOR")
                        .requestMatchers(HttpMethod.PUT, "/agendamentos/*/atualizar").hasRole("ADMINISTRATOR")
                        .requestMatchers(HttpMethod.POST, "/agendamentos/inserir").hasRole("CLIENT")
                        .requestMatchers(HttpMethod.GET, "/agendamentos/paginacao").hasRole("CLIENT")

                        .requestMatchers(HttpMethod.POST, "/autenticacao/login").permitAll()
                        .requestMatchers(HttpMethod.GET, "/autenticacao/token/cliente").hasRole("CLIENT")
                        .requestMatchers(HttpMethod.GET,  "/autenticacao/token/administrador").hasRole("ADMINISTRATOR")

                        .anyRequest().authenticated()
                ).sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(filterToken, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}