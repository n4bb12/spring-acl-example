# Spring ACL Example

> Work in Progress

## How to use

### Authentication

Authenticate as a certain user with the X-User header.
Valid users are `admin`, `regular`, `anonymous` (default)

```json
{
  "X-User": "admin"
}
```

Check who you are with

```gql
query Me {
  me {
    name
    permissions
  }
}
```

### Accessing Notes

```gql
query Notes {
  notes {
    id
    text
  }
  note1: note(id: "note-1") {
    id
    text
  }
}
```

### Updating Notes

```gql
mutation UpdateNote {
  updateNote(id: "note-1", text: "Note 1 - Edited") {
    id
    text
  }
}
```

## Docs

- https://docs.spring.io/spring-security/reference/servlet/authorization/acls.html
- https://docs.spring.io/spring-security/reference/servlet/authorization/expression-based.html
- https://www.baeldung.com/spring-security-acl
- https://github.com/spring-projects/spring-security-samples/tree/5.7.x/servlet/xml/java/contacts
- https://github.com/spring-projects/spring-security-samples/tree/5.7.x/servlet/xml/java/dms

## Not included yet

- Mapping Domain permissions to BasePermission
- Example for creating/deleting ACLs on entity create/delete
- Example for creating/deleting ACEs on linked user add/remove
- Example for using only granted authority (short-circuiting ACLs)
- No caching
