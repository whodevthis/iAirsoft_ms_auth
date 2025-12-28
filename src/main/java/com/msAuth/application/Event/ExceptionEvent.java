package com.msAuth.application.Event;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor

public class ExceptionEvent {
    private String username;
    private ExceptionAction action; // LOGIN_FAIL, LOGIN_SUCCESS, etc.
    private String ip;
    private long timestamp;

}
