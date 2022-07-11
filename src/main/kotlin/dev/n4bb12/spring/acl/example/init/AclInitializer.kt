package dev.n4bb12.spring.acl.example.init

import dev.n4bb12.spring.acl.example.acl.AclService
import dev.n4bb12.spring.acl.example.note.Note
import dev.n4bb12.spring.acl.example.note.NoteRepository
import dev.n4bb12.spring.acl.example.permission.Permission
import dev.n4bb12.spring.acl.example.user.UserService
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.security.acls.domain.AclImpl
import org.springframework.security.acls.domain.ObjectIdentityImpl
import org.springframework.security.acls.domain.PrincipalSid
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class AclInitializer(
  val aclService: AclService,
  val noteRepository: NoteRepository,
  val userService: UserService,
) : ApplicationRunner {

  @Transactional
  override fun run(args: ApplicationArguments?) {
    SecurityContextHolder.getContext().authentication = UsernamePasswordAuthenticationToken(
      UserService.ADMIN, null, listOf(SimpleGrantedAuthority(Permission.ACL_MANAGE.name))
    )

    val notes = noteRepository.findAll()
    val users = userService.getAllUsers()
    val owner = PrincipalSid(UserService.ADMIN)
    val entriesInitializer = CumulativeAclEntriesInitializer()

    println("[ACL] AclInitializer")

    println("[ACL] Notes:")
    notes.forEach { println("[ACL]   $it") }

    println("[ACL] Users:")
    users.forEach { println("[ACL]   $it") }

    println("[ACL] Entries:")
    notes.forEach { note ->
      val objectIdentity = ObjectIdentityImpl(Note::class.java, note.id)
      val acl = aclService.createAcl(objectIdentity) as AclImpl
      acl.owner = owner

      users.forEach { user ->
        entriesInitializer.insertAces(acl, note, user)
      }

      aclService.updateAclWithAuditing(acl)
    }
  }
}
