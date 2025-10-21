package com.example.Hospital.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final JwtAuthenticationFilter jwtAuthFilter; // YENİ: JWT Filtremizi enjekte et

    public SecurityConfig(UserDetailsService userDetailsService, JwtAuthenticationFilter jwtAuthFilter) {
        this.userDetailsService = userDetailsService;
        this.jwtAuthFilter = jwtAuthFilter; // YENİ
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // YENİ: CORS Yapılandırma Bean'i
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Frontend'inizin çalıştığı adresi buraya yazın (Vite varsayılanı 5173'tür)
        // React varsayılanı 3000'dir. Sizinki hangisiyse onu yazın.
        configuration.setAllowedOrigins(List.of("http://localhost:5173"));

        // İzin verilen HTTP metotları
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        // İzin verilen HTTP başlıkları
        configuration.setAllowedHeaders(List.of("*"));

        // *** EN ÖNEMLİ KISIM ***
        // Cookie'lerin (kimlik bilgisi) frontend'den backend'e gönderilmesine izin ver.
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Tüm /api yolları için geçerli
        return source;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable) // CSRF'yi modern yolla kapat

                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth

                        // 1. YENİ KURAL:
                        // Tüm 'OPTIONS' preflight isteklerine izinsiz izin ver.
                        // Bu, CORS hatasını (403) çözecektir.
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // 2. GİRİŞ (LOGIN) ve REFRESH endpoint'lerine izin ver
                        .requestMatchers("/api/auth/**").permitAll()

                        // 3. Admin oluşturduğumuz için /api/users iznini kaldırdık.

                        // 4. Swagger (API dokümantasyonu) için izin ver
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()

                        // 5. Yukarıdakiler dışındaki DİĞER TÜM istekler kimlik doğrulaması gerektirsin
                        .anyRequest().authenticated()
                )

                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}