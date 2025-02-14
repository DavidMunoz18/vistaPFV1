package servicios;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.json.JSONObject;
import org.springframework.security.crypto.bcrypt.BCrypt;

import com.fasterxml.jackson.databind.ObjectMapper;

import dtos.LoginUsuarioDto;
import dtos.UsuarioDto;
import utilidades.Utilidades;

public class AutentificacionServicio {

    private String rol = "";
    private Long idUsuario;

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


    public boolean recuperarContrasenia(String correo, String token) {
        // Crear el JSON con los parámetros requeridos
        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("correo", correo);  // Incluir correo en el JSON
        
       
      
        

        try {
            // Configurar la solicitud POST a la API de Spring Boot
            URL url = new URL("http://localhost:8081/api/usuarios/recuperarContrasenia");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json");

            // Convertir el Map a JSON
            String jsonInputString = new JSONObject(params).toString();

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // Manejar la respuesta de la API
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Aquí, enviar el correo usando la clase Utilidades
                String asunto = "Recuperación de contraseña";
                
                // Enlace de recuperación con el token
                String enlaceRecuperacion = "http://localhost:8080/VistaCodeComponents/NuevaContrasenia?token=" + token;
                
                // Contenido del correo con el enlace
                String contenido = "Has solicitado la recuperación de tu contraseña. Utiliza el siguiente enlace para continuar: \n\n"
                                   + enlaceRecuperacion ;

                // Enviar el correo
                boolean correoEnviado = Utilidades.enviarCorreo(correo, asunto, contenido);
                if (correoEnviado) {
                    return true; // Correo enviado correctamente
                } else {
                    System.out.println("Error al enviar el correo.");
                    return false;
                }
            } else {
                return false; // Hubo un error al guardar el token
            }
        } catch (Exception e) {
            e.printStackTrace(); // En producción, utiliza un framework de logging
            return false;
        }
    }





    public boolean validarToken(String token) {
        try {
            // URL de la API para validar el token
            String urlApi = "http://localhost:8081/api/usuarios/validarToken";
            System.out.println("Validando token: " + token);

            // Crear la URL y la conexión
            URL url = new URL(urlApi);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            
            // Configurar la conexión
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            // Crear el cuerpo de la solicitud con el token
            JSONObject json = new JSONObject();
            json.put("token", token);
            String jsonInput = json.toString();
            System.out.println("Cuerpo de la solicitud para validación de token: " + jsonInput);

            // Enviar la solicitud
            try (OutputStream os = connection.getOutputStream()) {
                os.write(jsonInput.getBytes(StandardCharsets.UTF_8));
                os.flush();
            }

            // Obtener el código de respuesta HTTP
            int responseCode = connection.getResponseCode();
            System.out.println("Código de respuesta HTTP: " + responseCode);

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
            System.out.println("Cuerpo de la solicitud para actualizar la contraseña: " + jsonInput);

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

    public String getRol() {
        return rol;
    }

    public Long getId() {
        return idUsuario;
    }
}
