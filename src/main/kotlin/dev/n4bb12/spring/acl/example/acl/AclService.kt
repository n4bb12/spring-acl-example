package dev.n4bb12.spring.acl.example.acl

import org.springframework.security.acls.domain.AclImpl
import org.springframework.security.acls.domain.ObjectIdentityImpl
import org.springframework.security.acls.domain.PrincipalSid
import org.springframework.security.acls.model.MutableAclService
import org.springframework.transaction.annotation.Transactional
import kotlin.reflect.KClass

@Transactional
class AclService(private val mutableAclService: MutableAclService) : MutableAclService by mutableAclService {

  fun updateAclWithAuditing(acl: AclImpl) {
    for ((index) in acl.entries.withIndex()) {
      acl.updateAuditing(index, true, true)
    }

    updateAcl(acl)
  }

  fun updateById(objectClass: KClass<*>, objectId: String, principals: List<String>, aclPermission: AclPermission) {
    val objectIdentity = ObjectIdentityImpl(objectClass.java, objectId)
    val acl = readAclById(objectIdentity) as AclImpl

    for (index in acl.entries.indices.reversed()) {
      if (acl.entries[index].permission.mask == aclPermission.mask) {
        acl.deleteAce(index)
      }
    }

    principals.forEach {
      acl.insertAce(acl.entries.size, aclPermission, PrincipalSid(it), true)
    }

    updateAclWithAuditing(acl)
  }

  fun create(objectClass: KClass<*>, objectId: String) {
    val objectIdentity = ObjectIdentityImpl(objectClass.java, objectId)
    createAcl(objectIdentity)
  }

  fun deleteById(objectClass: KClass<*>, objectId: String) {
    val objectIdentity = ObjectIdentityImpl(objectClass.java, objectId)
    val deleteChildren = true
    deleteAcl(objectIdentity, deleteChildren)
  }

}
