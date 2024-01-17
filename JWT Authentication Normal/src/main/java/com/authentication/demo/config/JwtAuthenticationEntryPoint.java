
package com.authentication.demo.config;

import java.io.IOException;
import java.io.Serializable;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {

	private static final long serialVersionUID = 4599658191526971499L;

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {

		// response.sendRedirect("http://localhost:8080/login/oauth2/code/google");
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "UnAuthorized");
	}

}
