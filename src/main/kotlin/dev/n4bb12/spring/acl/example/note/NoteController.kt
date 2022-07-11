package dev.n4bb12.spring.acl.example.note

import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller

@Controller
class NoteController(val noteService: NoteService) {

  @QueryMapping
  fun notes(): List<Note> = noteService.getAll()

  @QueryMapping
  fun note(@Argument id: String): Note? = noteService.getById(id)

  @MutationMapping
  fun editNote(
    @Argument id: String, @Argument text: String
  ): Note = noteService.edit(Note(id, text))

  @MutationMapping
  fun createNote(@Argument text: String): Note = noteService.create(text)

  @MutationMapping
  fun deleteNote(@Argument id: String) = noteService.delete(id)

  @MutationMapping
  fun updateNoteUserLink(
    @Argument id: String, @Argument userIds: List<String>, @Argument linkType: NoteUserLinkType
  ) = noteService.updateNoteUserLink(id, userIds, linkType)

}
