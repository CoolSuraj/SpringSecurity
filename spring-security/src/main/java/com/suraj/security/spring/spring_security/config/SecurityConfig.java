package com.suraj.security.spring.spring_security.config;

import static org.springframework.security.config.Customizer.withDefaults;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/*
 * Use this annotation for enabling configuration for security
 */
@EnableWebSecurity
/*
 * for using @preAuthorize for role specific security
 */
@EnableMethodSecurity
@Configuration
public class SecurityConfig {
	
	@Autowired
	DataSource dataSource;
	
	/*
	 * Create this type of Bean for bypassing the default settings
	 * In this Example we are just keeping basic Authentication which will use 
	password = spring.security.user.password = 
		user = spring.security.user.name , if set in application.properties
	 */
	
	@Bean
	public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {

		http.authorizeHttpRequests((requests) -> requests.requestMatchers("/h2-console/**").permitAll()/*only to stop authentication to h2 DB*/
				.anyRequest().authenticated());
		// http.formLogin(withDefaults()); //we can stop this in manual configuration
		/* httpBasic
		 * In PostMan go to authorization and then select Basic Authorization and give username and password to access 
		 * this if done from browser it will stay log in until you close the session 
		 * this makes authorization via header encoded in base64 format
		 */
		http.httpBasic(withDefaults());
		/*
		 * To make it stateless use below Code
		 * which Means Spring Security will never create an HttpSession and it will 
		 * never use it to obtain the SecurityContext
	I	 * In Simple Words it will not set any cookie
		 */
		http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		/**
		 * For H2 DB to work we have to do some changes 
		 */
		http.csrf(csrf->csrf.disable());
		http.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()));
		
		return http.build();

	}
	
	@Bean
	public UserDetailsService userDetailsService() {
		
		/**
		 * with This In Memory user Details Manager we can create multiple users
		 * {noop} is used to tell Spring to Store this Password as Plain Text 
		 */
//		UserDetails user1=User.withUsername("user1").password("{noop}Password1").roles("USER").build();
		/*
		 * after adding PasswordEncoder the passwords while saving will not show as plain text but some hashed value
		 */
		UserDetails user1=User.withUsername("user1").password(passwordEncoder().encode("Password1")).roles("USER").build();
		UserDetails admin = User.withUsername("admin").password(passwordEncoder().encode("admin1")).roles("ADMIN").build();
		
		JdbcUserDetailsManager userDetailsManager = new JdbcUserDetailsManager(dataSource);
		userDetailsManager.createUser(user1);
		userDetailsManager.createUser(admin);
		return userDetailsManager;
//		return new InMemoryUserDetailsManager(user1,admin);
		
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		//implementating Bcrypt Hashing 
		return new BCryptPasswordEncoder();
	}
	
}
