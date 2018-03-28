package com.skipthedishes.conf;

import java.io.IOException;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skipthedishes.dto.CustomerDto;
import com.skipthedishes.utils.SystemCostant;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JWTAuthentication extends UsernamePasswordAuthenticationFilter {
	
	private final AuthenticationManager authenticationManager;

    @Autowired
    public JWTAuthentication( final AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication( final HttpServletRequest request, final HttpServletResponse response) throws AuthenticationException {
        try { 
            final CustomerDto user = new ObjectMapper().readValue(request.getInputStream(), CustomerDto.class);
            
            return this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
        } catch (final Exception e) {
        	e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication( final HttpServletRequest request, final HttpServletResponse response, final FilterChain chain,
                                            final Authentication authResult) throws IOException, ServletException {
    	
        final User user = ((User) authResult.getPrincipal());
        
        final String jwsToken = Jwts.builder()
					                .setSubject(user.getUsername())
					                .claim("email", user.getUsername())
					                .claim("password", user.getPassword()) 
					                .setExpiration(new Date(System.currentTimeMillis() + 1296000000L))
					                .signWith(SignatureAlgorithm.HS512, SystemCostant.SECRET_KEY)
					                .compact();
        
        final String bearerToken = SystemCostant.BEARER_PREFIX + jwsToken;
        
        //write token in body response
        response.getWriter().write(bearerToken);
        
        //write token in head
        response.addHeader(SystemCostant.HEADER_AUTHORIZATION, bearerToken);
    }
}
