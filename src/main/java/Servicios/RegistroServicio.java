package servicios;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import dtos.RegistroUsuarioDto;

public class RegistroServicio {

    /**
     * Envía el código de verificación a través de la API.
     * 
     * @param correo el correo electrónico al que se enviará el código de verificación.
     * @return {@code true} si el código fue enviado correctamente; {@code false} en caso contrario.
     */
    public boolean enviarCodigoVerificacion(String correo) {
        try {
            URL url = new URL("http://localhost:8081/api/registro/enviarCodigoVerificacion");
            HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
            conexion.setRequestMethod("POST");
            conexion.setRequestProperty("Content-Type", "application/json");
            conexion.setDoOutput(true);

            // Convertimos el correo en formato JSON
            ObjectMapper mapper = new ObjectMapper();
            String jsonInput = mapper.writeValueAsString(Map.of("correo", correo));

            // Enviamos la petición
            try (OutputStream os = conexion.getOutputStream()) {
                os.write(jsonInput.getBytes());
                os.flush();
            }

            // Verificamos si la respuesta fue exitosa (200 OK)
            int responseCode = conexion.getResponseCode();
            return responseCode == HttpURLConnection.HTTP_OK;

        } catch (Exception e) {
            System.out.println("Error al enviar código: " + e.getMessage());
            return false;
        }
    }

    /**
     * Registra un nuevo usuario tras validar el código de verificación.
     * 
     * @param registroDto el objeto DTO con los datos del usuario a registrar.
     * @return {@code true} si el registro fue exitoso; {@code false} en caso contrario.
     */
    public boolean registrarUsuario(RegistroUsuarioDto registroDto) {
        try {
            URL url = new URL("http://localhost:8081/api/registro/usuario");
            HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
            conexion.setRequestMethod("POST");
            conexion.setRequestProperty("Content-Type", "application/json");
            conexion.setDoOutput(true);

            // Convertir el objeto DTO a JSON
            ObjectMapper mapper = new ObjectMapper();
            String jsonInput = mapper.writeValueAsString(registroDto);

            // Enviamos la petición
            try (OutputStream os = conexion.getOutputStream()) {
                os.write(jsonInput.getBytes());
                os.flush();
            }

            // Verificar el código de respuesta (201 Created)
            int responseCode = conexion.getResponseCode();
            return responseCode == HttpURLConnection.HTTP_CREATED;

        } catch (Exception e) {
            System.out.println("Error al registrar usuario: " + e.getMessage());
            return false;
        }
    }
}
