package com.suraj.security.spring.spring_security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

/*
 * Use this annotation for enabling configuration for security
 */
@EnableWebSecurity
@Configuration
public class SecurityConfig {
	
	/*
	 * Create this type of Bean for bypassing the default settings
	 * In this Example we are just keeping basic Authentication which will use 
	password = spring.security.user.password = 
		user = spring.security.user.name , if set in application.properties
	 */
	@Bean
	public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {

		http.authorizeHttpRequests((requests) -> requests.anyRequest().authenticated());
		// http.formLogin(withDefaults()); //we can stop this in manual configuration
		/*
		 * In PostMan go to authorization and then select Basic Authorization and give username and password to access 
		 * this if done from browser it will stay log in until you close the session 
		 */
		http.httpBasic(withDefaults());

		return http.build();

	}
}
