/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utilidades;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Clase utilitaria para el envío de correos electrónicos.
 * Maneja la configuración SMTP y el envío de notificaciones.
 */
public class ServicioCorreo {
    
    private static final Logger LOGGER = Logger.getLogger(ServicioCorreo.class.getName());
    
    // Configuración del servidor SMTP (Gmail como ejemplo)
    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final String SMTP_PORT = "587";
    private static final String EMAIL_ADMIN = "carlomachac@gmail.com"; // Cambiar por el email del administrador
    private static final String PASSWORD_ADMIN = "gato"; // Cambiar por la contraseña de aplicación
    
    /**
     * Envía un correo de notificación sobre el estado del préstamo.
     * 
     * @param emailUsuario Correo electrónico del usuario
     * @param ruUsuario RU del usuario
     * @param esAceptado true si el préstamo fue aceptado, false si fue rechazado
     * @return true si el correo se envió exitosamente, false en caso contrario
     */
    public static boolean enviarNotificacionPrestamo(String emailUsuario, int ruUsuario, boolean esAceptado) {
        try {
            // Configurar propiedades del servidor SMTP
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", SMTP_HOST);
            props.put("mail.smtp.port", SMTP_PORT);
            props.put("mail.smtp.ssl.trust", SMTP_HOST);
            
            // Crear la sesión con autenticación
            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(EMAIL_ADMIN, PASSWORD_ADMIN);
                }
            });
            
            // Crear el mensaje
            Message mensaje = new MimeMessage(session);
            mensaje.setFrom(new InternetAddress(EMAIL_ADMIN));
            mensaje.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailUsuario));
            
            // Configurar asunto y contenido según el estado
            if (esAceptado) {
                mensaje.setSubject("Préstamo Aceptado - Sistema de Préstamos");
                mensaje.setText("Hola usuario RU: " + ruUsuario + "\n\n" +
                              "El préstamo solicitado se aceptó correctamente.\n" +
                              "¡GRACIAS POR SU ESPERA!\n\n" +
                              "Sistema de Préstamos\n" +
                              "Administración");
            } else {
                mensaje.setSubject("Préstamo Rechazado - Sistema de Préstamos");
                mensaje.setText("Hola usuario RU: " + ruUsuario + "\n\n" +
                              "El préstamo solicitado fue rechazado.\n" +
                              "¡GRACIAS POR SU ESPERA!\n\n" +
                              "Sistema de Préstamos\n" +
                              "Administración");
            }
            
            // Enviar el mensaje
            Transport.send(mensaje);
            
            LOGGER.info("Correo enviado exitosamente a: " + emailUsuario + " para usuario RU: " + ruUsuario);
            return true;
            
        } catch (MessagingException e) {
            LOGGER.log(Level.SEVERE, "Error al enviar correo a " + emailUsuario + " para usuario RU: " + ruUsuario, e);
            return false;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error inesperado al enviar correo", e);
            return false;
        }
    }
    
    /**
     * Método para verificar si la configuración de correo es válida.
     * 
     * @return true si la configuración es válida, false en caso contrario
     */
    public static boolean verificarConfiguracion() {
        if (EMAIL_ADMIN.equals("tu_email@gmail.com") || PASSWORD_ADMIN.equals("tu_password_app")) {
            LOGGER.warning("Configuración de correo no establecida. Actualizar EMAIL_ADMIN y PASSWORD_ADMIN.");
            return false;
        }
        return true;
    }
}