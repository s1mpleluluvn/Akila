/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.akila.adapter.persistence.restService;

import java.io.IOException;
import java.util.Properties;
import jakarta.annotation.PostConstruct;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 *
 * @author minh
 */
@Component
@Log4j2
public class EmailService {

    private JavaMailSenderImpl mailSender;

    private final String hostName;
    private final int port;
    private final String username;
    private final String password;
    private final Boolean isEnableTest;

    public EmailService(@Value("${email.hostName}") String hostName,
            @Value("${email.port}") int port,
            @Value("${email.username}") String username,
            @Value("${email.password}") String password,
            @Value("${email.enable-test}") Boolean isEnableTest) throws IOException {

        this.hostName = hostName;
        this.port = port;
        this.username = username;
        this.password = password;
        this.isEnableTest = isEnableTest;
    }

    @PostConstruct
    private void init() {
        mailSender = new JavaMailSenderImpl();
        mailSender.setHost(hostName);
        mailSender.setPort(port);
        mailSender.setUsername(username);
        mailSender.setPassword(password);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.mime.charset", "utf-8");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.starttls.required", "true");
        props.put("mail.smtp.allow8bitmime", "true");
        props.put("mail.smtps.allow8bitmime", "true");
        if (isEnableTest == true) {
            props.put("mail.debug", "true");
        } else {
            props.put("mail.debug", "false");
        }
    }

    @Async("taskExecutor")
    public  void sendMessage(String from, String to, String subject, String text) throws Exception {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(from);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text);

        try {
            mailSender.send(message);
        } catch (Exception e) {
            log.error("sendMessage - email send error: {}", e.getMessage());
            throw e;
        }
    }

    public void sendMessageWithAttachment(String from, String to, String subject, String text,
            String attachmentFilename, Resource attachment) throws Exception {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(from);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text);
        helper.addAttachment(attachmentFilename, attachment);

        try {
            mailSender.send(message);
        } catch (Exception e) {
            log.error("sendMessage - email send error: {}", e.getMessage());
            throw e;
        }
    }
}
