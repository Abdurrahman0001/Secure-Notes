package com.secure.notes.models;

import lombok.Data;

import java.util.List;
@Data
public class StatusResponse {

    private List<String> message;
    private String status;
    private String code;
    private String error;

}
