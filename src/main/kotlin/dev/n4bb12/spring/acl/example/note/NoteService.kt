package dev.n4bb12.spring.acl.example.note

import dev.n4bb12.spring.acl.example.acl.AclService
import org.springframework.security.access.prepost.PostAuthorize
import org.springframework.security.access.prepost.PostFilter
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service
import javax.persistence.EntityNotFoundException

@Service
class NoteService(val repository: NoteRepository, val aclService: AclService) {

  @PostFilter("hasAuthority('NOTE_VIEW_ALL') or hasPermission(filterObject, 'NOTE_VIEW')")
  fun getAll(): List<Note> = repository.findAll()

  @PostAuthorize("hasAuthority('NOTE_VIEW_ALL') or hasPermission(returnObject, 'NOTE_VIEW')")
  fun getById(id: String): Note? = repository.findById(id).orElse(null)

  @PreAuthorize("hasAuthority('NOTE_CREATE')")
  fun create(text: String): Note {
    val note = repository.save(Note(text = text))
    aclService.create(Note::class, note.id)
    return note
  }

  @PreAuthorize("hasAuthority('NOTE_EDIT_ALL') or hasPermission(#note, 'NOTE_EDIT')")
  fun edit(note: Note): Note {
    return repository.save(note)
  }

  @PreAuthorize("hasAuthority('NOTE_DELETE_ALL') or hasAuthority('NOTE_DELETE')")
  fun delete(id: String): String {
    aclService.deleteById(Note::class, id)
    repository.deleteById(id)
    return id
  }

  @PreAuthorize("hasAuthority('NOTE_EDIT_ALL') or hasPermission(#note, 'NOTE_EDIT')")
  fun updateNoteUserLink(id: String, userIds: List<String>, linkType: NoteUserLinkType): Note {
    val note = getById(id) ?: throw EntityNotFoundException()

    val linkedUsers = note.linkedUsers.filter { it.type != linkType }.toMutableList()
    userIds.forEach { linkedUsers.add(NoteUserLink(it, linkType)) }
    aclService.updateById(Note::class, id, linkedUsers)

    return note
  }

}
