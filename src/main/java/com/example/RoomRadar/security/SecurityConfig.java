    package com.example.RoomRadar.security;

    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.context.annotation.*;
    import org.springframework.http.HttpMethod;
    import org.springframework.security.authentication.*;
    import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
    import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
    import org.springframework.security.config.annotation.web.builders.HttpSecurity;
    import org.springframework.security.config.http.*;
    import org.springframework.security.crypto.password.*;
    import org.springframework.security.web.SecurityFilterChain;
    import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
    import org.springframework.web.cors.*;
    import org.springframework.web.servlet.config.annotation.EnableWebMvc;

    import java.util.List;

    @Configuration
    @EnableWebMvc
    public class SecurityConfig {

        @Autowired
        private JwtAuthenticationFilter jwtFilter;

        @Autowired
        private CustomUserDetailsService customUserDetailsService;

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http
                    .cors(cors -> cors.configurationSource(corsConfigurationSource())) // ✅ inject cors config here
                    .csrf(csrf -> csrf.disable())
                    .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .authorizeHttpRequests(auth -> auth
                            .requestMatchers("/auth/**", "/api/users/**", "/api/otp/**", "/api/auth/**", "/api/admins/createAdmin").permitAll()
                            .requestMatchers(HttpMethod.GET, "/api/rooms/**").permitAll()
                            .requestMatchers(HttpMethod.GET, "/api/rooms/approved").permitAll()
                            .anyRequest().authenticated()
                    )
                    .authenticationProvider(authenticationProvider())
                    .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

            return http.build();
        }


        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
            return config.getAuthenticationManager();
        }

        @Bean
        public DaoAuthenticationProvider authenticationProvider() {
            DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
            provider.setUserDetailsService(customUserDetailsService);
            provider.setPasswordEncoder(passwordEncoder());
            return provider;
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
            return NoOpPasswordEncoder.getInstance(); // For dev only — use BCrypt in production!
        }


        @Bean
        CorsConfigurationSource corsConfigurationSource() {
            CorsConfiguration config = new CorsConfiguration();
            config.setAllowedOrigins(List.of("https://roomradar.in/", "https://www.roomradar.in/"));
            config.setAllowCredentials(true);
            config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
            config.setAllowedHeaders(List.of("*"));
            config.setExposedHeaders(List.of("Authorization", "Content-Type"));

            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
            source.registerCorsConfiguration("/**", config);
            return source;
        }



    }
