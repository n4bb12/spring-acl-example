INSERT INTO acl_sid (id, principal, sid) VALUES
  (1, 1, 'anonymous'),
  (2, 1, 'regular'),
  (3, 1, 'admin');

INSERT INTO acl_class (id, class, class_id_type) VALUES
  (1, 'dev.n4bb12.spring.acl.example.note.Note', 'java.lang.String');

INSERT INTO acl_object_identity (
  id,
  object_id_class,
  object_id_identity,
  parent_object,
  owner_sid,
  entries_inheriting
) VALUES
  (1, 1, 'note-1', NULL, 3, 0),
  (2, 1, 'note-2', NULL, 3, 0),
  (3, 1, 'note-3', NULL, 3, 0);
