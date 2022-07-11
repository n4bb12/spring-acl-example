package dev.n4bb12.spring.acl.example.init

import dev.n4bb12.spring.acl.example.note.Note
import dev.n4bb12.spring.acl.example.user.User
import org.springframework.security.acls.domain.CumulativePermission
import org.springframework.security.acls.domain.PrincipalSid
import org.springframework.security.acls.model.MutableAcl

// The cumulative mask is not compatible with the DefaultPermissionGrantingStrategy because it does an equals comparison
// instead of only looking at the relevant bits: `ace.getPermission().getMask() == p.getMask()`
// In order to make it work, a custom PermissionGrantingStrategy needs ot be used.
class CumulativeAclEntriesInitializer : NoteAclEntriesInitializer {
  override fun insertAces(acl: MutableAcl, note: Note, user: User) {
    val cumulativePermission = CumulativePermission()
    user.permissions.forEach { cumulativePermission.set(it) }

    if (cumulativePermission.mask > 0) {
      println("[ACL]   On Note(id=${note.id}) grant $cumulativePermission to User(name=${user.name})")
      acl.insertAce(acl.entries.size, cumulativePermission, PrincipalSid(user.name), true)
    }
  }
}
