package com.example.demo.service.Impl;

import com.example.demo.model.Mensaje;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
 public class EmailService {
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${portfolio.owner.email:tu.email@gmail.com}")
    private String portfolioOwnerEmail;

    public void enviarMensajeContacto(Mensaje mensaje) {
        try {
            SimpleMailMessage emailMessage = new SimpleMailMessage();
            emailMessage.setTo(portfolioOwnerEmail);
            emailMessage.setSubject("📧 Nuevo mensaje de contacto: " + mensaje.getNombreUsuario());
            emailMessage.setText(construirContenidoEmail(mensaje));
            emailMessage.setFrom(fromEmail);

            mailSender.send(emailMessage);
            log.info("Email de notificación enviado exitosamente para el mensaje ID: {}", mensaje.getId());

        } catch (Exception e) {
            log.error("Error al enviar email de notificación para el mensaje ID: {}", mensaje.getId(), e);
            // No lanzamos excepción para no afectar el guardado del mensaje en la base de datos
        }
    }

    private String construirContenidoEmail(Mensaje mensaje) {
        return String.format("""
            NUEVO MENSAJE DE CONTACTO - PORTFOLIO
            =====================================
            
            👤 Nombre: %s
            📧 Email: %s
            💬 Mensaje: 
            %s
            
            📅 Fecha: %s
            
            --------------------------------------
            Este mensaje fue enviado a través del formulario de contacto de tu portfolio.
            """,
                mensaje.getNombreUsuario(),
                mensaje.getEmail(),
                mensaje.getMensaje(),
                mensaje.getFechaCreacion()
        );
    }

}
