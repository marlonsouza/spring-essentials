package ueh.marlon.springessentials.config;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ueh.marlon.springessentials.service.UehUserDetailsService;

@EnableWebSecurity
@Slf4j
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	private final UehUserDetailsService uehUserServiceDetails;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf()
			.disable()
			//.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
			.authorizeRequests()
			.antMatchers("/animes/admin/**")
			.hasRole("ADMIN")
			.antMatchers("/animes/**")
			.hasRole("USER")
			.antMatchers("/actuator/**")
			.permitAll()
			.anyRequest()
			.authenticated()
			.and()
			.formLogin()
			.and()
			.httpBasic();
	}
	
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
		log.info("Password encoder {}",passwordEncoder.encode("test"));
		
		auth.inMemoryAuthentication()
			.withUser("marlon")
			.password(passwordEncoder.encode("PHSouza"))
			.roles("USER")
			.and()
			.withUser("ketlyn")
			.password(passwordEncoder.encode("PHSouza"))
			.roles("USER","ADMIN");
		
		auth.userDetailsService(uehUserServiceDetails)
			.passwordEncoder(passwordEncoder);
			
	}
}
