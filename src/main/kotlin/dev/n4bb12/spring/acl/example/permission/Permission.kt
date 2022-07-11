package dev.n4bb12.spring.acl.example.permission

import org.springframework.security.acls.domain.BasePermission
import org.springframework.security.acls.domain.CumulativePermission

typealias AclPermission = org.springframework.security.acls.model.Permission

val ACL_NONE = CumulativePermission()

enum class Permission(val aclPermission: AclPermission) : AclPermission by aclPermission {
  NONE(ACL_NONE),
  NOTE_CREATE(ACL_NONE),
  NOTE_VIEW(BasePermission.READ),
  NOTE_EDIT(BasePermission.WRITE),
  NOTE_DELETE(BasePermission.DELETE),
  NOTE_VIEW_ALL(ACL_NONE),
  NOTE_EDIT_ALL(ACL_NONE),
  NOTE_DELETE_ALL(ACL_NONE),
  ACL_MANAGE(ACL_NONE),
  ;
}
