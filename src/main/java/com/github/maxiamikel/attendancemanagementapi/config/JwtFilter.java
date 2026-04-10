package com.github.maxiamikel.attendancemanagementapi.config;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.github.maxiamikel.attendancemanagementapi.entity.Role;
import com.github.maxiamikel.attendancemanagementapi.entity.User;
import com.github.maxiamikel.attendancemanagementapi.exceptions.ResourceNotFoundException;
import com.github.maxiamikel.attendancemanagementapi.exceptions.TokenJwtException;
import com.github.maxiamikel.attendancemanagementapi.repository.UserRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            String token = getTokenFromRequest(request);
            if (token != null) {
                String email = jwtService.getEmailFromToken(token);
                User user = userRepository.findByEmail(email)
                        .orElseThrow(() -> new ResourceNotFoundException("User not found"));
                setUserAsAuthenticated(user);
            }
        } catch (TokenJwtException e) {
            System.err.println("Invalid token");
        }
        filterChain.doFilter(request, response);
    }

    private void setUserAsAuthenticated(User user) {

        List<String> roles = user.getRoles()
                .stream()
                .map(Role::getName)
                .toList();

        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .roles(roles.toArray(new String[0]))
                .build();

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails,
                "", userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    private String getTokenFromRequest(HttpServletRequest request) {

        String authHeader = request.getHeader("Authorization");
        if (authHeader != null) {
            String[] headerParts = authHeader.split(" ");
            if (headerParts.length == 2) {
                return headerParts[1];
            }
        }
        return null;
    }

}
