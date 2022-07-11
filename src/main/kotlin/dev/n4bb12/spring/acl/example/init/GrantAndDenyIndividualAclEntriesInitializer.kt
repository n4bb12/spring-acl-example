package dev.n4bb12.spring.acl.example.init

import dev.n4bb12.spring.acl.example.note.Note
import dev.n4bb12.spring.acl.example.permission.Permission
import dev.n4bb12.spring.acl.example.user.User
import org.springframework.security.acls.domain.PrincipalSid
import org.springframework.security.acls.model.MutableAcl

class GrantAndDenyIndividualAclEntriesInitializer : NoteAclEntriesInitializer {
  override fun insertAces(acl: MutableAcl, note: Note, user: User) {
    Permission.values().filter { it.mask > 0 }.forEach { permission ->
      val grant = user.permissions.contains(permission)
      println("[ACL]   On Note(id=${note.id}) ${if (grant) "grant" else "deny"} ${permission.aclPermission} to User(name=${user.name})")
      acl.insertAce(acl.entries.size, permission.aclPermission, PrincipalSid(user.name), grant)
    }
  }

}
