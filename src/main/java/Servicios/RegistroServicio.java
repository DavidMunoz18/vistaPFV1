package Servicios;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.fasterxml.jackson.databind.ObjectMapper;


import Dtos.RegistroUsuarioDto;

/**
 * Servicio para manejar el registro de usuarios y clubes.
 * <p>
 * Este servicio se comunica con una API externa para registrar usuarios y clubes,
 * enviando sus datos a través de solicitudes HTTP POST. Utiliza objetos DTO para
 * representar los datos a registrar y convertirlos en formato JSON.
 * </p>
 */
public class RegistroServicio {

    /**
     * Registra un nuevo usuario a través de la API.
     * <p>
     * Este método envía una solicitud HTTP POST con los datos de un usuario en
     * formato JSON. Si la respuesta es exitosa, el usuario es registrado.
     * </p>
     * 
     * @param registroDto el objeto DTO que contiene los datos del usuario a registrar.
     * @return {@code true} si el registro fue exitoso; {@code false} en caso contrario.
     */
    public boolean registrarUsuario(RegistroUsuarioDto registroDto) {
        boolean registroExitoso = false;

        try {
            // Configurar la URL de la API para el registro
            URL url = new URL("http://localhost:8081/api/registro/usuario");
            HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
            conexion.setRequestMethod("POST");
            conexion.setRequestProperty("Content-Type", "application/json");
            conexion.setDoOutput(true);

            // Convertir el DTO de usuario a JSON
            ObjectMapper mapper = new ObjectMapper();
            String jsonInput = mapper.writeValueAsString(registroDto);

            // Enviar la solicitud al servidor
            try (OutputStream os = conexion.getOutputStream()) {
                os.write(jsonInput.getBytes());
                os.flush();
            }

            // Obtener el código de respuesta
            int responseCode = conexion.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_CREATED) { // 201 Created
                // Si el usuario se creó correctamente
                registroExitoso = true;
            } else if (responseCode == HttpURLConnection.HTTP_CONFLICT) { // 409 Conflict
                // Si hubo un conflicto, como correo duplicado
                System.out.println("Error: El correo ya está registrado.");
            } else {
                System.out.println("Error: Código de respuesta no esperado. Código: " + responseCode);
            }
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }

        return registroExitoso;
    }

    
}
