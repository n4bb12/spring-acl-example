package dev.n4bb12.spring.acl.example.acl

import dev.n4bb12.spring.acl.example.init.AclEntriesInitializer
import dev.n4bb12.spring.acl.example.note.NoteUserLink
import org.springframework.security.acls.domain.AclImpl
import org.springframework.security.acls.domain.ObjectIdentityImpl
import org.springframework.security.acls.model.MutableAclService
import org.springframework.transaction.annotation.Transactional
import kotlin.reflect.KClass

@Transactional
class AclService(
  private val mutableAclService: MutableAclService,
  private val aclEntriesInitializer: AclEntriesInitializer
) : MutableAclService by mutableAclService {

  fun updateAclWithAuditing(acl: AclImpl) {
    for ((index) in acl.entries.withIndex()) {
      acl.updateAuditing(index, true, true)
    }

    updateAcl(acl)
  }

  fun updateById(objectClass: KClass<*>, objectId: String, linkedUsers: List<NoteUserLink>) {
    val objectIdentity = ObjectIdentityImpl(objectClass.java, objectId)
    val acl = readAclById(objectIdentity) as AclImpl

    for (index in acl.entries.indices.reversed()) {
      acl.deleteAce(index)
    }

    linkedUsers.forEach { link ->
      aclEntriesInitializer.insertAces(acl, objectId, link.userId, link.type.permissions)
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
