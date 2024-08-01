package com.acsa.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EmailServiceImplTest {

    @Mock
    private JavaMailSender mailSender;

    @Spy
    @InjectMocks
    private EmailServiceImpl emailService;

    @Mock
    private MimeMessage mimeMessage;

    @Mock
    private MimeMessageHelper mimeMessageHelper;

    @Mock
    private MultipartFile multipartFile;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
    }


    @Test
    void testSendSimpleMail_Success()throws MessagingException {
        //Arrange - for methods that return void, we start with doNothing() unlike other methods that have return types
        doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        //Act
        emailService.sendSimpleMail("test@acsa.com", "test subject", "test Text");

        //Assert
        verify(mailSender,times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    void testSendSimpleMail_Failure() {
        // Arrange
        doThrow(new RuntimeException("SMTP error")).when(mailSender).send(any(SimpleMailMessage.class));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            emailService.sendSimpleMail("to@example.com", "Test Subject", "Test Text");
        });

        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    void testSendHtmlMail_Success() throws MessagingException {
        // Arrange
        doNothing().when(mailSender).send(any(MimeMessage.class));

        // Act
        emailService.sendHtmlMail("to@example.com", "Test Subject", "<h1>Test HTML Content</h1>");

        // Assert
        verify(mailSender, times(1)).send(any(MimeMessage.class));
    }

    @Test
    public void testSendHtmlMail_Failure() {
        // Arrange
        doThrow(new RuntimeException("SMTP error")).when(mailSender).send(any(MimeMessage.class));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            emailService.sendHtmlMail("to@example.com", "Test Subject", "<h1>Test HTML Content</h1>");
        });

        verify(mailSender, times(1)).send(any(MimeMessage.class));
    }

    @Test
    public void testSendEmailUsingTextTemplate_Success() throws MessagingException, IOException {
        // Arrange
        doNothing().when(mailSender).send(any(MimeMessage.class));
        Map<String, String> templateModel = new HashMap<>();
        templateModel.put("name", "John Doe");

        // Mock the template loading
        doReturn("Hello {{name}},\nThis is a test email.").when(emailService).loadTemplate();

        // Act
        emailService.sendEmailUsingTextTemplate("to@example.com", "Test Subject", templateModel);

        // Assert
        verify(mailSender, times(1)).send(any(MimeMessage.class));
    }

    @Test
    public void testSendEmailUsingTextTemplate_Failure() throws IOException {
        // Arrange
        doThrow(new RuntimeException("SMTP error")).when(mailSender).send(any(MimeMessage.class));
        Map<String, String> templateModel = new HashMap<>();
        templateModel.put("name", "John Doe");

        // Mock the template loading
        doReturn("Hello {{name}},\nThis is a test email.").when(emailService).loadTemplate();

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            emailService.sendEmailUsingTextTemplate("to@example.com", "Test Subject", templateModel);
        });

        verify(mailSender, times(1)).send(any(MimeMessage.class));
    }


    @Test
    public void testSendAttachmentsMail_Success() throws MessagingException, IOException {
        // Arrange
        doNothing().when(mailSender).send(any(MimeMessage.class));

        MultipartFile[] files = new MultipartFile[]{multipartFile};
        when(multipartFile.getOriginalFilename()).thenReturn("test.txt");

        // Act
        emailService.sendAttachmentsMail("to@example.com", new String[]{"cc@example.com"}, new String[]{"bcc@example.com"}, "Test Subject", "Test Text", files);

        // Assert
        verify(mailSender, times(1)).send(any(MimeMessage.class));
    }

    @Test
    public void testSendAttachmentsMail_Failure() {
        // Arrange
        doThrow(new RuntimeException("SMTP error")).when(mailSender).send(any(MimeMessage.class));

        MultipartFile[] files = new MultipartFile[]{multipartFile};
        when(multipartFile.getOriginalFilename()).thenReturn("test.txt");

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            emailService.sendAttachmentsMail("to@example.com", new String[]{"cc@example.com"}, new String[]{"bcc@example.com"}, "Test Subject", "Test Text", files);
        });

        verify(mailSender, times(1)).send(any(MimeMessage.class));
    }

}