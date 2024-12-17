package com.secure.notes.services;

import com.secure.notes.models.ResponseObject;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;

public interface NoteService {
    ResponseEntity<ResponseObject> createNoteForUser(UserDetails userDetails, String content);

    ResponseEntity<ResponseObject> updateNoteForUser(Long noteId, UserDetails userDetails, String content);

    ResponseEntity<ResponseObject> deleteNoteForUser(Long noteId, UserDetails userDetails);

    ResponseEntity<ResponseObject> getNotesForUser(UserDetails userDetails);

}
