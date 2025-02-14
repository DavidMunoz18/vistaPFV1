package utilidades;

import java.util.Properties;

import javax.activation.CommandMap;
import javax.activation.MailcapCommandMap;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Utilidades {

    static {
        // Configuración del MailcapCommandMap para evitar el ClassCastException
        MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
        // Añadir los controladores para los tipos de contenido que se usan
        mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
        mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
        mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
        // Establecer el command map por defecto
        CommandMap.setDefaultCommandMap(mc);
    }

    /**
     * Envía un correo electrónico utilizando JavaMail.
     *
     * @param correoDestino Dirección de correo del destinatario.
     * @param asunto Asunto del mensaje.
     * @param contenido Contenido del mensaje.
     * @throws MessagingException Si ocurre un error durante el envío.
     */
    public static boolean enviarCorreo(String correoDestino, String asunto, String contenido) {
        boolean correoEnviado = false;

        // Configuración de propiedades SMTP para Gmail
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true"); // Activa STARTTLS
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        // Credenciales de la cuenta (utiliza variables de entorno en producción)
        final String username = "codecomponents123@gmail.com";
        final String password = "vbap ljmz ycki qboa";

        // Crear la sesión de correo con autenticación
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        // Construir el mensaje
        Message message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(correoDestino));
            message.setSubject(asunto);
            message.setText(contenido);

            // Enviar el mensaje
            Transport.send(message);
            correoEnviado = true;
            System.out.println("Correo enviado con éxito a: " + correoDestino);
        } catch (MessagingException e) {
            System.out.println("Error al enviar el correo: " + e.getMessage());
            e.printStackTrace();
        }

        return correoEnviado;
    }
}