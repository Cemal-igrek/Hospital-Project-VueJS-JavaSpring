package com.example.Hospital.security;

// ... (diğer importlar)
import com.example.Hospital.service.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.micrometer.common.lang.NonNull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        log.trace(">>> JwtAuthFilter - START for URI: {}", request.getRequestURI());

        final String token = extractTokenFromCookie(request);

        if (token == null) {
            log.trace("JwtAuthFilter - Token is null. Passing to next filter.");
            filterChain.doFilter(request, response);
            log.trace("<<< JwtAuthFilter - END (No Token)");
            return;
        } else {
            // Token'ın sadece ilk birkaç karakterini loglayalım (güvenlik)
            log.trace("JwtAuthFilter - Found token starting with: {}", token.substring(0, Math.min(token.length(), 10)) + "...");
        }


        String username = null;
        UserDetails userDetails = null;
        boolean tokenIsValid = false;

        try {
            username = jwtService.extractUsername(token);
            log.debug("JwtAuthFilter - Extracted username: {}", username);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                log.trace("JwtAuthFilter - SecurityContext is null, attempting to load user details.");
                try {
                    userDetails = this.userDetailsService.loadUserByUsername(username);
                    log.debug("JwtAuthFilter - UserDetails loaded for {}", username);

                    tokenIsValid = jwtService.isTokenValid(token, userDetails);
                    log.debug("JwtAuthFilter - Token validation result for {}: {}", username, tokenIsValid);

                } catch (UsernameNotFoundException e) {
                    log.warn("JwtAuthFilter - User '{}' not found in DB.", username);
                    // Kullanıcı yoksa devam et, kimlik doğrulanmamış kalacak
                }

                if (userDetails != null && tokenIsValid) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities()
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    log.trace("JwtAuthFilter - Attempting to set Authentication in SecurityContext for {}", username);
                    SecurityContextHolder.getContext().setAuthentication(authToken);

                    // *** YENİ KONTROL: Context'e ekledikten HEMEN SONRA kontrol et ***
                    Authentication authInContext = SecurityContextHolder.getContext().getAuthentication();
                    if (authInContext != null && authInContext.isAuthenticated() && authInContext.getName().equals(username)) {
                        log.info("JwtAuthFilter - SUCCESS: User '{}' set in SecurityContext.", username);
                    } else {
                        log.error("JwtAuthFilter - FAILURE: Failed to set user '{}' in SecurityContext! Context contains: {}",
                                username, authInContext);
                    }
                }
            } else {
                log.trace("JwtAuthFilter - Username null or user already authenticated.");
            }

        } catch (ExpiredJwtException e) { log.warn("JwtAuthFilter - JWT expired: {}", e.getMessage());
        } catch (MalformedJwtException e) { log.warn("JwtAuthFilter - JWT malformed: {}", e.getMessage());
        } catch (Exception e) { log.error("JwtAuthFilter - JWT processing error: {}", e.getMessage(), e);
        }

        log.trace("JwtAuthFilter - Proceeding down the filter chain.");
        filterChain.doFilter(request, response);
        log.trace("<<< JwtAuthFilter - END for URI: {}", request.getRequestURI());
    }

    // extractTokenFromCookie metodu aynı kalabilir


    private String extractTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        // *** YENİ BASİT LOG 4 ***
        log.trace("Attempting to extract token from cookies...");
        if (cookies == null) {
            log.trace("No cookies found in request.");
            return null;
        }
        for (Cookie cookie : cookies) {
            log.trace("Found cookie: Name={}, Value=[REDACTED]", cookie.getName()); // Değeri loglama
            if ("auth-token".equals(cookie.getName())) {
                log.trace("Found 'auth-token' cookie.");
                return cookie.getValue();
            }
        }
        log.trace("'auth-token' cookie not found.");
        return null;
    }

}