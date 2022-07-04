package dev.n4bb12.spring.acl.example.note

import org.springframework.security.access.prepost.PostAuthorize
import org.springframework.security.access.prepost.PostFilter
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service

@Service
class NoteService(val repository: NoteRepository) {

  @PostFilter("hasPermission(filterObject, 'READ')")
  fun getAllNotes(): List<Note> = repository.findAll()

  @PostAuthorize("hasPermission(returnObject, 'READ')")
  fun getNoteById(id: String): Note? = repository.findById(id).orElse(null)

  @PreAuthorize("hasPermission(#note, 'WRITE')")
  fun updateNote(input: Note): Note? = repository.save(input)

}
