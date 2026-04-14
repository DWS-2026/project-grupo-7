package com.rayokross.academy.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


	@Autowired
	public RepositoryUserDetailsService userDetailService;


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
	@Order(1)
	public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {


		http
				.securityMatcher("/api/**");
				//.exceptionHandling(handling -> handling.authenticationEntryPoint(unauthorizedHandlerJwt));

		http
				.authorizeHttpRequests(authorize -> authorize
						// PRIVATE ENDPOINTS
						// Images
						//.requestMatchers(HttpMethod.PUT, "/api/images/*/media").hasRole("USER")
						//.requestMatchers(HttpMethod.DELETE, "/api/books/*/images/*").hasRole("USER")
						// Books
						//.requestMatchers(HttpMethod.POST, "/api/books/**").hasRole("USER")
						//.requestMatchers(HttpMethod.PUT, "/api/books/**").hasRole("USER")
						//.requestMatchers(HttpMethod.DELETE, "/api/books/**").hasRole("ADMIN")
						// Shops
						//.requestMatchers(HttpMethod.PUT, "/api/shops/**").hasRole("ADMIN")
						//.requestMatchers(HttpMethod.PUT, "/api/shops/**").hasRole("ADMIN")
						//.requestMatchers(HttpMethod.DELETE, "/api/shops/**").hasRole("ADMIN")
						// PUBLIC ENDPOINTS
						.anyRequest().permitAll());

		// Disable Form login Authentication
		http.formLogin(formLogin -> formLogin.disable());

		// Disable CSRF protection (it is difficult to implement in REST APIs)
		http.csrf(csrf -> csrf.disable());

		// Disable Basic Authentication
		http.httpBasic(httpBasic -> httpBasic.disable());

		// Stateless session
		http.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		// Add JWT Token filter
		//http.addFilterBefore(new JwtRequestFilter(userDetailService, jwtTokenProvider),
				//UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

    @Order(2)
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/", "/courses", "/courses/**", "/course/**", "/login", "/register", "/error")
                .permitAll()
                .requestMatchers("/css/**", "/js/**", "/images/**", "/assets/**").permitAll()
                .requestMatchers("/profile").hasAnyRole("USER", "ADMIN")
                .requestMatchers("/cart/**", "checkout", "lessons/**").hasRole("USER")
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated());

        http.formLogin(formLogin -> formLogin
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/", true)
                .failureUrl("/login?error")
                .permitAll());

        http.logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .permitAll());

        return http.build();
    }
}