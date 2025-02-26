package servicios;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;
import com.fasterxml.jackson.databind.ObjectMapper;
import dtos.RegistroUsuarioDto;
import org.springframework.security.crypto.bcrypt.BCrypt;
import utilidades.Utilidades;

public class RegistroServicio {

    /**
     * Genera el código de verificación, envía el correo y lo transmite a la API para que lo almacene.
     *
     * @param correo el correo del usuario.
     * @return true si el proceso fue exitoso, false en caso contrario.
     */
    public boolean enviarCodigoVerificacion(String correo) {
        try {
            // Generar código aleatorio de 6 dígitos
            String codigoVerificacion = String.valueOf(100000 + new Random().nextInt(900000));

            // Enviar el correo con el código usando la clase Utilidades
            String asunto = "Código de Verificación para Registro";
            String mensaje = "Tu código de verificación es: " + codigoVerificacion;
            boolean correoEnviado = Utilidades.enviarCorreo(correo, asunto, mensaje);
            if (!correoEnviado) {
                return false;
            }
            
            // Enviar el código generado a la API para almacenarlo.
            URL url = new URL("http://localhost:8081/api/registro/almacenarCodigo");
            HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
            conexion.setRequestMethod("POST");
            conexion.setRequestProperty("Content-Type", "application/json");
            conexion.setDoOutput(true);
            
            // Preparamos el JSON con el correo y el código generado
            String jsonInput = "{\"emailUsuario\": \"" + correo + "\", \"codigoVerificacion\": \"" + codigoVerificacion + "\"}";
            
            try (OutputStream os = conexion.getOutputStream()) {
                os.write(jsonInput.getBytes("UTF-8"));
                os.flush();
            }
            
            int responseCode = conexion.getResponseCode();
            return responseCode == HttpURLConnection.HTTP_OK;
        } catch (Exception e) {
            System.out.println("Error en enviarCodigoVerificacion: " + e.getMessage());
            return false;
        }
    }

    /**
     * Registra un nuevo usuario llamando al endpoint de la API.
     *
     * @param registroDto el objeto DTO con los datos del usuario a registrar.
     * @return true si el registro fue exitoso; false en caso contrario.
     */
    public boolean registrarUsuario(RegistroUsuarioDto registroDto) {
        // Validación: el email es obligatorio.
        if (registroDto.getEmailUsuario() == null || registroDto.getEmailUsuario().isEmpty()) {
            throw new IllegalArgumentException("El email es obligatorio.");
        }
        
        try {
            // Encriptar la contraseña en el Dynamic Web Project
            String passwordEncriptada = BCrypt.hashpw(registroDto.getPasswordUsuario(), BCrypt.gensalt());
            registroDto.setPasswordUsuario(passwordEncriptada);

            URL url = new URL("http://localhost:8081/api/registro/usuario");
            HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
            conexion.setRequestMethod("POST");
            conexion.setRequestProperty("Content-Type", "application/json");
            conexion.setDoOutput(true);

            ObjectMapper mapper = new ObjectMapper();
            String jsonInput = mapper.writeValueAsString(registroDto);

            try (OutputStream os = conexion.getOutputStream()) {
                os.write(jsonInput.getBytes("UTF-8"));
                os.flush();
            }

            int responseCode = conexion.getResponseCode();
            return responseCode == HttpURLConnection.HTTP_CREATED;
        } catch (Exception e) {
            System.out.println("Error al registrar usuario: " + e.getMessage());
            return false;
        }
    }
}
