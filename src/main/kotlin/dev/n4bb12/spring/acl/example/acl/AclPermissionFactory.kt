package dev.n4bb12.spring.acl.example.acl

import dev.n4bb12.spring.acl.example.permission.Permission
import org.springframework.security.acls.domain.PermissionFactory

class AclPermissionFactory : PermissionFactory {
  override fun buildFromMask(mask: Int): AclPermission {
    TODO("Not yet implemented")
  }

  override fun buildFromName(name: String?): AclPermission {
    return Permission.values().firstOrNull { it.name == name } ?: throw IllegalArgumentException(name)
  }

  override fun buildFromNames(names: MutableList<String>?): MutableList<AclPermission> {
    TODO("Not yet implemented")
  }
}
