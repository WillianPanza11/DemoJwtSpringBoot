package com.example.demo.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.service.CustomUserDetailsService;
import com.example.demo.util.JwtUtil;

@Component

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String bearerToken = request.getHeader("Authorization"); 
        String username = null; 
        String token = null; 

        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            //extraer jwt tiken de bearerToken
            token = bearerToken.substring(7);

            try {
                username = jwtUtil.extractUsername(token);

                UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

                //checkear seguridad
                if(username !=null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UsernamePasswordAuthenticationToken upat = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    upat.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(upat);
                }else{
                    System.out.println("Invalid Token!");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            System.out.println("Invalid Bearer Token Format!");
        }

        //si todo es bien 
        filterChain.doFilter(request, response);
    }

}
