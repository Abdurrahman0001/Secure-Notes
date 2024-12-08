package com.secure.notes.services;

import com.secure.notes.models.Note;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface NoteService {
    Note createNoteForUser(String username, String content);
    Note updateNoteForUser(Long noteId,String username, String content);
    void deleteNoteForUser(Long noteId,String username);
    List<Note> getNotesForUser(String username);

}
