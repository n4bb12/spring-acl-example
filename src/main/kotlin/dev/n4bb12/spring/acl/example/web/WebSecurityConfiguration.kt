package dev.n4bb12.spring.acl.example.web

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.acls.model.AclService
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy.STATELESS
import org.springframework.security.web.SecurityFilterChain

/** https://docs.spring.io/spring-security/reference/servlet/integrations/mvc.html */
@Configuration
@EnableWebSecurity
class WebSecurityConfiguration(val aclService: AclService) {

  @Bean
  fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
    return http
      .httpBasic()
      .disable()
      .csrf()
      .disable()
      .formLogin()
      .disable()
      .logout()
      .disable()
      .sessionManagement()
      .sessionCreationPolicy(STATELESS)
      .and()
      .headers()
      .frameOptions()
      .sameOrigin()
      .and()
      .authorizeRequests()
      .anyRequest()
      .permitAll()
      .and()
      .build()
  }
}
