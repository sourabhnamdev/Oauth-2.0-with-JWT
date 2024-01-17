
package com.authentication.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

	@Autowired
	private UserDetailsService jwtUserDetailsService;

	
	  @Autowired private JwtRequestFilter jwtRequestFilter;
	 

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception { // configure AuthenticationManager
																						// so that it knows from
		// where to load // user for matching credentials // Use
		// BCryptPasswordEncoder
		auth.userDetailsService(jwtUserDetailsService).passwordEncoder(bcryptPasswordEncoder());
	}

	@Bean
	public static BCryptPasswordEncoder bcryptPasswordEncoder() {
		return new BCryptPasswordEncoder();// base64
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception { 
	    httpSecurity.cors().and().csrf().disable() 
	            .authorizeRequests()
	            .antMatchers("/authenticate", "/v1/user/save", "/refreshToken","/authenticate-by-email","/v1/hello","/testlogin").permitAll()
	            .anyRequest().authenticated();
//	            .and()
//	            .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint);
//	            .and()
//	            .sessionManagement()
//	            .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	             // Add this line

	    // Add a filter to validate the tokens with every request
	    httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
	    
	    httpSecurity.oauth2Login()
        .defaultSuccessUrl("/v1/user", true);
	}

		
}
