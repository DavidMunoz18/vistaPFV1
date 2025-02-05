package Servicios;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;

import Dtos.LoginUsuarioDto;
import Dtos.UsuarioDto;

public class AutentificacionServicio {

    private String rol = "";
    private Long idUsuario;

    /**
     * Verifica las credenciales de un usuario llamando a la API correspondiente.
     * Si las credenciales son válidas, el rol del usuario (por ejemplo, "admin" o "usuario")
     * y el ID del usuario se guardan, y el método devuelve {@code true}.
     * 
     * @param correo   el correo electrónico del usuario.
     * @param password la contraseña del usuario.
     * @return {@code true} si las credenciales son válidas; {@code false} en caso contrario.
     */
    public boolean verificarUsuario(String correo, String password) {
        boolean todoOk = false;

        try {
            // Crear la URL de la API para la verificación del usuario
            URL url = new URL("http://localhost:8081/api/login/validarUsuario");
            HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
            conexion.setRequestMethod("POST");
            conexion.setRequestProperty("Content-Type", "application/json");
            conexion.setDoOutput(true);

            // Crear el objeto DTO con las credenciales del usuario
            LoginUsuarioDto loginRequest = new LoginUsuarioDto();
            loginRequest.setEmail(correo);
            loginRequest.setPassword(password);

            // Convertir el DTO a JSON
            ObjectMapper mapper = new ObjectMapper();
            String jsonInput = mapper.writeValueAsString(loginRequest);

            // Enviar la solicitud al servidor
            try (OutputStream ot = conexion.getOutputStream()) {
                ot.write(jsonInput.getBytes());
                ot.flush();
            }

            // Procesar la respuesta del servidor
            int responseCode = conexion.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader in = new BufferedReader(new InputStreamReader(conexion.getInputStream()))) {
                    StringBuilder response = new StringBuilder();
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }

                    String respuesta = response.toString();
                    System.out.println("Respuesta del servidor: " + respuesta);

                    // Comprobar si la respuesta contiene el rol y el ID
                    if (respuesta.contains(" - ID: ")) {
                        String[] partes = respuesta.split(" - ID: ");
                        if (partes.length == 2) {
                            this.rol = partes[0].trim(); // "usuario" o "admin"
                            this.idUsuario = Long.parseLong(partes[1].trim()); // "2"
                            todoOk = true;
                        }
                    } else {
                        System.out.println("Rol desconocido o error en la respuesta.");
                    }
                }
            } else {
                System.out.println("Error: Código de respuesta no OK. Código: " + responseCode);
            }

        } catch (Exception e) {
            System.out.println("ERROR: " + e);
            e.printStackTrace();
        }

        return todoOk;
    }

    public boolean recuperarContrasenia(String correo) {
        try {
            // Imprimir el correo recibido para depuración
            System.out.println("Correo recibido en el servicio para recuperación de contraseña: " + correo);

            // URL del endpoint de recuperación de la API de Spring Boot
            String urlApi = "http://localhost:8081/api/usuarios/recuperar";
            
            // Crear la URL y la conexión
            URL url = new URL(urlApi);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            
            // Establecer el método POST y configurar las cabeceras
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);
            
            // Crear el cuerpo de la solicitud en formato JSON
            String jsonInputString = "{\"correo\": \"" + correo + "\"}";
            
            // Mostrar el JSON que se va a enviar
            System.out.println("JSON que se va a enviar a la API: " + jsonInputString);

            connection.getOutputStream().write(jsonInputString.getBytes("UTF-8"));
            
            // Obtener la respuesta de la API
            int responseCode = connection.getResponseCode();
            System.out.println("Código de respuesta HTTP: " + responseCode);
            
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Si la respuesta es 200 OK, la recuperación fue exitosa
                return true;  
            } else {
                System.out.println("Error en la API. Código de respuesta: " + responseCode);
                return false;  // Si no, hubo un error
            }

        } catch (Exception e) {
            // En caso de excepción, mostrar el error y retornar false
            e.printStackTrace();
            return false;  
        }
    }

    public boolean validarToken( String token) {
        try {
            // URL de la API para validar el token
            String urlApi = "http://localhost:8081/api/usuarios/validarToken";

            // Crear la URL y la conexión
            URL url = new URL(urlApi);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            
            // Configurar la conexión
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            // Crear el cuerpo de la solicitud con el correo y token
            JSONObject json = new JSONObject();
           
            json.put("token", token);
            String jsonInput = json.toString();

            // Enviar la solicitud
            try (OutputStream os = connection.getOutputStream()) {
                os.write(jsonInput.getBytes(StandardCharsets.UTF_8));
                os.flush();
            }

            // Obtener el código de respuesta HTTP
            int responseCode = connection.getResponseCode();

            // Verificar si el código de respuesta es HTTP_OK (200)
            if (responseCode == HttpURLConnection.HTTP_OK) {
                return true;  // El token es válido
            } else {
                return false; // El token no es válido
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;  // Error al validar el token
        }
    }



    /**
     * Actualiza la contraseña de un usuario.
     * @param correo el correo del usuario.
     * @param nuevaContrasenia la nueva contraseña.
     * @param confirmarContrasenia la confirmación de la nueva contraseña.
     * @param token el token de recuperación.
     * @return {@code true} si la contraseña se actualizó correctamente; {@code false} en caso contrario.
     */
    public boolean actualizarContrasenia(String nuevaContrasenia, String confirmarContrasenia, String token) {
        try {
            // Imprimir los datos recibidos para depuración
            System.out.println("Actualizando contraseña...");
            System.out.println("Nueva Contraseña: " + nuevaContrasenia);
            System.out.println("Confirmar Contraseña: " + confirmarContrasenia);
            System.out.println("Token: " + token);

            // URL de la API para actualizar la contraseña
            String urlApi = "http://localhost:8081/api/usuarios/actualizarContrasenia";

            // Crear la conexión
            URL url = new URL(urlApi);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            
            // Configurar la solicitud
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            // Crear JSON con los datos
            JSONObject json = new JSONObject();
            json.put("nuevaContrasenia", nuevaContrasenia);
            json.put("confirmarContrasenia", confirmarContrasenia);
            json.put("token", token);  // Incluir el token
            String jsonInput = json.toString();

            // Enviar los datos a la API
            try (OutputStream os = connection.getOutputStream()) {
                os.write(jsonInput.getBytes(StandardCharsets.UTF_8));
                os.flush();
            }

            // Leer la respuesta
            int responseCode = connection.getResponseCode();
            System.out.println("Código de respuesta HTTP: " + responseCode);

            // Si la respuesta es diferente a 200 OK, imprime la respuesta de error
            if (responseCode != HttpURLConnection.HTTP_OK) {
                try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getErrorStream(), StandardCharsets.UTF_8))) {
                    StringBuilder errorResponse = new StringBuilder();
                    String line;
                    while ((line = in.readLine()) != null) {
                        errorResponse.append(line);
                    }
                    System.out.println("Respuesta de error de la API: " + errorResponse.toString());
                }
            }

            return responseCode == HttpURLConnection.HTTP_OK; // Si es 200, éxito

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<UsuarioDto> obtenerUsuarios() {
        List<UsuarioDto> usuarios = new ArrayList<>();
        try {
            URL url = new URL("http://localhost:8081/api/usuarios/listar"); // Cambia el puerto y ruta según tu configuración
            HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
            conexion.setRequestMethod("GET");
            conexion.setRequestProperty("Content-Type", "application/json");

            int responseCode = conexion.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader in = new BufferedReader(new InputStreamReader(conexion.getInputStream()))) {
                    StringBuilder response = new StringBuilder();
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }

                    System.out.println("Respuesta de la API: " + response.toString());

                    // Mapeamos el JSON a la lista de objetos UsuarioDto
                    ObjectMapper mapper = new ObjectMapper();
                    UsuarioDto[] usuariosArray = mapper.readValue(response.toString(), UsuarioDto[].class);
                    for (UsuarioDto usuario : usuariosArray) {
                        usuarios.add(usuario);
                    }
                }
            } else {
                System.out.println("Error: Código de respuesta no OK. Código: " + responseCode);
            }
        } catch (Exception e) {
            System.out.println("ERROR: " + e);
            e.printStackTrace();
        }
        return usuarios;
    }




    /**
     * Obtiene el rol asignado al usuario autenticado.
     * 
     * @return el rol asignado.
     */
    public String getRol() {
        return rol;
    }

    /**
     * Obtiene el ID del usuario autenticado.
     * 
     * @return el ID del usuario.
     */
    public Long getId() {
        return idUsuario;
    }
}