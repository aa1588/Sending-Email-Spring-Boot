package com.acsa.controller;

import com.acsa.service.EmailService;
import jakarta.mail.MessagingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class EmailController {

    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    /*
     *  http://localhost:8083/api/simple?to=amrit_dev@gmail.com&subject=my subject&text=my content
     */
    @GetMapping("/simple")
    public String sendSimpleMail(
            @RequestParam("to") String to,
            @RequestParam("subject") String subject,
            @RequestParam("text") String text) {

        try{
            emailService.sendSimpleMail(to,subject,text);
            return "Email sent successfully";
        }catch (MessagingException e){
            return "Error sending email" + e.getMessage();
        }
    }

    /*
     *  http://localhost:8083/api/html?to=amrit_dev@gmail.com&subject=my subject&html=Hi <h3>AMRIT</h3><br>Find my <b>content</b>
     */
    @GetMapping("/html")
    public String sendMailWithHTML(
            @RequestParam("to") String to,
            @RequestParam("subject") String subject,
            @RequestParam("html") String html) {

        try{
            emailService.sendHtmlMail(to,subject,html);
            return "Email sent successfully";
        }catch (MessagingException e){
            return "Error sending email" + e.getMessage();
        }
    }

    /*
     *  http://localhost:8083/api/template?to=amrit_dev@gmail.com&subject=my subject&name=AMRIT
     */
    @PostMapping("/template")
    public String sendMailWithTextTemplate(
            @RequestParam("to") String to,
            @RequestParam("subject") String subject,
            @RequestParam("name") String name) {

        Map<String, String> templateModel = new HashMap<>();
        templateModel.put("subject", subject);
        templateModel.put("name", name);

        try{
            emailService.sendEmailUsingTextTemplate(to,subject,templateModel);
            return "Email sent successfully";
        }catch (MessagingException | IOException e){
            return "Error sending email" + e.getMessage();
        }
    }
    /*
     * Can upload files from postman
     * payload : goto Body, click form-data
     */
    @PostMapping("/attachment")
    public ResponseEntity<String> sendMailWithAttachment(
            @RequestParam("to") String to,
            @RequestParam(value = "cc", required = false) String[] cc,
            @RequestParam(value = "bcc", required = false) String[] bcc,
            @RequestParam("subject") String subject,
            @RequestParam("text") String text,
            @RequestParam("files") MultipartFile[] files) {

        try {
            System.out.println("Email with Attachment About to Send");

            emailService.sendAttachmentsMail(
                    to, cc, bcc, subject, text,files
            );

            return new ResponseEntity<String>("Email sent successfully", HttpStatus.OK);
        } catch (MessagingException | IOException e) {
            return new ResponseEntity<String>("Error sending email" + e.getMessage(), HttpStatus.CONFLICT);
        }
    }
}
