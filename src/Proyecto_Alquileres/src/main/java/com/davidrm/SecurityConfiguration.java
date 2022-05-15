package com.davidrm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.davidrm.services.JPAUserDetailsService;


@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter{

	@Autowired
	JPAUserDetailsService userDetailsService;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
			http
			.csrf().disable()//logout con get
			.authorizeRequests()
			.antMatchers("/*").permitAll()
			.antMatchers("/admin").hasRole("ADMIN")
			.antMatchers("/user").hasRole("USER")
			.and()
			.formLogin()
				.loginPage("/login-user")
				.loginProcessingUrl("/perform_login")
				.defaultSuccessUrl("/", true)
				.failureUrl("/login-user?error=true")
				.and()
				.logout()
				.logoutUrl("/logout-user")
				.deleteCookies("JSESSIONID")
				.logoutSuccessUrl("/");
	}
	
	@Bean
    public PasswordEncoder getPasswordEncoder() {         
		return new BCryptPasswordEncoder(15);
    }
	
}
