package servicios;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import dtos.ProductoDto;
import dtos.ReseniaDto;

/**
 * Servicio que gestiona las operaciones relacionadas con los productos en el sistema.
 * Permite obtener, agregar, modificar, eliminar productos, así como obtener reseñas de productos.
 */
public class ProductoServicio {

    /**
     * Obtiene la lista completa de productos desde la API.
     * 
     * @return Lista de productos.
     */
    public List<ProductoDto> obtenerProductos() {
        List<ProductoDto> productos = new ArrayList<>();
        try {
            URL url = new URL("http://localhost:8081/api/productos");
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
                    ProductoDto[] productosArray = mapper.readValue(response.toString(), ProductoDto[].class);
                    for (ProductoDto producto : productosArray) {
                        productos.add(producto);
                    }
                }
            } else {
                System.out.println("Error: Código de respuesta no OK. Código: " + responseCode);
            }
        } catch (Exception e) {
            System.out.println("ERROR: " + e);
            e.printStackTrace();
        }
        return productos;
    }

    /**
     * Obtiene un producto específico desde la API, dado su ID.
     * 
     * @param id El ID del producto.
     * @return El producto correspondiente o null si no se encuentra.
     */
    public ProductoDto obtenerProductoPorId(int id) {
        ProductoDto producto = null;
        try {
            URL url = new URL("http://localhost:8081/api/productos/" + id);
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
                    producto = mapper.readValue(response.toString(), ProductoDto.class);
                }
            } else {
                System.out.println("Error: Código de respuesta no OK. Código: " + responseCode);
            }
        } catch (Exception e) {
            System.out.println("ERROR: " + e);
            e.printStackTrace();
        }
        return producto;
    }

    /**
     * Agrega un nuevo producto a través de la API.
     * 
     * @param producto El producto que se desea agregar.
     * @return true si el producto fue agregado exitosamente, false en caso contrario.
     */
    public boolean agregarProducto(ProductoDto producto) {
        boolean resultado = false;
        try {
            URL url = new URL("http://localhost:8081/api/productos");
            HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
            conexion.setRequestMethod("POST");
            conexion.setRequestProperty("Content-Type", "application/json");
            conexion.setDoOutput(true);

            ObjectWriter writer = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String jsonProducto = writer.writeValueAsString(producto);

            try (OutputStream os = conexion.getOutputStream()) {
                os.write(jsonProducto.getBytes());
                os.flush();
            }

            int responseCode = conexion.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_CREATED) {
                resultado = true;
                System.out.println("Producto añadido exitosamente: " + jsonProducto);
            } else {
                System.out.println("Error al añadir el producto. Código de respuesta: " + responseCode);
            }
        } catch (Exception e) {
            System.out.println("ERROR: " + e);
            e.printStackTrace();
        }
        return resultado;
    }

    /**
     * Modifica un producto existente a través de la API.
     * 
     * @param id El ID del producto a modificar.
     * @param nombre Nuevo nombre del producto.
     * @param descripcion Nueva descripción del producto.
     * @param precio Nuevo precio del producto.
     * @param stock Nuevo stock del producto.
     * @param imagen Nueva imagen del producto en formato byte array.
     * @return true si el producto fue modificado exitosamente, false en caso contrario.
     */
    public boolean modificarProducto(Long id, String nombre, String descripcion, Double precio, Integer stock, byte[] imagen) {
        String API_URL_MODIFICAR = "http://localhost:8081/api/modificar/modificarProducto/";
        try {
            // Crear URL para la API de modificación
            URL url = new URL(API_URL_MODIFICAR + id);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=---123456789");
            connection.setDoOutput(true);

            // Construir cuerpo de la solicitud multipart
            String boundary = "---123456789";
            StringBuilder sb = new StringBuilder();

            if (nombre != null) {
                sb.append("--").append(boundary).append("\r\n");
                sb.append("Content-Disposition: form-data; name=\"nuevoNombre\"\r\n\r\n");
                sb.append(nombre).append("\r\n");
            }
            if (descripcion != null) {
                sb.append("--").append(boundary).append("\r\n");
                sb.append("Content-Disposition: form-data; name=\"nuevaDescripcion\"\r\n\r\n");
                sb.append(descripcion).append("\r\n");
            }
            if (precio != null) {
                sb.append("--").append(boundary).append("\r\n");
                sb.append("Content-Disposition: form-data; name=\"nuevoPrecio\"\r\n\r\n");
                sb.append(precio).append("\r\n");
            }
            if (stock != null) {
                sb.append("--").append(boundary).append("\r\n");
                sb.append("Content-Disposition: form-data; name=\"nuevoStock\"\r\n\r\n");
                sb.append(stock).append("\r\n");
            }
            if (imagen != null) {
                sb.append("--").append(boundary).append("\r\n");
                sb.append("Content-Disposition: form-data; name=\"nuevaImagen\"; filename=\"imagen.jpg\"\r\n");
                sb.append("Content-Type: image/jpeg\r\n\r\n");
            }

            byte[] textoBytes = sb.toString().getBytes(StandardCharsets.UTF_8);
            byte[] boundaryBytes = ("--" + boundary + "--\r\n").getBytes(StandardCharsets.UTF_8);

            // Enviar los datos al servidor
            try (OutputStream os = connection.getOutputStream()) {
                os.write(textoBytes);

                // Si hay una imagen, escribirla
                if (imagen != null) {
                    os.write(imagen);
                    os.write("\r\n".getBytes(StandardCharsets.UTF_8));
                }

                // Finalizar el cuerpo de la solicitud
                os.write(boundaryBytes);
            }

            // Verificar la respuesta del servidor
            int status = connection.getResponseCode();
            if (status == HttpURLConnection.HTTP_OK) {
                return true;
            } else {
                System.err.println("Error al modificar el producto: " + status);
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Elimina un producto a través de la API.
     * 
     * @param productoId El ID del producto a eliminar.
     * @return true si el producto fue eliminado exitosamente, false en caso contrario.
     */
    public boolean eliminarProducto(Long productoId) {
        boolean resultado = false;
        try {
            String API_BASE_URLEliminar = "http://localhost:8081/api/eliminar/";
            // Crear la URL completa para la solicitud DELETE
            URL url = new URL(API_BASE_URLEliminar + productoId);
            HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
            conexion.setRequestMethod("DELETE");
            conexion.setRequestProperty("Content-Type", "application/json");

            // Realizar la solicitud DELETE a la API
            int responseCode = conexion.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                resultado = true;
                System.out.println("Producto eliminado con éxito.");
            } else {
                System.out.println("Error: Código de respuesta no OK. Código: " + responseCode);
            }
        } catch (Exception e) {
            System.out.println("ERROR: " + e);
            e.printStackTrace();
        }
        return resultado;
    }

    /**
     * Obtiene las reseñas de un producto específico desde la API.
     * 
     * @param productoId El ID del producto para obtener sus reseñas.
     * @return Lista de reseñas del producto.
     */
    public List<ReseniaDto> obtenerReseniasPorProducto(int productoId) {
        List<ReseniaDto> resenias = new ArrayList<>();
        try {
            // URL de la API para obtener reseñas del producto
            URL url = new URL("http://localhost:8081/api/productos/" + productoId + "/resenias");
            HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
            conexion.setRequestMethod("GET");
            conexion.setRequestProperty("Content-Type", "application/json");

            // Leer la respuesta de la API
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
                System.out.println("Error: Código de respuesta no OK. Código: " + responseCode);
            }
        } catch (Exception e) {
            System.out.println("ERROR: " + e);
            e.printStackTrace();
        }
        return resenias;
    }
}
