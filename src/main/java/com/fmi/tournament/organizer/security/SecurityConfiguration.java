package com.fmi.tournament.organizer.security;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {

  private static final String PARTICIPANT_ROLE = "PARTICIPANT";
  private static final String ORGANIZER_ROLE = "ORGANIZER";
  private static final String ADMIN_ROLE = "ADMIN";

  private final BaseUserDetailsService baseUserDetailsService;

  @Autowired
  public SecurityConfiguration(BaseUserDetailsService baseUserDetailsService) {
    this.baseUserDetailsService = baseUserDetailsService;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
    httpSecurity.authorizeHttpRequests(registry -> {
      registry.requestMatchers(HttpMethod.GET, "/").permitAll();
      registry.requestMatchers(HttpMethod.POST, "/users").permitAll();
      registry.anyRequest().authenticated();
    });
    httpSecurity.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
    httpSecurity.formLogin(withDefaults());
    httpSecurity.httpBasic(withDefaults());
    httpSecurity.csrf(AbstractHttpConfigurer::disable);
    return httpSecurity.build();
  }

//  @Bean
//  public UserDetailsService userDetailsService() {
//    UserDetails participant = User.builder()
//        .username("participant")
//        .password(passwordEncoder().encode("1234"))
//        .roles(PARTICIPANT_ROLE)
//        .build();
//
//    UserDetails organizer = User.builder()
//        .username("organizer")
//        .password(passwordEncoder().encode("5678"))
//        .roles(ORGANIZER_ROLE)
//        .build();
//
//    UserDetails admin = User.builder()
//        .username("admin")
//        .password(passwordEncoder().encode("admin"))
//        .roles(PARTICIPANT_ROLE, ORGANIZER_ROLE, ADMIN_ROLE)
//        .build();
//
//    return new InMemoryUserDetailsManager(participant, organizer, admin);
//  }

  @Bean
  public AuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
    daoAuthenticationProvider.setUserDetailsService(baseUserDetailsService);
    daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
    return daoAuthenticationProvider;
  }

  @Bean
  public UserDetailsService userDetailsService() {
    return baseUserDetailsService;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
