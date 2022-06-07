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
			.csrf().disable()
			.authorizeRequests()
			
			.antMatchers("/","/login-user","/usuario-add","/vehiculos").permitAll()
			
			.antMatchers("/alquiler-add","/alquiler-edit","/vehiculo-add","/vehiculo-edit",
					"/vehiculo-delete","/usuarios","/usuarios-activo").hasRole("ADMIN")
			
			.antMatchers("/alquiler-cliente-add").hasRole("USER")
			
			.antMatchers("/logout-user","/usuario-edit","/alquileres").authenticated()
			
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
