package com.secure.notes.jwt;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class LoginRequest {
    private String userName;
    private String password;
}
