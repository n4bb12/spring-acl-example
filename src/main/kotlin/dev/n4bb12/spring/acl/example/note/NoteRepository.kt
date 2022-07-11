package dev.n4bb12.spring.acl.example.note

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
class NoteRepository : CrudRepository<Note, String> {

  val notes = mutableListOf(
    Note(text = "Note 1"),
    Note(text = "Note 2"),
    Note(text = "Note 3"),
  )

  override fun <S : Note?> save(note: S): S {
    if (note == null) {
      throw IllegalArgumentException("Entity can not be null")
    }

    deleteById(note.id)
    notes.add(note.copy())
    return note
  }

  override fun <S : Note?> saveAll(entities: MutableIterable<S>): MutableIterable<S> {
    TODO("Not yet implemented")
  }

  override fun findById(id: String): Optional<Note> {
    return Optional.ofNullable(notes.find { it.id == id }?.copy())
  }

  override fun existsById(id: String): Boolean {
    TODO("Not yet implemented")
  }

  override fun findAll(): List<Note> {
    return notes.map { it.copy() }
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
    notes.removeIf { it.id == id }
  }

  override fun findAllById(ids: MutableIterable<String>): MutableIterable<Note> {
    TODO("Not yet implemented")
  }

}
