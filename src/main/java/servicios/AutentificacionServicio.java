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
        Utilidades.escribirLog(null, "[INFO]", "AutentificacionServicio", "verificarUsuario", "Verificando usuario con correo: " + correo);

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
            
            //los ficheros log de los servicios no le paso la sesion porque la sesion se controla en los controladores, 
            //por lo tanto aqui lo unico que haría es mostrar los mensajes por la consola, que se controla en la clase utilidades
            //para saber exactamente donde sería, pero directamente desde el controlador puedo saber donde ocurre el error, esto es un extra.
            Utilidades.escribirLog(null, "[INFO]", "AutentificacionServicio", "verificarUsuario", "Datos enviados a la API: " + jsonInput);

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
                    Utilidades.escribirLog(null, "[INFO]", "AutentificacionServicio", "verificarUsuario", "Respuesta del servidor: " + respuesta);

                    // Mapeamos la respuesta JSON al objeto UsuarioDto
                    usuario = mapper.readValue(respuesta, UsuarioDto.class);

                    // Verificar la contraseña encriptada
                    if (usuario != null && BCrypt.checkpw(password, usuario.getContrasena())) {
                        // Si las contraseñas coinciden, establecer el rol y el idUsuario
                        this.rol = usuario.getRol();
                        this.idUsuario = usuario.getIdUsuario();
                        System.out.println("Usuario verificado correctamente. Rol: " + this.rol + ", ID: " + this.idUsuario);
                        Utilidades.escribirLog(null, "[INFO]", "AutentificacionServicio", "verificarUsuario", "Usuario verificado correctamente. Rol: " + this.rol + ", ID: " + this.idUsuario);
                    } else {
                        System.out.println("Contraseña incorrecta.");
                        Utilidades.escribirLog(null, "[ERROR]", "AutentificacionServicio", "verificarUsuario", "Contraseña incorrecta para el usuario con correo: " + correo);
                        usuario = null;  // Si no coincide, devolver null
                    }
                }
            } else {
                System.out.println("Error: Código de respuesta no OK. Código: " + responseCode);
                Utilidades.escribirLog(null, "[ERROR]", "AutentificacionServicio", "verificarUsuario", "Código de respuesta no OK: " + responseCode);
            }

        } catch (Exception e) {
            System.out.println("ERROR: " + e);
            Utilidades.escribirLog(null, "[ERROR]", "AutentificacionServicio", "verificarUsuario", "Excepción: " + e.getMessage());
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
        Utilidades.escribirLog(null, "[INFO]", "AutentificacionServicio", "recuperarContrasenia", "Recuperando contraseña para correo: " + correo + ", token: " + token + ", fechaExpiracion: " + fechaExpiracion);
        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("correo", correo);
        params.put("fechaExpiracion", String.valueOf(fechaExpiracion));  // Enviar la fecha de expiración

        try {
            URL url = new URL("http://localhost:8081/api/usuarios/recuperarContrasenia");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json");

            String jsonInputString = new JSONObject(params).toString();
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                String asunto = "Recuperación de contraseña";
                String enlaceRecuperacion = "http://localhost:8080/VistaCodeComponents/NuevaContrasenia?token=" + token;
                String contenido = "Has solicitado la recuperación de tu contraseña. Utiliza el siguiente enlace para continuar:\n\n" + enlaceRecuperacion;
                Utilidades.escribirLog(null, "[INFO]", "AutentificacionServicio", "recuperarContrasenia", "Enviando correo de recuperación a: " + correo);
                return Utilidades.enviarCorreo(correo, asunto, contenido);
            } else {
                Utilidades.escribirLog(null, "[ERROR]", "AutentificacionServicio", "recuperarContrasenia", "Respuesta de la API no OK. Código: " + responseCode);
                return false;
            }
        } catch (Exception e) {
            Utilidades.escribirLog(null, "[ERROR]", "AutentificacionServicio", "recuperarContrasenia", "Excepción: " + e.getMessage());
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
        Utilidades.escribirLog(null, "[INFO]", "AutentificacionServicio", "obtenerFechaExpiracionDelToken", "Obteniendo fecha de expiración para token: " + token);
        try {
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

                    String responseString = response.toString().trim();
                    Utilidades.escribirLog(null, "[INFO]", "AutentificacionServicio", "obtenerFechaExpiracionDelToken", "Respuesta de la API: " + responseString);
                    try {
                        return Long.parseLong(responseString);
                    } catch (NumberFormatException e) {
                        Utilidades.escribirLog(null, "[ERROR]", "AutentificacionServicio", "obtenerFechaExpiracionDelToken", "Error al parsear el valor: " + e.getMessage());
                        System.out.println("Error al parsear el valor: " + e.getMessage());
                        return null;
                    }
                }
            } else {
                Utilidades.escribirLog(null, "[ERROR]", "AutentificacionServicio", "obtenerFechaExpiracionDelToken", "Código de respuesta no OK: " + responseCode);
                System.out.println("Error: Código de respuesta no OK. Código: " + responseCode);
            }
        } catch (Exception e) {
            Utilidades.escribirLog(null, "[ERROR]", "AutentificacionServicio", "obtenerFechaExpiracionDelToken", "Excepción: " + e.getMessage());
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
        Utilidades.escribirLog(null, "[INFO]", "AutentificacionServicio", "actualizarContrasenia", "Actualizando contraseña para token: " + token);
        try {
            URL url = new URL("http://localhost:8081/api/usuarios/actualizarContrasenia");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            JSONObject json = new JSONObject();
            json.put("nuevaContrasenia", nuevaContraseniaEncriptada);
            json.put("token", token);

            String jsonInput = json.toString();
            try (OutputStream os = connection.getOutputStream()) {
                os.write(jsonInput.getBytes(StandardCharsets.UTF_8));
                os.flush();
            }

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                Utilidades.escribirLog(null, "[INFO]", "AutentificacionServicio", "actualizarContrasenia", "Contraseña actualizada correctamente.");
            } else {
                Utilidades.escribirLog(null, "[ERROR]", "AutentificacionServicio", "actualizarContrasenia", "Código de respuesta no OK: " + responseCode);
            }
            return responseCode == HttpURLConnection.HTTP_OK;
        } catch (Exception e) {
            Utilidades.escribirLog(null, "[ERROR]", "AutentificacionServicio", "actualizarContrasenia", "Excepción: " + e.getMessage());
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
        Utilidades.escribirLog(null, "[INFO]", "AutentificacionServicio", "obtenerUsuarios", "Obteniendo lista de usuarios");
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
                    Utilidades.escribirLog(null, "[INFO]", "AutentificacionServicio", "obtenerUsuarios", "Respuesta de la API: " + response.toString());

                    // Mapeamos el JSON a la lista de objetos UsuarioDto
                    ObjectMapper mapper = new ObjectMapper();
                    UsuarioDto[] usuariosArray = mapper.readValue(response.toString(), UsuarioDto[].class);
                    for (UsuarioDto usuario : usuariosArray) {
                        usuarios.add(usuario);
                    }
                }
            } else {
                System.out.println("Error: Código de respuesta no OK. Código: " + responseCode);
                Utilidades.escribirLog(null, "[ERROR]", "AutentificacionServicio", "obtenerUsuarios", "Código de respuesta no OK: " + responseCode);
            }
        } catch (Exception e) {
            System.out.println("ERROR: " + e);
            Utilidades.escribirLog(null, "[ERROR]", "AutentificacionServicio", "obtenerUsuarios", "Excepción: " + e.getMessage());
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
