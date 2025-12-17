package com.tuempresa.bodega.note;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
    // Aquí puedes agregar findByCategory si lo necesitas filtrar desde backend
}