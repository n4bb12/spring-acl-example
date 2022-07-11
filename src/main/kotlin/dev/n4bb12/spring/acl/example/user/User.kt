package dev.n4bb12.spring.acl.example.user

import dev.n4bb12.spring.acl.example.permission.Permission

data class User(val id: String, val permissions: List<Permission>)
