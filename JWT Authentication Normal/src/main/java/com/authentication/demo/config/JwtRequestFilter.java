
package com.authentication.demo.config;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.authentication.demo.serviceImpl.JwtUserDetailsServiceImpl;

import io.jsonwebtoken.ExpiredJwtException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

	@Autowired
	private JwtUserDetailsServiceImpl jwtUserDetailsService;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		System.out.println(request.getLocalAddr()+request.getRequestURI());
		return request.getRequestURI().equals("/v1/user") || request.getRequestURI().equals("/login")
				|| request.getRequestURI().equals("/authenticate")
				|| request.getRequestURI().equals("/authenticate-by-email")
				|| request.getRequestURI().equals("/refreshToken")
				|| request.getRequestURI().equals("/v1/user/save")
				|| request.getRequestURI().equals("/v1/hello")
				|| request.getRequestURI().equals("/testlogin");
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {

		final String requestTokenHeader = request.getHeader("Authorization");

		String username = null;
		String jwtToken = null; // JWT Token is in the form
		// "Bearer token". Remove Bearer word and get // only the Token
		if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
			jwtToken = requestTokenHeader.substring(7);
			try {
				username = jwtTokenUtil.getUsernameFromToken(jwtToken);
				System.out.println("Extracted username: " + username);
				if (username == null) {
					response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token is not available");
					return;
				}

			} catch (IllegalArgumentException e) {
				System.out.println("Unable to get JWT Token");
			} catch (ExpiredJwtException e) {
				System.out.println("JWT Token has expired");
			} catch (Exception e) {
				System.out.println("Error parsing JWT Token: " + e.getMessage());
			}
		} else {
			// Token is invalid, send 401 Unauthorized response
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token is not available");
			return;
		}

		// Once we get the token validate it.
		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername(username);
			if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				usernamePasswordAuthenticationToken
						.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			}
		}

		chain.doFilter(request, response);
	}
}
