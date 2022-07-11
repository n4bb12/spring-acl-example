package dev.n4bb12.spring.acl.example.acl

import org.springframework.security.acls.domain.AuditLogger
import org.springframework.security.acls.domain.DefaultPermissionGrantingStrategy
import org.springframework.security.acls.model.AccessControlEntry
import org.springframework.security.acls.model.Permission

class CumulativePermissionGrantingStrategy(
  logger: AuditLogger,
) : DefaultPermissionGrantingStrategy(logger) {

  override fun isGranted(ace: AccessControlEntry, permission: Permission): Boolean {
    return ace.permission.mask and permission.mask === 1
  }

}
