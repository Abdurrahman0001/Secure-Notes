package com.secure.notes.services.impl;

import com.secure.notes.Utils.NotesUtil;
import com.secure.notes.constants.CommonConstants;
import com.secure.notes.entities.Note;
import com.secure.notes.exception.NoteNotFoundException;
import com.secure.notes.models.ResponseObject;
import com.secure.notes.models.StatusResponse;
import com.secure.notes.repository.NoteRepository;
import com.secure.notes.services.NoteService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class NoteServiceImpl implements NoteService {
    private final NoteRepository noteRepository;

    public NoteServiceImpl(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    @Override
    public ResponseEntity<ResponseObject> createNoteForUser(UserDetails userDetails, String content) {

        List<String> message;
        StatusResponse statusResponse;
        String username = userDetails.getUsername();

        Note createNote = new Note();
        createNote.setContent(content);
        createNote.setOwnerUsername(username);
        noteRepository.save(createNote);
        message = Collections.singletonList("Note save Successfully");
        statusResponse = NotesUtil.buildStatusResponse(message, CommonConstants.RESPONSE_SUCCESS_CODE, CommonConstants.RESPONSE_SUCCESS_CODE, null);
        return NotesUtil.buildResponseEntityObject(statusResponse, null);

    }

    @Override
    public ResponseEntity<ResponseObject> updateNoteForUser(Long noteId, UserDetails userDetails, String content) {

        List<String> message;
        StatusResponse statusResponse;


        Note dbNote = noteRepository.findById(noteId).orElseThrow(()
                -> new NoteNotFoundException("Note Not Found"));

        if (userDetails.getUsername().equals(dbNote.getOwnerUsername())) {
            dbNote.setContent(content);
            noteRepository.save(dbNote);

            message = Collections.singletonList("Note Update Successfully");
            statusResponse = NotesUtil.buildStatusResponse(message, CommonConstants.RESPONSE_SUCCESS_CODE, CommonConstants.RESPONSE_SUCCESS, null);

        } else {
            message = Collections.singletonList("You have no permission to update");
            statusResponse = NotesUtil.buildStatusResponse(message, CommonConstants.USER_ACCESS_DENIED_CODE, CommonConstants.RESPONSE_STATUS, null);
        }
        return NotesUtil.buildResponseEntityObject(statusResponse, null);
    }

    @Override
    public ResponseEntity<ResponseObject> deleteNoteForUser(Long noteId, UserDetails userDetails) {

        List<String> message;
        StatusResponse statusResponse;


        Optional<Note> dbNote = noteRepository.findById(noteId);

        if (dbNote.isPresent()) {

            if (userDetails.getUsername().equals(dbNote.get().getOwnerUsername())) {
                noteRepository.deleteById(noteId);
                message = Collections.singletonList("Note Delete Successfully");
                statusResponse = NotesUtil.buildStatusResponse(message, CommonConstants.RESPONSE_SUCCESS_CODE, CommonConstants.RESPONSE_STATUS, null);
                return NotesUtil.buildResponseEntityObject(statusResponse, null);
            } else {
                message = Collections.singletonList("Unauthorized User");
                statusResponse = NotesUtil.buildStatusResponse(message, CommonConstants.RESPONSE_SUCCESS_CODE, CommonConstants.INVALID_CREDENTIAL, null);
                return NotesUtil.buildResponseEntityObject(statusResponse, null);
            }
        }
        message = Collections.singletonList("Note is not present related to given id");
        statusResponse = NotesUtil.buildStatusResponse(message, CommonConstants.RESPONSE_SUCCESS_CODE, CommonConstants.RESPONSE_STATUS, null);
        return NotesUtil.buildResponseEntityObject(statusResponse, null);
    }

    @Override
    public ResponseEntity<ResponseObject> getNotesForUser(UserDetails userDetails) {
        List<String> message;
        StatusResponse statusResponse;

        List<Note> noteList = noteRepository.findByOwnerUsername(userDetails.getUsername());
        if (!noteList.isEmpty()) {
            message = Collections.singletonList("Data Fetched Successfully");
            statusResponse = NotesUtil.buildStatusResponse(message, CommonConstants.RESPONSE_SUCCESS_CODE, CommonConstants.RESPONSE_STATUS, null);
        } else {
            message = Collections.singletonList("Data is not in the database");
            statusResponse = NotesUtil.buildStatusResponse(message, CommonConstants.RESPONSE_SUCCESS_CODE, CommonConstants.NO_DATA, null);
            return NotesUtil.buildResponseEntityObject(statusResponse, null);
        }
        return NotesUtil.buildResponseEntityObject(statusResponse, noteList);
    }
}
