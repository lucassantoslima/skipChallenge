package com.skipthedishes.conf;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.StringUtils;

import com.skipthedishes.security.CustomUserDetailsService;
import com.skipthedishes.utils.SystemCostant;

import io.jsonwebtoken.Jwts;

public class JWTAuthorization extends BasicAuthenticationFilter {
	
	private final CustomUserDetailsService customUserDetailService;

	@Autowired
	public JWTAuthorization(final AuthenticationManager authenticationManager, final CustomUserDetailsService customUserDetailService) {
		super(authenticationManager);
		this.customUserDetailService = customUserDetailService;
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
		final String header = request.getHeader(SystemCostant.HEADER_AUTHORIZATION);

		if (header == null || !header.startsWith(SystemCostant.BEARER_PREFIX)) {
			chain.doFilter(request, response);
			return;
		}
		try {
			final UsernamePasswordAuthenticationToken authenticationToken = this.getTokenHeader(request);

			SecurityContextHolder.getContext().setAuthentication(authenticationToken);
			chain.doFilter(request, response);
		} catch (final Exception e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
		}
	}
	
	private UsernamePasswordAuthenticationToken getTokenHeader( final HttpServletRequest request ) {
		final String token = request.getHeader(SystemCostant.HEADER_AUTHORIZATION);
		if (token == null)
			return null;

		final String username = Jwts.parser()
									.setSigningKey(SystemCostant.SECRET_KEY)
				                    .parseClaimsJws(token.replace(SystemCostant.BEARER_PREFIX, ""))
				                    .getBody()
				                    .getSubject();
		
		if( StringUtils.isEmpty(username)){
			return null;
		}
		
		final UserDetails userDetails = this.customUserDetailService.loadUserByUsername(username);
		
		return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
	}

}
