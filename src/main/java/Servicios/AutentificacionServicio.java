package servicios;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.security.crypto.bcrypt.BCrypt;

import com.fasterxml.jackson.databind.ObjectMapper;

import dtos.LoginUsuarioDto;
import dtos.UsuarioDto;
import utilidades.Utilidades;

/**
 * Servicio encargado de gestionar la autenticación de usuarios, recuperación de contraseñas y actualización de las mismas.
 */
public class AutentificacionServicio {

    private String rol = "";
    private Long idUsuario;

    /**
     * Verifica si el usuario existe en la base de datos y si la contraseña es correcta.
     * 
     * @param correo    El correo electrónico del usuario a verificar.
     * @param password  La contraseña que se quiere verificar.
     * @return Un objeto {@link UsuarioDto} con los datos del usuario si la autenticación es correcta, o null si la autenticación falla.
     */
    public UsuarioDto verificarUsuario(String correo, String password) {
        UsuarioDto usuario = null;

        try {
            System.out.println("Verificando usuario con correo: " + correo);

            // Consultar el usuario completo desde la base de datos a través de la API
            URL url = new URL("http://localhost:8081/api/login/consultarUsuario");
            HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
            conexion.setRequestMethod("POST");
            conexion.setRequestProperty("Content-Type", "application/json");
            conexion.setDoOutput(true);

            // Crear el objeto DTO con el correo
            LoginUsuarioDto loginRequest = new LoginUsuarioDto();
            loginRequest.setEmail(correo);

            // Convertir el DTO a JSON
            ObjectMapper mapper = new ObjectMapper();
            String jsonInput = mapper.writeValueAsString(loginRequest);

            System.out.println("Datos enviados a la API: " + jsonInput);

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

                    // Mapeamos la respuesta JSON al objeto UsuarioDto
                    usuario = mapper.readValue(respuesta, UsuarioDto.class);

                    // Verificar la contraseña encriptada
                    if (usuario != null && BCrypt.checkpw(password, usuario.getContrasena())) {
                        // Si las contraseñas coinciden, establecer el rol y el idUsuario
                        this.rol = usuario.getRol();
                        this.idUsuario = usuario.getIdUsuario();
                        System.out.println("Usuario verificado correctamente. Rol: " + this.rol + ", ID: " + this.idUsuario);
                    } else {
                        System.out.println("Contraseña incorrecta.");
                        usuario = null;  // Si no coincide, devolver null
                    }
                }
            } else {
                System.out.println("Error: Código de respuesta no OK. Código: " + responseCode);
            }

        } catch (Exception e) {
            System.out.println("ERROR: " + e);
            e.printStackTrace();
        }

        return usuario;  // Devuelve el usuario completo o null si no se verifica
    }

   /**
    * Solicita la recuperación de la contraseña del usuario enviando un token al correo electrónico.
    * 
    * @param correo          El correo electrónico del usuario.
    * @param token           El token de recuperación de contraseña.
    * @param fechaExpiracion La fecha de expiración del token en milisegundos.
    * @return true si el correo se envió correctamente, false en caso contrario.
    */
    public boolean recuperarContrasenia(String correo, String token, long fechaExpiracion) {
        // Parametros para la API
        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("correo", correo);
        params.put("fechaExpiracion", String.valueOf(fechaExpiracion));  // Enviar la fecha de expiración

        try {
            // URL de la API para guardar el token y enviar el correo
            URL url = new URL("http://localhost:8081/api/usuarios/recuperarContrasenia");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json");

            // Enviar parámetros de la solicitud como JSON
            String jsonInputString = new JSONObject(params).toString();
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // Obtener código de respuesta
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Generar enlace de recuperación
                String asunto = "Recuperación de contraseña";
                String enlaceRecuperacion = "http://localhost:8080/VistaCodeComponents/NuevaContrasenia?token=" + token;
                String contenido = "Has solicitado la recuperación de tu contraseña. Utiliza el siguiente enlace para continuar:\n\n" + enlaceRecuperacion;

                // Enviar el correo al usuario
                return Utilidades.enviarCorreo(correo, asunto, contenido);
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Obtiene la fecha de expiración de un token.
     * 
     * @param token El token del cual se quiere obtener la fecha de expiración.
     * @return La fecha de expiración del token en milisegundos, o null si ocurre un error.
     */
    public Long obtenerFechaExpiracionDelToken(String token) {
        try {
            // Usar la nueva ruta que devuelve la fecha de expiración
            URL url = new URL("http://localhost:8081/api/usuarios/obtenerFechaExpiracionToken?token=" + token);
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

                    // Imprimir la respuesta para depuración
                    System.out.println("Respuesta de la API: " + response.toString());

                    // La respuesta es un número (marca de tiempo en milisegundos)
                    String responseString = response.toString().trim();
                    try {
                        // Intentamos convertir el valor a un Long
                        return Long.parseLong(responseString);
                    } catch (NumberFormatException e) {
                        System.out.println("Error al parsear el valor: " + e.getMessage());
                        return null;
                    }
                }
            } else {
                System.out.println("Error: Código de respuesta no OK. Código: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Actualiza la contraseña de un usuario utilizando el token de recuperación.
     * 
     * @param nuevaContraseniaEncriptada La nueva contraseña encriptada.
     * @param token                      El token de recuperación de la contraseña.
     * @return true si la actualización fue exitosa, false en caso contrario.
     */
    public boolean actualizarContrasenia(String nuevaContraseniaEncriptada, String token) {
        try {
            // Llamar a la API para actualizar la contraseña
            URL url = new URL("http://localhost:8081/api/usuarios/actualizarContrasenia");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            // Crear el objeto JSON con la nueva contraseña encriptada y el token
            JSONObject json = new JSONObject();
            json.put("nuevaContrasenia", nuevaContraseniaEncriptada);
            json.put("token", token);

            // Enviar la solicitud a la API
            String jsonInput = json.toString();
            try (OutputStream os = connection.getOutputStream()) {
                os.write(jsonInput.getBytes(StandardCharsets.UTF_8));
                os.flush();
            }

            // Verificar la respuesta de la API
            int responseCode = connection.getResponseCode();
            return responseCode == HttpURLConnection.HTTP_OK;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Obtiene la lista de todos los usuarios registrados en el sistema.
     * 
     * @return Una lista de objetos {@link UsuarioDto} con todos los usuarios.
     */
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
     * Obtiene el rol del usuario autenticado.
     * 
     * @return El rol del usuario.
     */
    public String getRol() {
        return rol;
    }

    /**
     * Obtiene el ID del usuario autenticado.
     * 
     * @return El ID del usuario.
     */
    public Long getId() {
        return idUsuario;
    }
}
