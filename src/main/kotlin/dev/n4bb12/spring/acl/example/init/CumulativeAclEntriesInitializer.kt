package dev.n4bb12.spring.acl.example.init

import dev.n4bb12.spring.acl.example.permission.Permission
import org.springframework.security.acls.domain.CumulativePermission
import org.springframework.security.acls.domain.PrincipalSid
import org.springframework.security.acls.model.MutableAcl

/**
 * This strategy uses fewer rows.
 * It requires using the `CumulativePermissionGrantingStrategy`.
 */
class CumulativeAclEntriesInitializer : AclEntriesInitializer {
  override fun insertAces(acl: MutableAcl, objectId: String, userId: String, permissions: List<Permission>) {
    val cumulativePermission = CumulativePermission()
    permissions.forEach { cumulativePermission.set(it) }

    if (cumulativePermission.mask > 0) {
      println("[ACL]   On object (id=${objectId}) grant $cumulativePermission to user (id=${userId})")
      acl.insertAce(acl.entries.size, cumulativePermission, PrincipalSid(userId), true)
    }
  }
}
