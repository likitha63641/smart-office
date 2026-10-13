package com.smartoffice;

import com.smartoffice.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) { this.jwtAuthenticationFilter=jwtAuthenticationFilter; }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
            .cors(cors -> {})
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/index.html", "/auth/login", "/auth/register", "/auth/logout", "/auth.js", "/favicon.ico", "/error").permitAll()
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                .requestMatchers("/actuator/health", "/actuator/info").permitAll()
                .requestMatchers(HttpMethod.GET, "/categories").hasAnyRole("USER", "ADMIN")
                .requestMatchers("/employees/**", "/technicians/**", "/notifications/email", "/categories/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/notifications/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers("/issues/*/status", "/issues/*/assign/*", "/issues/status/**", "/issues/priority/**", "/issues/category/**", "/issues/statistics", "/issues/category-statistics", "/issues/technician-statistics", "/issues/filter/**", "/issues/page/**", "/issues/search", "/issues/sorted-by-priority", "/issues/with-details/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/issues/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/issues").hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.GET, "/issues/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.PUT, "/issues/*").hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.POST, "/issues/*/attachment").hasAnyRole("USER", "ADMIN")
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }
    @Bean AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception { return configuration.getAuthenticationManager(); }
}
