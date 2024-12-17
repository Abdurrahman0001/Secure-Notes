package com.secure.notes.controller;

import com.secure.notes.models.ResponseObject;
import com.secure.notes.services.NoteService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequestMapping("/api/note")
public class NoteController {

    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @PostMapping("/save")
    public ResponseEntity<ResponseObject> savedNote(@RequestBody String content, @AuthenticationPrincipal UserDetails userDetails) {
        log.info("URL : {} , MethodType : {} , RequestBody : {} , Principal : {} ", "api/note/save", "POST", content, userDetails);
        ResponseEntity<ResponseObject> response = noteService.createNoteForUser(userDetails, content);
        log.info("URL : {} , MethodType : {} , RequestBody : {} , Principal : {} , ResponseBody : {} ", "/api/note/save", "POST", content, userDetails, response);
        return response;
    }

    @GetMapping("/get")
    public ResponseEntity<ResponseObject> getNote(@AuthenticationPrincipal UserDetails userDetails) {

        log.info("URL : {} , MethodType : {} , Principal : {} ", "api/note/get", "GET", userDetails);
        ResponseEntity<ResponseObject> response = noteService.getNotesForUser(userDetails);
        log.info("URL : {} , MethodType : {} , Principal : {} , ResponseBody : {} ", "/api/note/get", "GET", userDetails, response);
        return response;
    }

    @PutMapping("/{noteId}/update")
    public ResponseEntity<ResponseObject> updateNote(@PathVariable Long noteId, @RequestBody String content, @AuthenticationPrincipal UserDetails userDetails) {


        log.info("URL : {} , MethodType : {} , RequestBody : {} , Principal : {} , PathVariable  : {} ", "api/note/save", "POST", content, userDetails, noteId);
        ResponseEntity<ResponseObject> response = noteService.updateNoteForUser(noteId, userDetails, content);
        log.info("URL : {} , MethodType : {} , RequestBody : {} , Principal : {} , RequestBody : {} , ResponseBody : {} ", "/api/note/save", "POST", content, userDetails, noteId, response);
        return response;
    }

    @DeleteMapping("/{noteId}/delete")
    public ResponseEntity<ResponseObject> deleteNote(@PathVariable Long noteId, @AuthenticationPrincipal UserDetails userDetails) {


        log.info("URL : {} , MethodType : {} , PathVariable : {} , Principal : {} ", "api/note/noteID/delete", "DELETE", noteId, userDetails);
        ResponseEntity<ResponseObject> response = noteService.deleteNoteForUser(noteId, userDetails);
        log.info("URL : {} , MethodType : {} , PathVariable : {} , Principal : {} , ResponseBody : {} ", "/api/note/save", "POST", noteId, userDetails, response);
        return response;
    }
}
