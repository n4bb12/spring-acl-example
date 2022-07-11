INSERT INTO acl_sid (id, principal, sid) VALUES
  (1, TRUE, 'anonymous'),
  (2, TRUE, 'viewer'),
  (3, TRUE, 'editor'),
  (4, TRUE, 'admin');

INSERT INTO acl_class (id, class, class_id_type) VALUES
  (1, 'dev.n4bb12.spring.acl.example.note.Note', 'java.lang.String');
