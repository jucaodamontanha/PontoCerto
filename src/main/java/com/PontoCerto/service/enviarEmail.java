package com.PontoCerto.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class enviarEmail {

    @Autowired
    private JavaMailSender mailSender;

    public void enviarEmail(String destinatario, String assunto, String token, String templateHtml) {
        try {
            MimeMessage mensagem = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mensagem, true);

            helper.setTo(destinatario);
            helper.setSubject(assunto);

            String html = templateHtml
                    .replace("{{TOKEN}}", token)
                    .replace("{{LINK_REDEFINIR}}", "https://seusite.com/resetar-senha?token=" + token);

            helper.setText(html, true); // true = conteúdo HTML

            mailSender.send(mensagem);
            System.out.println("E-mail enviado para " + destinatario);
        } catch (MessagingException e) {
            throw new RuntimeException("Erro ao enviar e-mail", e);
        }
    }
}