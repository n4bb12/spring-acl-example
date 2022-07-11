package dev.n4bb12.spring.acl.example.init

import dev.n4bb12.spring.acl.example.permission.Permission
import org.springframework.security.acls.domain.PrincipalSid
import org.springframework.security.acls.model.MutableAcl

class GrantAndDenyAclEntriesInitializer : AclEntriesInitializer {
  override fun insertAces(
    acl: MutableAcl,
    objectId: String,
    userId: String,
    permissions: List<Permission>,
  ) {
    Permission.values().filter { it.mask > 0 }.forEach { permission ->
      val grant = permissions.contains(permission)
      println("[ACL]   On object (id=${objectId}) ${if (grant) "grant" else "deny"} ${permission.aclPermission} to user (id=${userId})")
      acl.insertAce(acl.entries.size, permission.aclPermission, PrincipalSid(userId), grant)
    }
  }

}
