package dev.n4bb12.spring.acl.example.note

import java.util.UUID

data class Note(
  val id: String = UUID.randomUUID().toString().split("-").first(),
  val text: String,
  val linkedUsers: List<NoteUserLink> = emptyList(),
)
