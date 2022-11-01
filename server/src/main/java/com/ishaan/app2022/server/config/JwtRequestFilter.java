package com.ishaan.app2022.server.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Objects;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String requestHeader = request.getHeader("Authorization");
        if (Objects.isNull(requestHeader) || !requestHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwtToken = requestHeader.substring(7);
        String email = jwtTokenUtil.getEmailFromJWT(jwtToken);
        if(Objects.isNull(email) || Objects.nonNull(SecurityContextHolder.getContext().getAuthentication())) {
            filterChain.doFilter(request, response);
            return;
        }

        UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(email);
        if(!jwtTokenUtil.validateJWT(jwtToken, userDetails)) {
            filterChain.doFilter(request, response);
            return;
        }

        UsernamePasswordAuthenticationToken authTok = new UsernamePasswordAuthenticationToken(userDetails, null, Collections.emptyList());
        authTok.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authTok);
        request.setAttribute("email", userDetails.getUsername());
        filterChain.doFilter(request, response);
    }
}