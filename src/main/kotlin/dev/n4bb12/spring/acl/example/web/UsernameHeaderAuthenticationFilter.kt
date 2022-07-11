package dev.n4bb12.spring.acl.example.web

import dev.n4bb12.spring.acl.example.user.UserService
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class UsernameHeaderAuthenticationFilter(val userService: UserService) : OncePerRequestFilter() {
  override fun doFilterInternal(
    request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain
  ) {
    val name = request.getHeader("X-User")
    val user = userService.getUserByName(name) ?: userService.getUserByName(UserService.ANONYMOUS)!!

    val principal = user.id
    val credentials = null
    val authorities = user.permissions.map { SimpleGrantedAuthority(it.name) }

    SecurityContextHolder.getContext().authentication =
      UsernamePasswordAuthenticationToken(principal, credentials, authorities)

    filterChain.doFilter(request, response)
  }
}
