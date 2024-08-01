package com.acsa.service;

import jakarta.mail.MessagingException;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

public interface EmailService {

    //SendFailedException extends MessagingException
    public void sendSimpleMail(String to, String subject, String text) throws MessagingException;

    public void sendHtmlMail(String to, String subject, String htmlContent) throws MessagingException;

    public void sendEmailUsingTextTemplate(String to, String subject, Map<String, String> templateModel) throws MessagingException, IOException;

    public void sendAttachmentsMail(String to,
                                      String[] cc,
                                      String[] bcc,
                                      String subject,
                                      String text,
                                      MultipartFile[] files) throws MessagingException, IOException;

}
