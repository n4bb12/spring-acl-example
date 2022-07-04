package dev.n4bb12.spring.acl.example

import dev.n4bb12.spring.acl.example.note.Note
import dev.n4bb12.spring.acl.example.note.NoteRepository
import dev.n4bb12.spring.acl.example.security.Permission
import dev.n4bb12.spring.acl.example.user.UserService
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.security.acls.domain.BasePermission
import org.springframework.security.acls.domain.ObjectIdentityImpl
import org.springframework.security.acls.domain.PrincipalSid
import org.springframework.security.acls.model.MutableAcl
import org.springframework.security.acls.model.MutableAclService
import org.springframework.security.acls.model.NotFoundException
import org.springframework.security.acls.model.ObjectIdentity
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

@Component
class InitialData(
  val aclService: MutableAclService,
  val noteRepository: NoteRepository,
  val userService: UserService,
) : ApplicationRunner {

  fun getAcl(objectIdentity: ObjectIdentity): MutableAcl {
    return try {
      aclService.readAclById(objectIdentity) as MutableAcl
    } catch (e: NotFoundException) {
      aclService.createAcl(objectIdentity)
    }
  }

  override fun run(args: ApplicationArguments?) {
    SecurityContextHolder.getContext().authentication =
      UsernamePasswordAuthenticationToken("admin", null, listOf(SimpleGrantedAuthority(Permission.ACL_MANAGE.name)))

    println(">>> InitialData")
    println(">>> Notes ${noteRepository.findAll()}")
    println(">>> Users ${userService.getAllUsers()}")

    noteRepository.findAll().forEach { note ->
      val acl = getAcl(ObjectIdentityImpl(Note::class.java, note.id))

      userService.getAllUsers().forEach { user ->
        if (user.permissions.contains(Permission.NOTE_VIEW)) {
          println(">>> On Note ${note.id} grant Permission READ  to User ${user.name}")
          acl.insertAce(acl.entries.size, BasePermission.READ, PrincipalSid(user.name), true)
        }
        if (user.permissions.contains(Permission.NOTE_EDIT)) {
          println(">>> On Note ${note.id} grant Permission WRITE to User ${user.name}")
          acl.insertAce(acl.entries.size, BasePermission.WRITE, PrincipalSid(user.name), true)
        }
      }

      aclService.updateAcl(acl)
    }
  }
}
