package dev.n4bb12.spring.acl.example.note

data class NoteUserLink(
  val userId: String,
  val type: NoteUserLinkType,
)
