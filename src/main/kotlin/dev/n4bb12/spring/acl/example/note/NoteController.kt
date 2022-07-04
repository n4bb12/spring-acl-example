package dev.n4bb12.spring.acl.example.note

import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller

@Controller
class NoteController(val service: NoteService) {

  @QueryMapping
  fun notes(): List<Note> = service.getAllNotes()

  @QueryMapping
  fun note(@Argument id: String): Note? = service.getNoteById(id)

  @MutationMapping
  fun updateNote(@Argument id: String, @Argument text: String): Note? = service.updateNote(Note(id, text))

}
