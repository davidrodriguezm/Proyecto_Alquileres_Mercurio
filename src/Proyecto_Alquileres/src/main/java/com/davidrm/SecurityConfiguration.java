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

/*
 * CLASE DONDE ESTABLECEREMOS LA CONFIGURACION DE
 * AUTENTIFICACION - CÓMO ACCEDO
 * AUTORIZACION - A QUÉ PUEDO ACCEDER
 * MÉTODO DE ENCRIPTACIÓN DE LAS CONTRASEÑAS
 */

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter{
	/* Obtengo una refencia al SINGLENTON del userDetailsService	 * 
	 */
	@Autowired
	JPAUserDetailsService userDetailsService;
	
	/* MÉTODO PARA AUTENTIFICAR LOS USUARIOS */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		//La autentificación JPA no está incluido tenemos que configurarla nosotros
		//Creando nuestro propio servicio que nos permita obtener la información del usuario
		auth.userDetailsService(userDetailsService);
	}

	/*
	 * MÉTODO PARA ESTABLECER AUTORIZACION - A QUÉ PUEDO ACCEDER
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {					 
			/* URL con información sobre ANT MATCHERS
			 * https://www.baeldung.com/spring-security-expressions */
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
				.logoutUrl("/perform_logout")
				.deleteCookies("JSESSIONID")
				.logoutSuccessUrl("/");
	}
	
	@Bean
    public PasswordEncoder getPasswordEncoder() {         
		return new BCryptPasswordEncoder(15);
    }
	
}
