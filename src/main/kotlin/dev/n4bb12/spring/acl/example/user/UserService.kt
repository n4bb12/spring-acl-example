package dev.n4bb12.spring.acl.example.user

import dev.n4bb12.spring.acl.example.security.Permission.ACL_MANAGE
import dev.n4bb12.spring.acl.example.security.Permission.NOTE_EDIT
import dev.n4bb12.spring.acl.example.security.Permission.NOTE_VIEW
import org.springframework.stereotype.Service

@Service
class UserService {

  companion object {
    const val ANONYMOUS = "anonymous"
  }

  private val users = listOf(
    User(ANONYMOUS, listOf()),
    User("regular", listOf(NOTE_VIEW)),
    User("admin", listOf(NOTE_VIEW, NOTE_EDIT, ACL_MANAGE)),
  )

  fun getAllUsers(): List<User> = users

  fun getUserByName(name: String?): User? = users.find { it.name == name }

}
