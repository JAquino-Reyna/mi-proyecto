package com.studyprogress.service;

public interface EmailService {
    void send(String recipient, String heading, String message);
}
