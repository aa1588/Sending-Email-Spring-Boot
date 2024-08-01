package com.acsa.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class EmailServiceImpl implements EmailService{

    private final JavaMailSender mailSender;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendSimpleMail(String to, String subject, String text) throws MessagingException {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@acsa.com"); // Not mandatory,some SMTP servers might reject
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }

    @Override
    public void sendHtmlMail(String to, String subject, String htmlContent) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom("noreply@acsa.com"); // Not mandatory,some SMTP servers might reject
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);
        mailSender.send(message);
    }

    @Override
    public void sendEmailUsingTextTemplate(String to, String subject, Map<String, String> templateModel) throws MessagingException, IOException {
        String text = loadTemplate();

        for (Map.Entry<String, String> entry : templateModel.entrySet()) {
            text = text.replace("{{" + entry.getKey() + "}}", entry.getValue());
        }

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom("noreply@acsa.com"); // Not mandatory,some SMTP servers might reject
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text, false);
        mailSender.send(message);
    }

    @Override
    public void sendAttachmentsMail(String to,
                                      String[] cc,
                                      String[] bcc,
                                      String subject,
                                      String text,
                                      MultipartFile[] files) throws MessagingException,IOException {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, (files!=null && files.length>0));

        helper.setFrom("noreply@acsa.com");
        helper.setTo(to);

        if(cc!=null && cc.length>0) {
            helper.setCc(cc);
        }

        if (bcc != null && bcc.length>0) {
            helper.setBcc(bcc);
        }

        helper.setSubject(subject);
        helper.setText(text, false);

        if (files != null && files.length > 0) {
            for (MultipartFile file : files) {
                helper.addAttachment(file.getOriginalFilename(), file);
            }
        }
        mailSender.send(message);
    }

    String loadTemplate() throws IOException {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new ClassPathResource("templates/emailTemplate.txt").getInputStream()))) {
            return reader.lines().collect(Collectors.joining("\n"));
        }
    }
}
