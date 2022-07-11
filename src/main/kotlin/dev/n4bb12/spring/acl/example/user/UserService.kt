package dev.n4bb12.spring.acl.example.user

import dev.n4bb12.spring.acl.example.permission.Permission.ACL_MANAGE
import dev.n4bb12.spring.acl.example.permission.Permission.NOTE_CREATE
import dev.n4bb12.spring.acl.example.permission.Permission.NOTE_DELETE
import dev.n4bb12.spring.acl.example.permission.Permission.NOTE_DELETE_ALL
import dev.n4bb12.spring.acl.example.permission.Permission.NOTE_EDIT
import dev.n4bb12.spring.acl.example.permission.Permission.NOTE_EDIT_ALL
import dev.n4bb12.spring.acl.example.permission.Permission.NOTE_VIEW
import dev.n4bb12.spring.acl.example.permission.Permission.NOTE_VIEW_ALL
import org.springframework.stereotype.Service

@Service
class UserService {

  companion object {
    const val ANONYMOUS = "anonymous"
    const val VIEWER = "viewer"
    const val EDITOR = "editor"
    const val ADMIN = "admin"
  }

  private val users = listOf(
    User(ANONYMOUS, emptyList()),
    User(VIEWER, listOf(NOTE_VIEW)),
    User(EDITOR, listOf(NOTE_VIEW, NOTE_EDIT, NOTE_DELETE, NOTE_CREATE)),
    User(ADMIN, listOf(ACL_MANAGE, NOTE_VIEW_ALL, NOTE_EDIT_ALL, NOTE_DELETE_ALL, NOTE_CREATE)),
  )

  fun getAllUsers(): List<User> = users

  fun getUserByName(name: String?): User? = users.find { it.id == name }

}
