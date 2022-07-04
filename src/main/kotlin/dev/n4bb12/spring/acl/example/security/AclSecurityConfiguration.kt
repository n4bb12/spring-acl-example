package dev.n4bb12.spring.acl.example.security

import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.concurrent.ConcurrentMapCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler
import org.springframework.security.acls.AclPermissionCacheOptimizer
import org.springframework.security.acls.AclPermissionEvaluator
import org.springframework.security.acls.domain.AclAuthorizationStrategy
import org.springframework.security.acls.domain.AclAuthorizationStrategyImpl
import org.springframework.security.acls.domain.AuditLogger
import org.springframework.security.acls.domain.ConsoleAuditLogger
import org.springframework.security.acls.domain.DefaultPermissionGrantingStrategy
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
    const val cacheName = "aclCache"
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
    return DefaultPermissionGrantingStrategy(auditLogger())
  }

  /**
   * Determines whether a principal is allowed to manage ACLs.
   */
  @Bean
  fun aclAuthorizationStrategy(): AclAuthorizationStrategy {
    return AclAuthorizationStrategyImpl(SimpleGrantedAuthority(Permission.ACL_MANAGE.name))
  }

  @Bean
  fun cacheManager(): CacheManager {
    return ConcurrentMapCacheManager(cacheName)
  }

  @Bean
  fun aclCache(): AclCache {
    return SpringCacheBasedAclCache(
      cacheManager().getCache(cacheName), permissionGrantingStrategy(), aclAuthorizationStrategy()
    )
  }

  /**
   * Used for reading from ACL tables.
   */
  @Bean
  fun lookupStrategy(): LookupStrategy {
    val lookupStrategy = BasicLookupStrategy(dataSource, aclCache(), aclAuthorizationStrategy(), auditLogger())
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
   * Adds ACL support to the default method security expression handler.
   */
  @Bean
  fun defaultMethodSecurityExpressionHandler(): MethodSecurityExpressionHandler? {
    val expressionHandler = DefaultMethodSecurityExpressionHandler()
    val permissionEvaluator = AclPermissionEvaluator(mutableAclService())
    expressionHandler.setPermissionEvaluator(permissionEvaluator)
    expressionHandler.setPermissionCacheOptimizer(AclPermissionCacheOptimizer(mutableAclService()))
    return expressionHandler
  }

}
