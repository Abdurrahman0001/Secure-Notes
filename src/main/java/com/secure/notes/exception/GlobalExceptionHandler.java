package com.secure.notes.exception;

import com.secure.notes.Utils.NotesUtil;
import com.secure.notes.constants.CommonConstants;
import com.secure.notes.models.ResponseObject;
import com.secure.notes.models.StatusResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ResponseObject> handleUserUserNotFoundException(UserNotFoundException ex) {
        StatusResponse statusResponse;
        statusResponse = NotesUtil.buildStatusResponse(Collections.singletonList(ex.getMessage()), CommonConstants.RESPONSE_SUCCESS_CODE, CommonConstants.RESPONSE_SUCCESS, null);
        return NotesUtil.buildResponseEntityObject(statusResponse, null);

    }

    @ExceptionHandler(NoteNotFoundException.class)
    public ResponseEntity<ResponseObject> handleNoteNotFoundException(NoteNotFoundException ex) {
        StatusResponse statusResponse;
        statusResponse = NotesUtil.buildStatusResponse(Collections.singletonList(ex.getMessage()), CommonConstants.RESPONSE_SUCCESS_CODE, CommonConstants.NO_DATA, null);
        return NotesUtil.buildResponseEntityObject(statusResponse, null);

    }
}
