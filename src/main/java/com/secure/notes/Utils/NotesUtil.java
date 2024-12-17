package com.secure.notes.Utils;


import com.secure.notes.models.ResponseObject;
import com.secure.notes.models.StatusResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class NotesUtil {

    public static ResponseEntity<ResponseObject> buildResponseEntityObject(StatusResponse statusResponse, Object obj) {
        ResponseObject responseObject = new ResponseObject();
        responseObject.setObject(obj);
        responseObject.setTimestamp(LocalDateTime.now());
        responseObject.setCode(statusResponse.getCode());
        responseObject.setMessage(statusResponse.getMessage());
        responseObject.setError(statusResponse.getError());
        responseObject.setStatus(statusResponse.getStatus());
        return new ResponseEntity<>(responseObject, HttpStatus.OK);
    }


    public static StatusResponse buildStatusResponse(List<String> message, String code, String status, String error) {
        StatusResponse statusResponse = new StatusResponse();
        statusResponse.setMessage(message);
        statusResponse.setCode(code);
        statusResponse.setStatus(status);
        statusResponse.setError(error);

        return statusResponse;
    }

}
