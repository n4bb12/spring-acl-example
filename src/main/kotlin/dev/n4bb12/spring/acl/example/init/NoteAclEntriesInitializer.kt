package dev.n4bb12.spring.acl.example.init

import dev.n4bb12.spring.acl.example.note.Note
import dev.n4bb12.spring.acl.example.user.User
import org.springframework.security.acls.model.MutableAcl

interface NoteAclEntriesInitializer {

  fun insertAces(acl: MutableAcl, note: Note, user: User)

}
