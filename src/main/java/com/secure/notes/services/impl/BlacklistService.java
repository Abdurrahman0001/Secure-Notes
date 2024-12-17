package com.secure.notes.services.impl;

import com.secure.notes.Utils.NotesUtil;
import com.secure.notes.constants.CommonConstants;
import com.secure.notes.entities.BlackListedToken;
import com.secure.notes.models.ResponseObject;
import com.secure.notes.models.StatusResponse;
import com.secure.notes.repository.BlackListRepository;
import org.aspectj.weaver.ast.Not;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class BlacklistService {

    @Autowired
    private BlackListRepository blacklistRepository;

    public ResponseEntity<ResponseObject> blacklistToken(String token) {
        List<String> message;
        StatusResponse statusResponse;
        BlackListedToken blacklistedToken = new BlackListedToken();
        blacklistedToken.setToken(token);
        blacklistRepository.save(blacklistedToken);
        message = Collections.singletonList("Signout Successfully");
        statusResponse  = NotesUtil.buildStatusResponse(message, CommonConstants.RESPONSE_SUCCESS_CODE,CommonConstants.RESPONSE_SUCCESS,null);
        return NotesUtil.buildResponseEntityObject(statusResponse,null);
    }

    public boolean isTokenBlacklisted(String token) {
        return blacklistRepository.existsByToken(token);
    }
}

