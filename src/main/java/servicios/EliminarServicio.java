package servicios;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import utilidades.Utilidades;

/**
 * Servicio para manejar la eliminación de usuarios y clubes.
 * <p>
 * Este servicio se comunica con una API externa para realizar solicitudes
 * HTTP DELETE que eliminan usuarios o clubes específicos.
 * </p>
 */
public class EliminarServicio {

    /**
     * Elimina un usuario llamando a la API correspondiente.
     * <p>
     * Envía una solicitud HTTP DELETE al endpoint de la API para eliminar
     * un usuario con el ID especificado. Retorna un mensaje de resultado
     * dependiendo de la respuesta de la API.
     * </p>
     * 
     * @param idUsuario el ID del usuario que se desea eliminar.
     * @return un mensaje indicando el resultado de la operación.
     */
    public String eliminarUsuario(long idUsuario) {
        HttpURLConnection connection = null;
        
        // Log de inicio de la operación
        Utilidades.escribirLog(null, "[INFO]", "EliminarServicio", "eliminarUsuario", "Inicio de eliminación del usuario con ID: " + idUsuario);
        
        try {
            // Construir la URL para la solicitud DELETE
            URL url = new URL("http://localhost:8081/api/eliminar/usuario/" + idUsuario);

            // Abrir la conexión
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("DELETE");
            connection.setConnectTimeout(5000); // Tiempo de espera para conexión
            connection.setReadTimeout(5000); // Tiempo de espera para lectura

            // Verificar el código de respuesta
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_NO_CONTENT) { // 204 No Content
                Utilidades.escribirLog(null, "[INFO]", "EliminarServicio", "eliminarUsuario", "Usuario eliminado exitosamente con ID: " + idUsuario);
                return "Usuario eliminado exitosamente.";
            } else if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) { // 404 Not Found
                Utilidades.escribirLog(null, "[ERROR]", "EliminarServicio", "eliminarUsuario", "Usuario no encontrado con ID: " + idUsuario);
                return "Usuario no encontrado.";
            } else {
                Utilidades.escribirLog(null, "[ERROR]", "EliminarServicio", "eliminarUsuario", "Error al eliminar el usuario con ID: " + idUsuario + ". Código de respuesta: " + responseCode);
                return "Error al eliminar el usuario. Código de respuesta: " + responseCode;
            }

        } catch (IOException e) {
            e.printStackTrace();
            Utilidades.escribirLog(null, "[ERROR]", "EliminarServicio", "eliminarUsuario", "Error al conectar con la API: " + e.getMessage());
            return "Error al conectar con la API: " + e.getMessage();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}
