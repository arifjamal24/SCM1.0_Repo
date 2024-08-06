package com.scm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	public UserDetailsService getUserDetailService() {
		return new UserDetailServiceImpl();
	}
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {	
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setUserDetailsService(this.getUserDetailService());
		daoAuthenticationProvider.setPasswordEncoder(this.passwordEncoder());
		return daoAuthenticationProvider;
	}
	
	@Bean
	public SecurityFilterChain security(HttpSecurity http) throws Exception {
		http
			.csrf(csrf -> csrf.disable()) // make sure to maintain order
			.authorizeHttpRequests(register->{ register
			.requestMatchers("/admin/**").hasRole("ADMIN") 
			.requestMatchers("/user/**").hasRole("USER")
			.requestMatchers("/**").permitAll()
			.anyRequest().authenticated();
		})
		.formLogin(form->form.permitAll())
		.formLogin(login->login.loginPage("/signin"))
		.formLogin(login->login.loginProcessingUrl("/dologin"))
		.formLogin(login->login.defaultSuccessUrl("/user/index"));
		return http.build();
	}
	
	// configure method
	
}
