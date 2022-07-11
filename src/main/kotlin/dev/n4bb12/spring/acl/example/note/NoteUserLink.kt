package dev.n4bb12.spring.acl.example.note

import dev.n4bb12.spring.acl.example.user.User

data class NoteUserLink(
  val user: User,
  val type: NoteUserLinkType,
)
