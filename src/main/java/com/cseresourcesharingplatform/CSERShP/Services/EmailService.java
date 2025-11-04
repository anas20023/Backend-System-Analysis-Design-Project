package com.cseresourcesharingplatform.CSERShP.Services;

import com.cseresourcesharingplatform.CSERShP.Repository.EmailRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service

public class EmailService implements EmailRepository {
    @Autowired
    private JavaMailSender mailSender;

    public void sendHtmlMail(String to, String subject, String htmlContent) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true); // true = HTML
            helper.setFrom(new InternetAddress("anasibnbelal@gmail.com", "CSE Resource Sharing Platform"));


            mailSender.send(mimeMessage);
            System.out.println("✅ HTML email sent successfully to: " + to);

        } catch (MessagingException e) {
            System.err.println("❌ Failed to construct email message: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("⚠️ Unexpected error while sending email: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
