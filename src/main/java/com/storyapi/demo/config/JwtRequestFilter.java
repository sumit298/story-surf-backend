package com.storyapi.demo.config;

import java.io.IOException;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    private static final Set<String> PUBLIC_ENDPOINTS = Set.of(
            "/api/users/register",
            "/api/users/register/",
            "/api/users/login",
            "/api/users/login/");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        String uri = request.getRequestURI();
        logger.info("[JWT Filter] Incoming request URI: " + uri);
        logger.info("[JWT Filter] Authorization header: " + request.getHeader("Authorization"));

        if (PUBLIC_ENDPOINTS.stream().anyMatch(uri::startsWith) || uri.startsWith("/h2-console")) {
            logger.info("[JWT Filter] Public endpoint, skipping JWT check: " + uri);
            chain.doFilter(request, response);
            return;
        }

        final String requestTokenHeader = request.getHeader("Authorization");
        String username = null;
        String jwtToken = null;

        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
            try {
                username = jwtUtil.extractUsername(jwtToken);
                logger.info("[JWT Filter] Extracted username from token: " + username);
            } catch (Exception e) {
                logger.warn("[JWT Filter] JWT Token extraction failed: " + e.getMessage());
            }
        } else {
            logger.info("[JWT Filter] No JWT token found or invalid header");
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            if (jwtUtil.validateToken(jwtToken, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
                logger.info("[JWT Filter] Authentication set for user: " + username);
            } else {
                logger.info("[JWT Filter] JWT token invalid for user: " + username);
            }
        } else {
            logger.info("[JWT Filter] No username found or user already authenticated");
        }

        chain.doFilter(request, response);
    }

}
