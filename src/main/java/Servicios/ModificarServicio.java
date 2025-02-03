package Servicios;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import com.fasterxml.jackson.databind.ObjectMapper;


import Dtos.ModificarUsuarioDto;

/**
 * Servicio para manejar la modificación de usuarios y clubes.
 * <p>
 * Este servicio se comunica con una API externa para realizar solicitudes
 * HTTP PUT que modifican usuarios o clubes específicos. También incluye
 * métodos para obtener la información de un usuario o club por su ID.
 * </p>
 */
public class ModificarServicio {

    /**
     * Modifica la información de un usuario.
     * <p>
     * Este método envía una solicitud HTTP PUT para modificar los datos de un
     * usuario específico, como nombre, DNI, teléfono, rol y foto. La información
     * es enviada como parte de un cuerpo `multipart/form-data`.
     * </p>
     * 
     * @param idUsuario     el ID del usuario a modificar.
     * @param nuevoNombre   el nuevo nombre del usuario.
     * @param nuevoDni      el nuevo DNI del usuario.
     * @param nuevoTelefono el nuevo número de teléfono del usuario.
     * @param nuevoRol      el nuevo rol del usuario.
     * @param nuevaFoto     los bytes de la nueva foto del usuario.
     * @return un mensaje indicando el resultado de la operación.
     */
    public String modificarUsuario(long idUsuario, String nuevoNombre, String nuevoDni, String nuevoTelefono, String nuevoRol, byte[] nuevaFoto) {
        String boundary = "*****" + System.currentTimeMillis() + "*****"; // Límite para multipart
        try {
            URL url = new URL("http://localhost:8081/api/modificar/modificarUsuario/" + idUsuario);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            conn.setDoOutput(true);

            // Construir cuerpo multipart
            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());

            // Campo nuevoNombre
            if (nuevoNombre != null) {
                dos.writeBytes("--" + boundary + "\r\n");
                dos.writeBytes("Content-Disposition: form-data; name=\"nuevoNombre\"\r\n\r\n");
                dos.writeBytes(nuevoNombre + "\r\n");
            }

            // Campo nuevoDni
            if (nuevoDni != null) {
                dos.writeBytes("--" + boundary + "\r\n");
                dos.writeBytes("Content-Disposition: form-data; name=\"nuevoDni\"\r\n\r\n");
                dos.writeBytes(nuevoDni + "\r\n");
            }

            // Campo nuevoTelefono
            if (nuevoTelefono != null) {
                dos.writeBytes("--" + boundary + "\r\n");
                dos.writeBytes("Content-Disposition: form-data; name=\"nuevoTelefono\"\r\n\r\n");
                dos.writeBytes(nuevoTelefono + "\r\n");
            }

            // Campo nuevoRol
            if (nuevoRol != null) {
                dos.writeBytes("--" + boundary + "\r\n");
                dos.writeBytes("Content-Disposition: form-data; name=\"nuevoRol\"\r\n\r\n");
                dos.writeBytes(nuevoRol + "\r\n");
            }

            // Campo nuevaFoto
            if (nuevaFoto != null) {
                dos.writeBytes("--" + boundary + "\r\n");
                dos.writeBytes("Content-Disposition: form-data; name=\"nuevaFoto\"; filename=\"foto.jpg\"\r\n");
                dos.writeBytes("Content-Type: image/jpeg\r\n\r\n");
                dos.write(nuevaFoto);
                dos.writeBytes("\r\n");
            }

            dos.writeBytes("--" + boundary + "--\r\n");
            dos.flush();
            dos.close();

            // Leer respuesta
            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                StringBuilder response = new StringBuilder();
                try (Scanner scanner = new Scanner(conn.getInputStream())) {
                    while (scanner.hasNextLine()) {
                        response.append(scanner.nextLine());
                    }
                }
                return response.toString();
            } else {
                return "Error: Código de respuesta HTTP " + responseCode;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "Error al conectar con la API: " + e.getMessage();
        }
    }

    /**
     * Obtiene un usuario por su ID.
     * <p>
     * Este método envía una solicitud HTTP GET para obtener los datos de un usuario
     * específico a través de su ID. La respuesta se convierte en un objeto
     * {@link ModificarUsuarioDto}.
     * </p>
     * 
     * @param idUsuario el ID del usuario a obtener.
     * @return el objeto {@link ModificarUsuarioDto} con los datos del usuario, o {@code null} si no se encuentra.
     */
    public ModificarUsuarioDto obtenerUsuarioPorId(long idUsuario) {
        try {
            URL url = new URL("http://localhost:8081/api/modificar/buscarUsuario/" + idUsuario);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoOutput(true);

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                // Leer la respuesta y convertirla en un objeto UsuarioDTO
                Scanner scanner = new Scanner(conn.getInputStream());
                StringBuilder response = new StringBuilder();
                while (scanner.hasNextLine()) {
                    response.append(scanner.nextLine());
                }
                scanner.close();

                // Convertir la respuesta JSON en un objeto ModificarUsuarioDto
                ObjectMapper objectMapper = new ObjectMapper();
                return objectMapper.readValue(response.toString(), ModificarUsuarioDto.class);
            } else {
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

  

    
}
