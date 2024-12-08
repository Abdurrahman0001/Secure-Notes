package com.secure.notes.services.impl;

import com.secure.notes.models.Note;
import com.secure.notes.repository.NoteRepository;
import com.secure.notes.services.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteServiceImpl implements NoteService {

    @Autowired
    NoteRepository noteRepository;

    @Override
    public Note createNoteForUser(String username, String content) {
        Note createNote = new Note();
        createNote.setContent(content);
        createNote.setOwnerUsername(username);
        return noteRepository.save(createNote);
    }

    @Override
    public Note updateNoteForUser(Long noteId, String username, String content) {
        Note dbNote = noteRepository.findById(noteId).orElseThrow(()
                -> new RuntimeException("Not not found"));

        Note savedNote;

        if (username.equals(dbNote.getOwnerUsername())) {
            dbNote.setContent(content);
            dbNote.setOwnerUsername(username);
            savedNote = noteRepository.save(dbNote);
        } else {
            throw new RuntimeException("you can't update");
        }
        return savedNote;
    }

    @Override
    public void deleteNoteForUser(Long noteId, String username) {
        Note dbNote = noteRepository.findById(noteId)
                .orElseThrow(() -> new RuntimeException("Write valid Id."));

        if (username.equals(dbNote.getOwnerUsername())) {
            noteRepository.deleteById(noteId);
        } else {
            throw new RuntimeException("You haven't permission to delete.");
        }
    }

    @Override
    public List<Note> getNotesForUser(String username) {
        return noteRepository.findByOwnerUsername(username);
    }
}
