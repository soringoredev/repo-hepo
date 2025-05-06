package com.client_db.webapp.service;

import com.client_db.webapp.models.Note;
import com.client_db.webapp.repositories.NoteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {

    private final NoteRepository noteRepo;

    public NoteService(NoteRepository noteRepo) {
        this.noteRepo = noteRepo;
    }

    public List<Note> findByClientId(Long clientId) {
        return noteRepo.findByClientId(clientId);
    }

    public void saveNote(Note note) {
        noteRepo.save(note);
    }

    public void deleteNoteById(Long id) {
        noteRepo.deleteById(id);
    }

}
