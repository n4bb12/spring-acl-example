package dev.n4bb12.spring.acl.example.web

import dev.n4bb12.spring.acl.example.user.UserService
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service

@Service
class UserDetailsService(val userService: UserService) :
  org.springframework.security.core.userdetails.UserDetailsService {
  override fun loadUserByUsername(username: String?): UserDetails {
    val user = userService.getUserByName(username)
      ?: userService.getUserByName(UserService.ANONYMOUS)!!

    return User
      .withUsername(user.id)
      .password("")
      .authorities(*user.permissions.map { it.toString() }.toTypedArray())
      .build()
  }
}
