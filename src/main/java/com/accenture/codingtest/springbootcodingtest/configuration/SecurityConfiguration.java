package com.accenture.codingtest.springbootcodingtest.configuration;

import com.accenture.codingtest.springbootcodingtest.enums.UserRole;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/**
 * @author Parag Sansare
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .inMemoryAuthentication()
                .withUser("admin").password(passwordEncoder().encode("admin123")).roles(UserRole.ADMIN.toString())
                .and()
                .withUser("p_owner1").password(passwordEncoder().encode("p_owner123")).roles(UserRole.PRODUCT_OWNER.toString())
                .and()
                .withUser("p_owner2").password(passwordEncoder().encode("p_owner456")).roles(UserRole.PRODUCT_OWNER.toString())
                .and()
                .withUser("user1").password(passwordEncoder().encode("user123")).roles(UserRole.USER.toString())
                .and()
                .withUser("user2").password(passwordEncoder().encode("user456")).roles(UserRole.USER.toString());
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http
                .csrf().disable()
                .httpBasic()
                .and()
                .authorizeRequests()
                .antMatchers("/api/v1/users/**").hasRole(UserRole.ADMIN.toString())
                .antMatchers(HttpMethod.POST, "/api/v1/projects").hasRole(UserRole.PRODUCT_OWNER.toString())
                .antMatchers(HttpMethod.POST, "/api/v1/projects/").hasRole(UserRole.PRODUCT_OWNER.toString())
                .antMatchers(HttpMethod.DELETE, "/api/v1/projects/**").hasRole(UserRole.PRODUCT_OWNER.toString())
                .antMatchers(HttpMethod.POST, "/api/v1/tasks").hasRole(UserRole.PRODUCT_OWNER.toString())
                .antMatchers(HttpMethod.POST, "/api/v1/tasks/").hasRole(UserRole.PRODUCT_OWNER.toString())
                .antMatchers(HttpMethod.DELETE, "/api/v1/tasks/**").hasRole(UserRole.PRODUCT_OWNER.toString())
                .anyRequest()
                .authenticated()
                .and()
                .formLogin();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
