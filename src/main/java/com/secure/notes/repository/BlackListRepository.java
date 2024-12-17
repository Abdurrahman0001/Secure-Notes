package com.secure.notes.repository;

import com.secure.notes.entities.BlackListedToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlackListRepository extends JpaRepository<BlackListedToken,Long> {
    boolean existsByToken(String token);
}
