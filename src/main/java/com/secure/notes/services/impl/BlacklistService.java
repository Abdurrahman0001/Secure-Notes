package com.secure.notes.services.impl;

import com.secure.notes.models.BlackListedToken;
import com.secure.notes.repository.BlackListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class BlacklistService {

    @Autowired
    private BlackListRepository blacklistRepository;

    public void blacklistToken(String token, Date expirationDate) {
        BlackListedToken blacklistedToken = new BlackListedToken();
        blacklistedToken.setToken(token);
        blacklistedToken.setExpirationDate(expirationDate);
        blacklistRepository.save(blacklistedToken);
    }

    public boolean isTokenBlacklisted(String token) {
        return blacklistRepository.existsByToken(token);
    }
}

