package dev.n4bb12.spring.acl.example.init

import dev.n4bb12.spring.acl.example.permission.Permission
import org.springframework.security.acls.model.MutableAcl

interface AclEntriesInitializer {

  fun insertAces(
    acl: MutableAcl,
    objectId: String,
    userId: String,
    permissions: List<Permission>,
  )

}
