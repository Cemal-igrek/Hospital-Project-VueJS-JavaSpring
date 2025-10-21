package com.example.Hospital.security;

import com.example.Hospital.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component // Bu sınıfı bir Spring Bean'i (bileşeni) olarak tanımlar
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

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

        // 1. Cookie'den token'ı bul
        final String token = extractTokenFromCookie(request);

        if (token == null) {
            // Eğer token yoksa, isteği sonraki filtreye devret ve çık
            filterChain.doFilter(request, response);
            return;
        }

        // 2. Token'dan kullanıcı adını (username) çıkar
        final String username = jwtService.extractUsername(token);

        // 3. Kullanıcı adı varsa VE kullanıcı henüz kimlik doğrulamamışsa
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // 4. Veritabanından kullanıcıyı UserDetails olarak çek
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            // 5. Token'ı doğrula
            if (jwtService.isTokenValid(token, userDetails)) {
                // 6. Token geçerliyse, Spring Security için bir kimlik doğrulama nesnesi oluştur
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null, // Kimlik doğrulaması zaten yapıldı (JWT ile), o yüzden şifre (credentials) null
                        userDetails.getAuthorities() // Kullanıcının rolleri (ADMIN, DOCTOR vs.)
                );

                // 7. Bu kimlik doğrulama nesnesine isteğin detaylarını (IP adresi, session vb.) ekle
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                // 8. KİMLİK DOĞRULAMASINI GÜNCELLE:
                // Spring'in güvenlik 'context'ine bu kullanıcıyı "giriş yapmış" olarak tanıt
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // 9. İsteği zincirdeki bir sonraki filtreye devam ettir
        filterChain.doFilter(request, response);
    }

    /**
     * Gelen isteğin cookie dizisinden 'auth-token' adlı cookie'yi bulan yardımcı metot.
     */
    private String extractTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if ("auth-token".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }
}