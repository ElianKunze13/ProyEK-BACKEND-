package com.example.demo.config;

import com.example.demo.authsecurity.JwtAuthFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.XXssProtectionHeaderWriter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;
import java.util.Objects;

import static org.springframework.security.config.Customizer.withDefaults;

/*
@Configuration
@EnableWebSecurity
public class SecurityConfig {
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/", "/home",  "/api/*", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
            .anyRequest().authenticated()   )
        .httpBasic(Customizer.withDefaults()); // Configuración de autenticación básica
    return http.build();  }

  @Bean
  public UserDetailsService userDetailsService() {
    UserDetails user = User.withDefaultPasswordEncoder()
        .username("user") .password("password")
        .roles("USER")
        .build();
    UserDetails admin = User.withDefaultPasswordEncoder()
        .username("admin")
        .password("admin123")
        .roles("ADMIN")
        .build();
    return new InMemoryUserDetailsManager(user, admin);}
}
*/@Configuration
@EnableWebSecurity
@Slf4j
@EnableMethodSecurity(prePostEnabled = true) // Activar seguridad a nivel de métodos
//@RequiredArgsConstructor
public class SecurityConfig {


  private final JwtAuthFilter jwtAuthFilter;
  private final UserDetailsService userDetailsService;

  private static final String[] WHITE_LIST = {
      "/api/v1/auth/**", "/api/v1/**" , "/api/v1/test/**",
      "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html"
  };

  public SecurityConfig(JwtAuthFilter jwtAuthFilter, UserDetailsService userDetailsService) {
    this.jwtAuthFilter = Objects.requireNonNull(jwtAuthFilter, "JwtAuthFilter is required");
    this.userDetailsService = Objects.requireNonNull(userDetailsService, "UserDetailsService is required");
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    log.info("🛡️ Configurando seguridad");

    http
        .csrf(AbstractHttpConfigurer::disable)
        // Configuración CSRF para APIs stateless
        /*.csrf(csrf -> csrf
            .ignoringRequestMatchers(WHITE_LIST) // Opcional: endpoints públicos
            .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
        )*/
        .cors(cors -> cors.configurationSource(corsConfig()))
        .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .headers(headers -> headers
            // Activa X-XSS-Protection
            .xssProtection(xss -> xss
                .headerValue(XXssProtectionHeaderWriter.HeaderValue.ENABLED_MODE_BLOCK)
            )
            // Política de Seguridad de Contenido (CSP)
            .contentSecurityPolicy(csp -> csp
                .policyDirectives("default-src 'self'; script-src 'self'; style-src 'self'; object-src 'none'")
            )
            // Otras cabeceras de seguridad
            .frameOptions(frame -> frame.deny())
            .httpStrictTransportSecurity(hsts -> hsts
                .includeSubDomains(true)
                .preload(true)
                .maxAgeInSeconds(31536000) // 1 año
            )
            // Prevención de Content Sniffing (Agregar esta línea)
            .contentTypeOptions(withDefaults())
        )
        .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(auth -> auth
            .requestMatchers(WHITE_LIST).permitAll()
            .anyRequest().authenticated())
        .authenticationProvider(authProvider())
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }


  @Bean
  public AuthenticationManager authManager(AuthenticationConfiguration config) throws Exception {
    return config.getAuthenticationManager();
  }

  @Bean
  public CorsConfigurationSource corsConfig() {
    CorsConfiguration config = new CorsConfiguration();
    //rutas permitidas modificadas para permitir el acceso desde el frontend en desarrollo y producción
      config.setAllowedOrigins(List.of(
              "http://localhost:4200",
              "http://localhost:8080",
              System.getenv("FRONTEND_URL") != null ? System.getenv("FRONTEND_URL") : ""
      ));
    config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    config.setAllowedHeaders(List.of("*"));
    config.setAllowCredentials(true);

    //config.setAllowedHeaders(List.of("Authorization", "Content-Type", "X-Requested-With"));
    //config.setExposedHeaders(List.of("Content-Security-Policy", "X-XSS-Protection"));
    config.setMaxAge(3600L);

    var source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);
    return source;

  }

  @Bean
  public AuthenticationProvider authProvider() {
    var provider = new DaoAuthenticationProvider(userDetailsService);
//    provider.setUserDetailsService(userDetailsService);
    provider.setPasswordEncoder(new BCryptPasswordEncoder(12));
    return provider;
  }

  @Bean
  public PasswordEncoder passwordEncoder()
  {
    return new BCryptPasswordEncoder(12);
  }
}