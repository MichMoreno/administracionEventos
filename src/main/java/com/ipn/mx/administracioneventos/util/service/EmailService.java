package com.ipn.mx.administracioneventos.util.service;

public interface EmailService {
    public void enviarCorreo(String to, String subject, String text);
}
