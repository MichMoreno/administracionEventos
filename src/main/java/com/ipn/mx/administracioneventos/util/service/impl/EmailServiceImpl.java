package com.ipn.mx.administracioneventos.util.service.impl;

import com.ipn.mx.administracioneventos.util.service.EmailService;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
public class EmailServiceImpl implements EmailService {
    @Autowired
    private JavaMailSender mailSender;

    @Value("classpath:static/diccionarioDatos26_1.pdf")
    Resource resourceFile;

    @Override
    public void enviarCorreo(String to, String subject, String text) {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper;
        try{
            messageHelper = new MimeMessageHelper(message,true,"UTF-8");
            messageHelper.setFrom(new InternetAddress("noreply@gmail.com", "Administración de Eventos"));

            // ¡CAMBIA ESTA LÍNEA! - De File a InputStream
            // messageHelper.addAttachment("archivo", new File(resourceFile.getFile().toURI()));

            // Nueva versión que funciona en JARs:
            try (InputStream inputStream = resourceFile.getInputStream()) {
                messageHelper.addAttachment("archivo.pdf",
                        new InputStreamResource(inputStream),
                        "application/pdf");
            } catch (Exception e) {
                // Si no puede cargar el PDF, continúa sin él
                System.out.println("No se pudo adjuntar PDF: " + e.getMessage());
            }

            messageHelper.setTo(to);
            messageHelper.setSubject(subject);
            messageHelper.setText(text, true);
            mailSender.send(message);
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}