package com.example.notificationservice.consumers;

import com.example.notificationservice.dtos.EmailDto;
import com.example.notificationservice.utils.EmailUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.util.Properties;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class EmailNotificationConsumer {



    @Value("${from_email}")
    private String from_Email;
    @Value("${email_password}")
    private String email_password;
    private ObjectMapper objectMapper;
    public EmailNotificationConsumer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = {"send_email"},groupId = "Email_Group")
    public void sendEmail(String message) {
        EmailDto emailDto;
        try {
            emailDto=objectMapper.readValue(message, EmailDto.class);
        }
        catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Send email message : " + message);
        final String fromEmail = from_Email; //requires valid gmail id
        final String password = email_password; // correct password for gmail id
        final String toEmail = emailDto.getTo_email(); // can be any email id

        System.out.println("TLSEmail Start");
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host
        props.put("mail.smtp.port", "587"); //TLS Port
        props.put("mail.smtp.auth", "true"); //enable authentication
        props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS

        //create Authenticator object to pass in Session.getInstance argument
        Authenticator auth = new Authenticator() {
            //override the getPasswordAuthentication method
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        };
        Session session = Session.getInstance(props, auth);
        EmailUtil.sendEmail(session, toEmail,emailDto.getSubject(), emailDto.getBody());

    }

}
