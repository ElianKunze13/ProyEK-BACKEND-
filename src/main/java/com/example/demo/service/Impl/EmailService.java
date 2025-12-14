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
import org.springframework.mail.javamail.MimeMessageHelper;
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

    /*public void enviarMensajeContacto(Mensaje mensaje) {
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
*/


    public void enviarMensajeContacto(Mensaje mensaje) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

            helper.setTo(portfolioOwnerEmail);
            helper.setSubject("📧 Nuevo mensaje de contacto: " + mensaje.getNombreUsuario());
            helper.setText(construirContenidoEmailHTML(mensaje), true); // true = HTML
            helper.setFrom(fromEmail);

            mailSender.send(mimeMessage);
            log.info("Email de notificación enviado exitosamente para el mensaje ID: {}", mensaje.getId());

        } catch (Exception e) {
            log.error("Error al enviar email de notificación para el mensaje ID: {}", mensaje.getId(), e);
            // Podrías considerar enviar un correo de fallback simple
            enviarCorreoSimpleDeRespaldo(mensaje);
        }
    }

    private String construirContenidoEmailHTML(Mensaje mensaje) {
        return String.format("""
        <!DOCTYPE html>
        <html>
        <head>
            <style>
                body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
                .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                .header { background-color: #f8f9fa; padding: 15px; border-radius: 5px; text-align: center; }
                .content { margin: 20px 0; }
                .field { margin-bottom: 15px; }
                .label { font-weight: bold; color: #555; }
                .message-box { background-color: #f1f1f1; padding: 15px; border-radius: 5px; margin: 15px 0; }
                .footer { margin-top: 30px; font-size: 12px; color: #777; text-align: center; }
            </style>
        </head>
        <body>
            <div class="container">
                <div class="header">
                    <h2>📧 NUEVO MENSAJE DE CONTACTO - PORTFOLIO</h2>
                </div>
                
                <div class="content">
                    <div class="field">
                        <span class="label">👤 Nombre:</span>
                        <span>%s</span>
                    </div>
                    
                    <div class="field">
                        <span class="label">📧 Email:</span>
                        <span><a href="mailto:%s">%s</a></span>
                    </div>
                    
                    <div class="field">
                        <span class="label">📅 Fecha:</span>
                        <span>%s</span>
                    </div>
                    
                    <div class="field">
                        <span class="label">💬 Mensaje:</span>
                        <div class="message-box">
                            %s
                        </div>
                    </div>
                </div>
                
                <div class="footer">
                    <p>Este mensaje fue enviado a través del formulario de contacto de tu portfolio.</p>
                    <p>ID del mensaje: %d</p>
                </div>
            </div>
        </body>
        </html>
        """,
                mensaje.getNombreUsuario(),
                mensaje.getEmail(), mensaje.getEmail(),
                mensaje.getFechaCreacion(),
                mensaje.getMensaje().replace("\n", "<br>"),
                mensaje.getId()
        );
    }

    private void enviarCorreoSimpleDeRespaldo(Mensaje mensaje) {
        try {
            SimpleMailMessage fallbackMessage = new SimpleMailMessage();
            fallbackMessage.setTo(portfolioOwnerEmail);
            fallbackMessage.setSubject("[FALLBACK] Mensaje de contacto: " + mensaje.getNombreUsuario());
            fallbackMessage.setText("Nombre: " + mensaje.getNombreUsuario() + "\n" +
                    "Email: " + mensaje.getEmail() + "\n" +
                    "Mensaje: " + mensaje.getMensaje());
            fallbackMessage.setFrom(fromEmail);
            mailSender.send(fallbackMessage);
        } catch (Exception ex) {
            log.error("Error incluso con el correo de respaldo", ex);
        }
}
}
