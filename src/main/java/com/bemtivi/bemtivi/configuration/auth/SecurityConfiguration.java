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
                        .requestMatchers(HttpMethod.DELETE, "/servico/{id}/deletar").hasRole("ADMINISTRATOR")
                        .requestMatchers(HttpMethod.DELETE, "/servico/{id}/desativar").hasRole("ADMINISTRATOR")
                        .requestMatchers(HttpMethod.PUT, "/servico/{id}/atualizar").hasRole("ADMINISTRATOR")
                        .requestMatchers(HttpMethod.POST, "/servico/inserir").hasRole("ADMINISTRATOR")
                        .requestMatchers(HttpMethod.GET, "/servico/{id}/buscar").permitAll()
                        .requestMatchers(HttpMethod.GET, "/servico/paginacao").permitAll()

                        .requestMatchers(HttpMethod.DELETE, "/produto/{id}/deletar").hasRole("ADMINISTRATOR")
                        .requestMatchers(HttpMethod.DELETE, "/produto/{id}/desativar").hasRole("ADMINISTRATOR")
                        .requestMatchers(HttpMethod.PUT, "/produto/{id}/atualizar").hasRole("ADMINISTRATOR")
                        .requestMatchers(HttpMethod.POST, "/produto/inserir").hasRole("ADMINISTRATOR")
                        .requestMatchers(HttpMethod.GET, "/produto/{id}/buscar").permitAll()
                        .requestMatchers(HttpMethod.GET, "/produto/paginacao").permitAll()

                        .requestMatchers(HttpMethod.DELETE, "/categoria/{id}/deletar").hasRole("ADMINISTRATOR")
                        .requestMatchers(HttpMethod.DELETE, "/categoria/{id}/desativar").hasRole("ADMINISTRATOR")
                        .requestMatchers(HttpMethod.PUT, "/categoria/{id}/atualizar").hasRole("ADMINISTRATOR")
                        .requestMatchers(HttpMethod.POST, "/categoria/inserir").hasRole("ADMINISTRATOR")
                        .requestMatchers(HttpMethod.GET, "/categoria/{id}/buscar").permitAll()
                        .requestMatchers(HttpMethod.GET, "/categoria/paginacao").permitAll()


                        .requestMatchers(HttpMethod.DELETE, "/cliente/{id}/deletar").hasRole("CLIENT")
                        .requestMatchers(HttpMethod.DELETE, "/cliente/{id}/desativar").hasRole("CLIENT")
                        .requestMatchers(HttpMethod.PUT, "/cliente/{id}/atualizar").hasRole("CLIENT")
                        .requestMatchers(HttpMethod.POST, "/cliente/inserir").permitAll()
                        .requestMatchers(HttpMethod.GET, "/cliente/{id}/buscar").hasRole("CLIENT")
                        .requestMatchers(HttpMethod.GET, "/cliente/paginacao").hasRole("ADMINISTRATOR")

                        .requestMatchers(HttpMethod.DELETE, "/administrador/{id}/deletar").hasRole("ADMINISTRATOR")
                        .requestMatchers(HttpMethod.DELETE, "/administrador/{id}/desativar").hasRole("ADMINISTRATOR")
                        .requestMatchers(HttpMethod.PUT, "/administrador/{id}/atualizar").hasRole("ADMINISTRATOR")
                        .requestMatchers(HttpMethod.POST, "/administrador/inserir").hasRole("ADMINISTRATOR")
                        .requestMatchers(HttpMethod.GET, "/administrador/{id}/buscar").hasRole("ADMINISTRATOR")
                        .requestMatchers(HttpMethod.GET, "/administrador/paginacao").hasRole("ADMINISTRATOR")

                        .requestMatchers(HttpMethod.DELETE, "/pedido/{id}/deletar").hasRole("ADMINISTRATOR")
                        .requestMatchers(HttpMethod.DELETE, "/pedido/{id}/desativar").hasRole("ADMINISTRATOR")
                        .requestMatchers(HttpMethod.PUT, "/pedido/{id}/atualizar").hasRole("ADMINISTRATOR")
                        .requestMatchers(HttpMethod.POST, "/pedido/inserir").hasRole("CLIENT")
                        .requestMatchers(HttpMethod.GET, "/pedido/paginacao").hasRole("CLIENT")

                        .requestMatchers(HttpMethod.DELETE, "/agendamento/{id}/deletar").hasRole("ADMINISTRATOR")
                        .requestMatchers(HttpMethod.DELETE, "/agendamento/{id}/desativar").hasRole("ADMINISTRATOR")
                        .requestMatchers(HttpMethod.PUT, "/agendamento/{id}/atualizar").hasRole("ADMINISTRATOR")
                        .requestMatchers(HttpMethod.POST, "/agendamento/inserir").hasRole("CLIENT")
                        .requestMatchers(HttpMethod.GET, "/agendamento/paginacao").hasRole("CLIENT")

                        .requestMatchers(HttpMethod.POST, "/autorizacao/login").permitAll()
                        .requestMatchers(HttpMethod.GET, "/autorizacao/token/cliente").hasRole("CLIENT")
                        .requestMatchers(HttpMethod.GET, "/autorizacao/token/administrador").hasRole("ADMINISTRATOR")
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