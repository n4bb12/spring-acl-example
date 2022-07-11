package dev.n4bb12.spring.acl.example.note

import dev.n4bb12.spring.acl.example.acl.AclService
import org.springframework.security.access.prepost.PostAuthorize
import org.springframework.security.access.prepost.PostFilter
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service

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
    return repository.save(note)
  }

  @PreAuthorize("hasAuthority('NOTE_EDIT_ALL') or hasPermission(#note, 'NOTE_EDIT')")
  fun edit(note: Note): Note {
    return repository.save(note)
  }

  @PreAuthorize("hasAuthority('NOTE_DELETE_ALL') or hasAuthority('NOTE_DELETE')")
  fun delete(id: String) {
    aclService.deleteById(Note::class, id)
    repository.deleteById(id)
  }

  @PreAuthorize("hasAuthority('NOTE_EDIT_ALL') or hasPermission(#note, 'NOTE_EDIT')")
  fun updateNoteUserLink(
    id: String, userIds: List<String>, linkType: NoteUserLinkType
  ) {
    // TODO We don't know which link type caused which entries. This information is lost when entries are created.
    // To avoid cleaning up unrelated entries, we need to delete all entries for the note id and re-create them for
    // all user links.
    linkType.permissions.forEach { permission ->
      aclService.updateById(Note::class, id, userIds, permission)
    }
  }

}
