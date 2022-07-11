package dev.n4bb12.spring.acl.example.note

import dev.n4bb12.spring.acl.example.permission.Permission

enum class NoteUserLinkType(val permissions: List<Permission>) {
  VIEWER(listOf(Permission.NOTE_VIEW)),
  EDITOR(listOf(Permission.NOTE_VIEW, Permission.NOTE_EDIT, Permission.NOTE_CREATE, Permission.NOTE_DELETE)),
}
