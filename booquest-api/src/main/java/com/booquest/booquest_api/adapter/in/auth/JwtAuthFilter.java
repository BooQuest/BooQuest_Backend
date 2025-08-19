package com.booquest.booquest_api.adapter.in.auth;

import com.booquest.booquest_api.application.port.in.auth.TokenUseCase;
import com.booquest.booquest_api.application.port.out.auth.JwtTokenPort;
import com.booquest.booquest_api.application.port.out.auth.dto.TokenRefreshResponse;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@AllArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtTokenPort jwt;
    private final TokenUseCase tokenUseCase;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();
        if (path.startsWith("/api/auth/")) {
            filterChain.doFilter(request, response);
            return;
        }

        String auth = request.getHeader("Authorization");
        if (auth != null && auth.startsWith("Bearer ")) {
            String token = auth.substring(7);
            try {
                var claimsJws = jwt.parse(token);
                var body = claimsJws.getBody();
                String principal = body.getSubject();   // principal => userId

                var authentication = new UsernamePasswordAuthenticationToken(principal, null, Collections.emptyList());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (ExpiredJwtException e) {
                // 토큰이 만료된 경우, 리프레시 토큰으로 갱신 시도
                handleTokenExpiration(request, response, filterChain);
                return;
            } catch (Exception e) {
                SecurityContextHolder.clearContext();
            }
        }
        filterChain.doFilter(request, response);
    }

    private void handleTokenExpiration(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String refreshToken = request.getHeader("X-Refresh-Token");
        
        if (refreshToken != null) {
            try {
                TokenRefreshResponse newToken = tokenUseCase.refreshAccessToken(refreshToken);

                response.setHeader("X-New-Access-Token", newToken.getAccessToken());
                response.setHeader("X-Token-Type", newToken.getTokenType());
                response.setHeader("X-Expires-In", String.valueOf(newToken.getExpiresIn()));

                var claimsJws = jwt.parse(newToken.getAccessToken());
                var body = claimsJws.getBody();
                String principal = body.getSubject();
                
                var authentication = new UsernamePasswordAuthenticationToken(principal, null, Collections.emptyList());
                SecurityContextHolder.getContext().setAuthentication(authentication);

                filterChain.doFilter(request, response);
                return;
            } catch (Exception e) {
                SecurityContextHolder.clearContext();
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token refresh failed");
                return;
            }
        }

        SecurityContextHolder.clearContext();
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token expired");
    }
}
