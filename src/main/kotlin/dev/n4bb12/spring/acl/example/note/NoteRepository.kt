package dev.n4bb12.spring.acl.example.note

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
class NoteRepository : CrudRepository<Note, String> {

  val notes = mutableListOf(
    Note("note-1", "Note 1"),
    Note("note-2", "Note 2"),
    Note("note-3", "Note 3"),
  )

  override fun <S : Note?> save(entity: S): S {
    if (entity == null) {
      return null as S
    }
    return findById(entity.id).orElse(null)?.also { it.text = entity.text } as S
  }

  override fun <S : Note?> saveAll(entities: MutableIterable<S>): MutableIterable<S> {
    TODO("Not yet implemented")
  }

  override fun findById(id: String): Optional<Note> {
    return Optional.ofNullable(notes.find { it.id == id })
  }

  override fun existsById(id: String): Boolean {
    TODO("Not yet implemented")
  }

  override fun findAll(): List<Note> {
    return notes
  }

  override fun count(): Long {
		TODO("Not yet implemented")
  }

  override fun deleteAll() {
    TODO("Not yet implemented")
  }

  override fun deleteAll(entities: MutableIterable<Note>) {
    TODO("Not yet implemented")
  }

  override fun deleteAllById(ids: MutableIterable<String>) {
    TODO("Not yet implemented")
  }

  override fun delete(entity: Note) {
    TODO("Not yet implemented")
  }

  override fun deleteById(id: String) {
    TODO("Not yet implemented")
  }

  override fun findAllById(ids: MutableIterable<String>): MutableIterable<Note> {
    TODO("Not yet implemented")
  }

}
