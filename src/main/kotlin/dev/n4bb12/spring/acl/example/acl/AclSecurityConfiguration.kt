package dev.n4bb12.spring.acl.example.acl

import dev.n4bb12.spring.acl.example.init.AclEntriesInitializer
import dev.n4bb12.spring.acl.example.init.CumulativeAclEntriesInitializer
import dev.n4bb12.spring.acl.example.permission.Permission
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.support.NoOpCache
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler
import org.springframework.security.acls.AclPermissionEvaluator
import org.springframework.security.acls.domain.AclAuthorizationStrategy
import org.springframework.security.acls.domain.AclAuthorizationStrategyImpl
import org.springframework.security.acls.domain.AuditLogger
import org.springframework.security.acls.domain.ConsoleAuditLogger
import org.springframework.security.acls.domain.PermissionFactory
import org.springframework.security.acls.domain.SpringCacheBasedAclCache
import org.springframework.security.acls.jdbc.BasicLookupStrategy
import org.springframework.security.acls.jdbc.JdbcMutableAclService
import org.springframework.security.acls.jdbc.LookupStrategy
import org.springframework.security.acls.model.AclCache
import org.springframework.security.acls.model.MutableAclService
import org.springframework.security.acls.model.PermissionGrantingStrategy
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.core.authority.SimpleGrantedAuthority
import javax.sql.DataSource

/**
 * https://docs.spring.io/spring-security/reference/servlet/authorization/acls.html
 * https://docs.spring.io/spring-security/reference/modules.html#spring-security-acl
 * https://stackoverflow.com/questions/41706241/how-to-use-spring-security-acl-when-my-domain-identifiers-are-of-string-type
 */
@Configuration
@EnableCaching
@EnableGlobalMethodSecurity(prePostEnabled = true)
class AclSecurityConfiguration(val dataSource: DataSource) {

  companion object {
    const val alcCacheName = "aclCache"
  }

  /**
   * Logs decisions to grant or deny permissions.
   */
  @Bean
  fun auditLogger(): AuditLogger {
    return ConsoleAuditLogger()
  }

  /**
   * Implements the logic to decide whether a permission is granted to a particular SID by an ACL.
   */
  @Bean
  fun permissionGrantingStrategy(): PermissionGrantingStrategy {
//    return DefaultPermissionGrantingStrategy(auditLogger())
    return CumulativePermissionGrantingStrategy(auditLogger())
  }

  /**
   * Determines whether a principal is allowed to manage ACLs.
   */
  @Bean
  fun aclAuthorizationStrategy(): AclAuthorizationStrategy {
    return AclAuthorizationStrategyImpl(SimpleGrantedAuthority(Permission.ACL_MANAGE.name))
  }

  @Bean
  fun aclCache(): AclCache {
    return SpringCacheBasedAclCache(
      NoOpCache(alcCacheName), permissionGrantingStrategy(), aclAuthorizationStrategy()
    )
  }

  /**
   * Used for reading from ACL tables.
   */
  @Bean
  fun lookupStrategy(): LookupStrategy {
    val lookupStrategy =
      BasicLookupStrategy(dataSource, aclCache(), aclAuthorizationStrategy(), permissionGrantingStrategy())
    lookupStrategy.setAclClassIdSupported(true) // Enable support for String IDs
    return lookupStrategy
  }

  /**
   * Used for writing to ACL tables.
   */
  @Bean
  fun mutableAclService(): MutableAclService {
    val mutableAclService = JdbcMutableAclService(dataSource, lookupStrategy(), aclCache())
    mutableAclService.setAclClassIdSupported(true) // Enable support for String IDs
    return mutableAclService
  }

  /**
   * Maps permissions used in Spring security expressions to ACL permissions.
   */
  @Bean
  fun permissionFactory(): PermissionFactory {
    return AclPermissionFactory()
  }

  /**
   * Adds ACL support to the default method security expression handler.
   */
  @Bean
  fun defaultMethodSecurityExpressionHandler(): MethodSecurityExpressionHandler {
    val expressionHandler = DefaultMethodSecurityExpressionHandler()
    val permissionEvaluator = AclPermissionEvaluator(mutableAclService())
    permissionEvaluator.setPermissionFactory(permissionFactory())
    expressionHandler.setPermissionEvaluator(permissionEvaluator)
//    expressionHandler.setPermissionCacheOptimizer(AclPermissionCacheOptimizer(mutableAclService()))
    return expressionHandler
  }

  /**
   * A convenience wrapper around MutableAclService used by this example.
   */
  @Bean
  fun aclService(): AclService {
    return AclService(mutableAclService(), aclEntriesInitializer())
  }

  /**
   * Determines which ACL entries need to be created for a specific entity, user, and list of permissions.
   */
  @Bean
  fun aclEntriesInitializer(): AclEntriesInitializer {
    return CumulativeAclEntriesInitializer()
  }

}
