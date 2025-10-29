package com.mylaptop.org.config;

import com.mylaptop.org.security.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.*;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired private CustomUserDetailsService userDetailsService;
    @Autowired private JwtAuthenticationEntryPoint jwtAuthEntryPoint;
    @Autowired private JwtAuthenticationFilter jwtFilter;

//    private static final String[] PUBLIC_URLS = {
//            "/api/auth/**",
//            "/swagger-ui/**", "/v3/api-docs/**"
//    };
    private static final String[] PUBLIC_URLS = {
    		"/api/auth/**","/api/home/**","/api/admin/**","/api/user/**",
    		"/swagger-ui/**", "/v3/api-docs/**"
    };

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    // simple permissive CORS config for dev - adjust production settings
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cfg = new CorsConfiguration();
        cfg.setAllowedOriginPatterns(List.of("*"));
        cfg.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS"));
        cfg.setAllowedHeaders(List.of("*"));
//        cfg.addAllowedHeader("Authorization");
		cfg.addAllowedHeader("Content-Type");
		cfg.addAllowedHeader("Accept");
        cfg.setAllowCredentials(true);
        var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cfg);
        
        return source;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
            .authorizeHttpRequests()
               .antMatchers(PUBLIC_URLS).permitAll()
//               .antMatchers("/api/admin/**").hasRole("ADMIN")
//               .antMatchers("/api/user/**").hasAnyRole("USER", "ADMIN")

               .anyRequest().authenticated()
            .and()
               .exceptionHandling().authenticationEntryPoint(jwtAuthEntryPoint)
            .and()
               .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
