package com.ipn.mx.administracioneventos;

import com.ipn.mx.administracioneventos.util.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AdministracionEventosApplication implements CommandLineRunner {
    @Autowired
    private EmailService emailService;

    @Override
    public void run(String... args) throws Exception {
        String texto = "Lorem ipsum dolor sit amet consectetur adipiscing elit tempor risus dui porttitor, blandit aptent vitae at torquent rhoncus eget ultrices lacinia nostra, cubilia sociis arcu hac nunc netus magna morbi dis varius. Hac porta a consequat volutpat bibendum habitant libero risus, faucibus dapibus dis tortor placerat ornare leo, vitae diam mauris tempus phasellus ante porttitor. Dui faucibus aliquet massa tristique semper cursus quisque dignissim himenaeos vehicula rutrum sapien pellentesque, penatibus natoque sollicitudin facilisi nec sed accumsan potenti mollis congue nullam.";

        String to="fmdominguezmoreno16@gmail.com";
        String subject = "Lorem ipsum. Correo enviado desde Spring Boot";
        emailService.enviarCorreo(to, subject, texto);
    }


    public static void main(String[] args) {
        SpringApplication.run(AdministracionEventosApplication.class, args);
    }
}