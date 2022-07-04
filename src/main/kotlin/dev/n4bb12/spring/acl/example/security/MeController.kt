package dev.n4bb12.spring.acl.example.security

import dev.n4bb12.spring.acl.example.user.User
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller

@Controller
class MeController {

  @QueryMapping
  fun me(): User? {
    return SecurityContextHolder.getContext().authentication.principal as User?
  }

}
