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
import utilidades.Utilidades; // Asegúrate de que este sea el paquete correcto para la utilidad

/**
 * Servicio que contiene la lógica de negocio para la gestión de productos.
 * Se conecta a la API (que es solo de persistencia) y aplica validaciones,
 * comprobaciones y logging (escribiendo en fichero si se pasa null) antes y después de invocar los endpoints.
 */
public class ProductoServicio {

    private static final String API_BASE_URL = "http://localhost:8081/api/";

    /**
     * Obtiene la lista completa de productos desde la API y realiza validaciones.
     */
    public List<ProductoDto> obtenerProductos() {
        Utilidades.escribirLog(null, "[INFO]", "ProductoServicio", "obtenerProductos", "Iniciando obtención de productos...");
        List<ProductoDto> productos = new ArrayList<>();
        try {
            URL url = new URL(API_BASE_URL + "productos");
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
                    if (productosArray == null || productosArray.length == 0) {
                        Utilidades.escribirLog(null, "[INFO]", "ProductoServicio", "obtenerProductos", "No se encontraron productos en la API.");
                    } else {
                        for (ProductoDto producto : productosArray) {
                            if (producto.getNombre() == null || producto.getNombre().isBlank()) {
                                Utilidades.escribirLog(null, "[WARN]", "ProductoServicio", "obtenerProductos", "Producto sin nombre encontrado.");
                            } else {
                                Utilidades.escribirLog(null, "[INFO]", "ProductoServicio", "obtenerProductos", "Producto obtenido: " + producto.getNombre());
                            }
                            productos.add(producto);
                        }
                    }
                }
            } else {
                Utilidades.escribirLog(null, "[ERROR]", "ProductoServicio", "obtenerProductos", "Código de respuesta: " + responseCode);
            }
        } catch (Exception e) {
            Utilidades.escribirLog(null, "[ERROR]", "ProductoServicio", "obtenerProductos", "Excepción: " + e);
            e.printStackTrace();
        }
        return productos;
    }

    /**
     * Obtiene un producto específico desde la API, dado su ID, y realiza validaciones.
     */
    public ProductoDto obtenerProductoPorId(int id) {
        Utilidades.escribirLog(null, "[INFO]", "ProductoServicio", "obtenerProductoPorId", "Buscando producto con ID: " + id);
        ProductoDto producto = null;
        try {
            URL url = new URL(API_BASE_URL + "productos/" + id);
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
                    if (producto != null) {
                        Utilidades.escribirLog(null, "[INFO]", "ProductoServicio", "obtenerProductoPorId", "Producto encontrado: " + producto.getNombre());
                    } else {
                        Utilidades.escribirLog(null, "[WARN]", "ProductoServicio", "obtenerProductoPorId", "Producto no encontrado con ID: " + id);
                    }
                }
            } else {
                Utilidades.escribirLog(null, "[ERROR]", "ProductoServicio", "obtenerProductoPorId", "Código de respuesta: " + responseCode);
            }
        } catch (Exception e) {
            Utilidades.escribirLog(null, "[ERROR]", "ProductoServicio", "obtenerProductoPorId", "Excepción: " + e);
            e.printStackTrace();
        }
        return producto;
    }

    /**
     * Agrega un nuevo producto a través de la API tras validar los datos.
     */
    public boolean agregarProducto(ProductoDto producto) {
        if (producto == null || producto.getNombre() == null || producto.getNombre().isBlank()) {
            Utilidades.escribirLog(null, "[ERROR]", "ProductoServicio", "agregarProducto", "Producto inválido: se requiere un nombre.");
            return false;
        }
        Utilidades.escribirLog(null, "[INFO]", "ProductoServicio", "agregarProducto", "Agregando producto: " + producto.getNombre());
        boolean resultado = false;
        try {
            URL url = new URL(API_BASE_URL + "productos");
            HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
            conexion.setRequestMethod("POST");
            conexion.setRequestProperty("Content-Type", "application/json");
            conexion.setDoOutput(true);
            ObjectWriter writer = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String jsonProducto = writer.writeValueAsString(producto);
            try (OutputStream os = conexion.getOutputStream()) {
                os.write(jsonProducto.getBytes(StandardCharsets.UTF_8));
                os.flush();
            }
            int responseCode = conexion.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_CREATED) {
                Utilidades.escribirLog(null, "[INFO]", "ProductoServicio", "agregarProducto", "Producto añadido exitosamente: " + producto.getNombre());
                resultado = true;
            } else {
                Utilidades.escribirLog(null, "[ERROR]", "ProductoServicio", "agregarProducto", "Código de respuesta: " + responseCode);
            }
        } catch (Exception e) {
            Utilidades.escribirLog(null, "[ERROR]", "ProductoServicio", "agregarProducto", "Excepción: " + e);
            e.printStackTrace();
        }
        return resultado;
    }

    /**
     * Modifica un producto existente a través de la API tras realizar validaciones.
     */
    public boolean modificarProducto(Long id, String nombre, String descripcion, Double precio, Integer stock, byte[] imagen) {
        Utilidades.escribirLog(null, "[INFO]", "ProductoServicio", "modificarProducto", "Iniciando modificación del producto con ID: " + id);
        if (id == null || id <= 0) {
            Utilidades.escribirLog(null, "[ERROR]", "ProductoServicio", "modificarProducto", "ID inválido.");
            return false;
        }
        if (nombre == null || nombre.isBlank()) {
            Utilidades.escribirLog(null, "[ERROR]", "ProductoServicio", "modificarProducto", "El nombre del producto es obligatorio.");
            return false;
        }
        String API_URL_MODIFICAR = API_BASE_URL + "modificar/modificarProducto/";
        try {
            URL url = new URL(API_URL_MODIFICAR + id);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=---123456789");
            connection.setDoOutput(true);
            String boundary = "---123456789";
            StringBuilder sb = new StringBuilder();
            // Agregar nombre (obligatorio)
            sb.append("--").append(boundary).append("\r\n");
            sb.append("Content-Disposition: form-data; name=\"nuevoNombre\"\r\n\r\n");
            sb.append(nombre).append("\r\n");
            // Agregar descripción si se proporciona
            if (descripcion != null && !descripcion.isBlank()) {
                sb.append("--").append(boundary).append("\r\n");
                sb.append("Content-Disposition: form-data; name=\"nuevaDescripcion\"\r\n\r\n");
                sb.append(descripcion).append("\r\n");
            }
            // Agregar precio si se proporciona
            if (precio != null) {
                sb.append("--").append(boundary).append("\r\n");
                sb.append("Content-Disposition: form-data; name=\"nuevoPrecio\"\r\n\r\n");
                sb.append(precio).append("\r\n");
            }
            // Agregar stock si se proporciona
            if (stock != null) {
                sb.append("--").append(boundary).append("\r\n");
                sb.append("Content-Disposition: form-data; name=\"nuevoStock\"\r\n\r\n");
                sb.append(stock).append("\r\n");
            }
            // Agregar imagen si se proporciona
            if (imagen != null && imagen.length > 0) {
                sb.append("--").append(boundary).append("\r\n");
                sb.append("Content-Disposition: form-data; name=\"nuevaImagen\"; filename=\"imagen.jpg\"\r\n");
                sb.append("Content-Type: image/jpeg\r\n\r\n");
            }
            byte[] textoBytes = sb.toString().getBytes(StandardCharsets.UTF_8);
            byte[] boundaryBytes = ("--" + boundary + "--\r\n").getBytes(StandardCharsets.UTF_8);
            try (OutputStream os = connection.getOutputStream()) {
                os.write(textoBytes);
                if (imagen != null && imagen.length > 0) {
                    os.write(imagen);
                    os.write("\r\n".getBytes(StandardCharsets.UTF_8));
                }
                os.write(boundaryBytes);
            }
            int status = connection.getResponseCode();
            if (status == HttpURLConnection.HTTP_OK) {
                Utilidades.escribirLog(null, "[INFO]", "ProductoServicio", "modificarProducto", "Producto modificado exitosamente con ID: " + id);
                return true;
            } else {
                Utilidades.escribirLog(null, "[ERROR]", "ProductoServicio", "modificarProducto", "Código de respuesta: " + status);
                return false;
            }
        } catch (Exception e) {
            Utilidades.escribirLog(null, "[ERROR]", "ProductoServicio", "modificarProducto", "Excepción: " + e);
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Elimina un producto a través de la API tras validar el ID.
     */
    public boolean eliminarProducto(Long productoId) {
        Utilidades.escribirLog(null, "[INFO]", "ProductoServicio", "eliminarProducto", "Iniciando eliminación del producto con ID: " + productoId);
        if (productoId == null || productoId <= 0) {
            Utilidades.escribirLog(null, "[ERROR]", "ProductoServicio", "eliminarProducto", "ID inválido.");
            return false;
        }
        boolean resultado = false;
        try {
            URL url = new URL(API_BASE_URL + "eliminar/" + productoId);
            HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
            conexion.setRequestMethod("DELETE");
            conexion.setRequestProperty("Content-Type", "application/json");
            int responseCode = conexion.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                Utilidades.escribirLog(null, "[INFO]", "ProductoServicio", "eliminarProducto", "Producto eliminado exitosamente con ID: " + productoId);
                resultado = true;
            } else {
                Utilidades.escribirLog(null, "[ERROR]", "ProductoServicio", "eliminarProducto", "Código de respuesta: " + responseCode);
            }
        } catch (Exception e) {
            Utilidades.escribirLog(null, "[ERROR]", "ProductoServicio", "eliminarProducto", "Excepción: " + e);
            e.printStackTrace();
        }
        return resultado;
    }

    /**
     * Obtiene las reseñas de un producto específico desde la API y aplica lógica de negocio.
     */
    public List<ReseniaDto> obtenerReseniasPorProducto(int productoId) {
        Utilidades.escribirLog(null, "[INFO]", "ProductoServicio", "obtenerReseniasPorProducto", "Obteniendo reseñas para el producto con ID: " + productoId);
        List<ReseniaDto> resenias = new ArrayList<>();
        try {
            URL url = new URL(API_BASE_URL + "productos/" + productoId + "/resenias");
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
                    if (reseniasArray != null) {
                        for (ReseniaDto resenia : reseniasArray) {
                            resenias.add(resenia);
                        }
                        Utilidades.escribirLog(null, "[INFO]", "ProductoServicio", "obtenerReseniasPorProducto", "Se obtuvieron " + reseniasArray.length + " reseñas.");
                    } else {
                        Utilidades.escribirLog(null, "[INFO]", "ProductoServicio", "obtenerReseniasPorProducto", "No se encontraron reseñas para el producto con ID: " + productoId);
                    }
                }
            } else {
                Utilidades.escribirLog(null, "[ERROR]", "ProductoServicio", "obtenerReseniasPorProducto", "Código de respuesta: " + responseCode);
            }
        } catch (Exception e) {
            Utilidades.escribirLog(null, "[ERROR]", "ProductoServicio", "obtenerReseniasPorProducto", "Excepción: " + e);
            e.printStackTrace();
        }
        return resenias;
    }
}
