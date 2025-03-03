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

/**
 * Clase utilitaria para enviar correos electrónicos utilizando JavaMail.
 */
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
     * @return true si el correo fue enviado exitosamente, false en caso contrario.
     * @throws MessagingException Si ocurre un error durante el envío del correo.
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
        final String password = "kjqk daka ayor jplg";

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
    
    /**
     * Escribe un log en un fichero único por sesión.
     * El nombre del fichero incluye el ID de la sesión y la fecha actual.
     *
     * @param session La sesión HTTP actual.
     * @param nivel Nivel del log (por ejemplo, INFO, ERROR).
     * @param clase Clase desde donde se llama el log.
     * @param metodo Método desde donde se llama el log.
     * @param mensaje Mensaje a registrar en el log.
     */
    /**
     * Escribe un log en un fichero único por sesión.
     * El nombre del fichero incluye el ID de la sesión y la fecha actual.
     *
     * @param session La sesión HTTP actual.
     * @param nivel Nivel del log (por ejemplo, INFO, ERROR).
     * @param clase Clase desde donde se llama el log.
     * @param metodo Método desde donde se llama el log.
     * @param mensaje Mensaje a registrar en el log.
     */
    public static void escribirLog(jakarta.servlet.http.HttpSession session, String nivel, String clase, String metodo, String mensaje) {
        
    	if (session == null) {
            System.out.println("Nivel: " + nivel + " - Clase: " + clase + " - Método: " + metodo + " - Mensaje: " + mensaje);
            return; //Salir para solo mostrar por pantalla
        }

    	
    	// Ruta absoluta hacia la carpeta "fichero"
        final String RUTA_BASE = "C:" + java.io.File.separator + "Users" + java.io.File.separator + "dmunnoz" 
                 + java.io.File.separator + "Documents"+ java.io.File.separator + "vistaPFV1" + java.io.File.separator + "fichero" + java.io.File.separator;
        
        // Asegurarse de que el directorio existe, y si no, crearlo
        java.io.File directorio = new java.io.File(RUTA_BASE);
        if (!directorio.exists()) {
            directorio.mkdirs();
        }
        
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
        String fecha = sdf.format(new java.util.Date());
        String sessionId = session.getId();
        String rutaArchivo = RUTA_BASE + "session_" + sessionId + "_" + fecha + ".txt";

        try (java.io.FileWriter fw = new java.io.FileWriter(rutaArchivo, true);
             java.io.PrintWriter pw = new java.io.PrintWriter(fw)) {

            String trazaError = new Throwable().getStackTrace()[1].toString();
            pw.println(nivel + " - " + clase + " - " + metodo + " - " + mensaje + " - " + trazaError);
        } catch (java.io.IOException e) {
            System.err.println("No se pudo escribir en el archivo de log: " + e.getMessage());
        }
    }




}
