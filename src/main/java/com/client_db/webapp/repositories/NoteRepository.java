package com.client_db.webapp.repositories;


import com.client_db.webapp.models.Note;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findByClientId(Long clientId);
}
