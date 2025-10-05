package sga.sgajwt.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import sga.sgajwt.JWT.JwtAuthFilter;
import sga.sgajwt.JWT.JwtAutorizationFilter;
import sga.sgajwt.JWT.JwtUtils;
import sga.sgajwt.Services.UserDetailsServiceImpl;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private JwtAutorizationFilter jwtAuthorization ;

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtUtils jwtUtils;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception{

     JwtAuthFilter jwtAuthFilter = new JwtAuthFilter(jwtUtils);  
     jwtAuthFilter.setAuthenticationManager(authenticationManager);

     return http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/**").permitAll()
                .anyRequest().authenticated()
            )
            // .addFilter(jwtAuthFilter)               
            // .exceptionHandling(exception -> exception
            // .authenticationEntryPoint(jwtComponentEntryPoint))           

            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilter(jwtAuthFilter)            
            .addFilterBefore(jwtAuthorization, UsernamePasswordAuthenticationFilter.class)
            .build();
    }

    // @Bean
    // UserDetailsService userDetailsService(){

    //     InMemoryUserDetailsManager userMemory = new InMemoryUserDetailsManager();
    //     userMemory.createUser(User.withUsername("wperez")
    //     .password(passwordEncoder().encode("Encrypted"))
    //     .roles()
    //     .build());

    //     return userMemory;

    // }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(HttpSecurity httpSecurity,PasswordEncoder passwordEncoder) throws Exception{

            return httpSecurity.getSharedObject(AuthenticationManagerBuilder.class)
            .userDetailsService(userDetailsService)
            .passwordEncoder(passwordEncoder)
            .and()
            .build();

    }
   

}
