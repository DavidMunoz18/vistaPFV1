package servicios;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import dtos.ProductoDto;
import dtos.ReseniaDto;

/**
 * Servicio encargado de gestionar las operaciones relacionadas con las reseñas de productos.
 * Permite obtener reseñas de productos y agregar nuevas reseñas.
 */
public class ResenaServicio {

    private static final String API_URL_RESENIAS = "http://localhost:8081/api/resenias";

    /**
     * Obtiene las reseñas de un producto dado su ID.
     * 
     * @param idProducto El ID del producto para obtener sus reseñas.
     * @return Lista de reseñas del producto.
     */
    public List<ReseniaDto> obtenerReseniasPorProducto(Long idProducto) {
        List<ReseniaDto> resenias = new ArrayList<>();
        try {
            URL url = new URL(API_URL_RESENIAS + "/producto/" + idProducto);
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

                    ObjectMapper mapper = new ObjectMapper();
                    ReseniaDto[] reseniasArray = mapper.readValue(response.toString(), ReseniaDto[].class);
                    for (ReseniaDto resenia : reseniasArray) {
                        resenias.add(resenia);
                    }
                }
            } else {
                throw new IOException("Error al obtener las reseñas, Código de respuesta: " + responseCode);
            }
        } catch (Exception e) {
            System.err.println("Error obteniendo reseñas: " + e.getMessage());
            e.printStackTrace();
        }
        return resenias;
    }

    /**
     * Agrega una nueva reseña a un producto dado su ID de usuario.
     * 
     * @param reseniaDto El objeto de la reseña a agregar.
     * @param productoDto El producto al cual se agrega la reseña.
     * @param idUsuario El ID del usuario que está agregando la reseña.
     * @return true si la reseña fue agregada exitosamente, false en caso contrario.
     */
    public boolean agregarResenia(ReseniaDto reseniaDto, ProductoDto productoDto, Long idUsuario) {
        boolean resultado = false;
        try {
            // Establecer el ID del producto y el ID del usuario
            reseniaDto.setIdProducto(productoDto.getId());
            reseniaDto.setIdUsuario(idUsuario);

            URL url = new URL(API_URL_RESENIAS + "/agregar");
            HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
            conexion.setRequestMethod("POST");
            conexion.setRequestProperty("Content-Type", "application/json");
            conexion.setDoOutput(true);

            String jsonResenia = new ObjectMapper().writeValueAsString(reseniaDto);

            try (OutputStream os = conexion.getOutputStream()) {
                os.write(jsonResenia.getBytes());
                os.flush();
            }

            int responseCode = conexion.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                resultado = true;
            } else {
                System.err.println("Error al agregar la reseña. Código de respuesta: " + responseCode);
            }
        } catch (Exception e) {
            System.err.println("Error al agregar reseña: " + e.getMessage());
            e.printStackTrace();
        }
        return resultado;
    }
}
