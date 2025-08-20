package com.travel.service;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {
	private final JavaMailSender naverMailSender;
//    private final JavaMailSender gmailMailSender;

    public EmailService(
            @Qualifier("naverMailSender") JavaMailSender naverMailSender
            ) {
        this.naverMailSender = naverMailSender;
    }

    public void sendShareLink(String toEmail, String shareUrl, String mailType) throws MessagingException {
        JavaMailSender sender;
        
        if ("naver".equalsIgnoreCase(mailType)) {
            sender = naverMailSender;
        } else if ("gmail".equalsIgnoreCase(mailType)) {
            sender = null;
        } else {
            throw new IllegalArgumentException("지원하지 않는 메일 타입입니다: " + mailType);
        }
        
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom("ggt9875654@naver.com");
        helper.setTo(toEmail);
        helper.setSubject("일정 공유 링크입니다");
        helper.setText(
            "<p>아래 링크를 통해 일정을 확인할 수 있습니다:</p>" +
            "<a href='" + shareUrl + "'>" + shareUrl + "</a>",
            true
        );
        
        sender.send(message);
        
    }
}
