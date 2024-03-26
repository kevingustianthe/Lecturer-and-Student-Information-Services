package com.fikupnvj.restfulapi.service;

import com.fikupnvj.restfulapi.entity.Account;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Async
    public void sendVerificationEmail(Account account) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            String content ="<body style=\"font-family: Arial, sans-serif; background-color: #f4f4f4; text-align: center;\">\n" +
                            "    <div style=\"background-color: #ffffff; max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #ddd; border-radius: 5px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);\">\n" +
                            "        <div style=\"background-color: #007bff; color: #ffffff; padding: 20px; border-top-left-radius: 5px; border-top-right-radius: 5px;\">\n" +
                            "            <h1>Verifikasi Email</h1>\n" +
                            "        </div>\n" +
                            "        <div style=\"padding: 20px;\">\n" +
                            "            <p>Dear [[name]],</p>\n" +
                            "            <p>Silakan klik tombol di bawah ini untuk memverifikasi alamat email Anda:</p>\n" +
                            "            <a href=\"[[URL]]\" style=\"display: inline-block; padding: 10px 20px; background-color: #007bff; color: #fff; text-decoration: none; border-radius: 5px;\">Verifikasi Email</a>\n" +
                            "            <p>Terima kasih.</p>\n" +
                            "        </div>\n" +
                            "    </div>\n" +
                            "</body>";

            String baseURL = "http://localhost:8090/api/auth/verify";
            String verifyURL = baseURL + "?email=" + account.getEmail() + "&code=" + account.getVerificationCode();

            content = content.replace("[[name]]", account.getEmail().split("@")[0]);
            content = content.replace("[[URL]]", verifyURL);

            helper.setTo(account.getEmail());
            helper.setSubject("Verify your account");
            helper.setText(content, true);

            javaMailSender.send(message);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    public boolean checkValidEmail(String email) {
        String emailPattern = "^[a-z0-9_+&*-]+(?:\\.[a-z0-9_+&*-]+)*@(upnvj\\.ac\\.id|mahasiswa\\.upnvj\\.ac\\.id)$";
        Pattern p = Pattern.compile(emailPattern);
        Matcher m = p.matcher(email);
        return m.matches();
    }
}
