    package com.lohith.jwtSecurity.config;


    import com.lohith.jwtSecurity.filters.JwtAuthFilter;
    import com.lohith.jwtSecurity.model.User;
    import com.lohith.jwtSecurity.repo.UserRepo;
    import com.lohith.jwtSecurity.services.MyUserDetailsService;
    import com.lohith.jwtSecurity.services.ServiceImpl.UserServiceImpl;
    import lombok.RequiredArgsConstructor;
    import org.springframework.beans.factory.annotation.Qualifier;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.security.authentication.AuthenticationManager;
    import org.springframework.security.authentication.AuthenticationProvider;
    import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
    import org.springframework.security.config.Customizer;
    import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
    import org.springframework.security.config.annotation.web.builders.HttpSecurity;
    import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
    import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
    import org.springframework.security.config.http.SessionCreationPolicy;
    import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
    import org.springframework.security.crypto.password.PasswordEncoder;
    import org.springframework.security.oauth2.core.user.OAuth2User;
    import org.springframework.security.web.SecurityFilterChain;
    import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
    import org.springframework.web.cors.CorsConfiguration;
    import org.springframework.web.cors.CorsConfigurationSource;
    import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

    import java.util.List;

    @Configuration
    @EnableWebSecurity
    @RequiredArgsConstructor
    public class securityConfig {

        private final MyUserDetailsService userDetailsService;
        private final JwtAuthFilter jwtAuthFilter;
        private final UserRepo userRepo;
        private final AuthUtil authUtil;

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http, @Qualifier("corsConfigurationSource") CorsConfigurationSource corsSource) throws Exception {
            return http
                    .cors(cors -> cors.configurationSource(corsSource))
                    .csrf(AbstractHttpConfigurer::disable)
                    .authorizeHttpRequests(requests -> requests
                            .requestMatchers(
                                    "/v3/api-docs/**",
                                    "/swagger-ui/**",
                                    "/swagger-ui.html"
                            ).permitAll()
                            .requestMatchers("/auth/**").permitAll()
                            .anyRequest().authenticated())
                    .sessionManagement(session -> session
                            .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .oauth2Login(oauth2 -> oauth2.successHandler(
                            (request, response, authentication) -> {
                                OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

                                String googleId = oAuth2User.getAttribute("sub");
                                String email = oAuth2User.getAttribute("email");
                                String username = oAuth2User.getAttribute("name");

                                User user = userRepo.findByUserName(username).orElse(new User());
                                user.setUserName(username);
                                user.setEmail(email);
                                user.setProvider("google");
                                user.setProvider_id_sub(googleId);
                                userRepo.save(user);

                                String jwt = authUtil.generateAccessToken(user);

                                // Redirect to React app with JWT as query param
                                String redirectUrl = "http://localhost:5173/oauth2/redirect?token=" + jwt;
                                response.sendRedirect(redirectUrl);
                            }
                    ))

                    .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                    .build();
        }

        @Bean
        public AuthenticationProvider authenticationProvider() {
            DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
            provider.setPasswordEncoder(new BCryptPasswordEncoder(12));
            provider.setUserDetailsService(userDetailsService);

            return provider;
        }

        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
            return configuration.getAuthenticationManager();
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder(12);
        }

        @Bean
        public CorsConfigurationSource corsConfigurationSource() {
            CorsConfiguration config = new CorsConfiguration();
            config.setAllowedOrigins(List.of("http://localhost:5173"));
            config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
            config.setAllowedHeaders(List.of("*"));
            config.setAllowCredentials(true);

            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
            source.registerCorsConfiguration("/**", config);
            return source;
        }
    }
