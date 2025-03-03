package servicios;

import dtos.CarritoDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import utilidades.Utilidades;

/**
 * Servicio encargado de manejar las operaciones relacionadas con el carrito de compras.
 * Permite obtener los productos del carrito, agregar productos, eliminar productos y 
 * obtener detalles de un producto por su ID.
 */
public class CarritoServicio {

    private static final String API_URL = "http://localhost:8081/api/carrito";

    /**
     * Obtiene todos los productos que están actualmente en el carrito.
     * Realiza una petición HTTP GET a la API y mapea la respuesta a una lista de CarritoDto.
     *
     * @return Lista de productos en el carrito.
     */
    public List<CarritoDto> obtenerCarrito() {
        List<CarritoDto> carrito = new ArrayList<>();
        Utilidades.escribirLog(null, "[INFO]", "CarritoServicio", "obtenerCarrito", "Inicio de obtención del carrito");
        try {
            URL url = new URL(API_URL);
            HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
            conexion.setRequestMethod("GET");
            conexion.setRequestProperty("Content-Type", "application/json");

            int responseCode = conexion.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                
                ObjectMapper mapper = new ObjectMapper();
                // Asegúrate de que la respuesta se mapea correctamente al DTO
                CarritoDto[] carritoArray = mapper.readValue(response.toString(), CarritoDto[].class);
                for (CarritoDto producto : carritoArray) {
                    carrito.add(producto);
                    // Imprimir el id de cada producto
                    System.out.println("Producto en el carrito: ID = " + producto.getId() + ", Nombre: " + producto.getNombre());
                }
                Utilidades.escribirLog(null, "[INFO]", "CarritoServicio", "obtenerCarrito", "Se obtuvieron " + carrito.size() + " productos del carrito");
            } else {
                Utilidades.escribirLog(null, "[ERROR]", "CarritoServicio", "obtenerCarrito", "Código de respuesta no OK: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Utilidades.escribirLog(null, "[ERROR]", "CarritoServicio", "obtenerCarrito", "Excepción: " + e.getMessage());
        }
        return carrito;
    }

    /**
     * Obtiene un producto del carrito dado su ID.
     * Realiza una petición HTTP GET a la API para obtener los detalles del producto.
     *
     * @param id El ID del producto a buscar.
     * @return El producto encontrado o null si no se encuentra.
     */
    public CarritoDto obtenerProductoPorId(long id) {
        Utilidades.escribirLog(null, "[INFO]", "CarritoServicio", "obtenerProductoPorId", "Inicio de búsqueda del producto con ID: " + id);
        try {
            // Verificar el ID recibido
            System.out.println("Buscando producto con ID: " + id);
            
            // Realizar la solicitud a la API
            URL url = new URL("http://localhost:8081/api/productos/" + id);
            HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
            conexion.setRequestMethod("GET");
            conexion.setRequestProperty("Content-Type", "application/json");

            // Leer la respuesta de la API
            BufferedReader in = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Imprimir la respuesta de la API para depurar
            System.out.println("Respuesta de la API: " + response.toString());

            // Convertir la respuesta JSON a un objeto CarritoDto
            ObjectMapper mapper = new ObjectMapper();
            CarritoDto producto = mapper.readValue(response.toString(), CarritoDto.class);

            // Verificar los datos del producto después de convertirlo
            if (producto != null) {
                System.out.println("Producto encontrado: ID = " + producto.getId() + ", Nombre: " + producto.getNombre());
                Utilidades.escribirLog(null, "[INFO]", "CarritoServicio", "obtenerProductoPorId", "Producto encontrado: ID = " + producto.getId() + ", Nombre: " + producto.getNombre());
            } else {
                System.out.println("No se encontró el producto con ID: " + id);
                Utilidades.escribirLog(null, "[ERROR]", "CarritoServicio", "obtenerProductoPorId", "No se encontró el producto con ID: " + id);
            }

            return producto;

        } catch (Exception e) {
            e.printStackTrace();
            Utilidades.escribirLog(null, "[ERROR]", "CarritoServicio", "obtenerProductoPorId", "Excepción: " + e.getMessage());
            return null;
        }
    }

    /**
     * Agrega un producto al carrito.
     * Realiza una petición HTTP POST a la API para agregar el producto al carrito.
     *
     * @param producto El producto a agregar al carrito.
     * @return true si el producto se agregó correctamente, false en caso contrario.
     */
    public boolean agregarProducto(CarritoDto producto) {
        Utilidades.escribirLog(null, "[INFO]", "CarritoServicio", "agregarProducto", "Inicio de agregar producto: ID = " + producto.getId() + ", Nombre: " + producto.getNombre());
        try {
            // Verificar que el producto tiene el id correcto antes de enviarlo
            System.out.println("Producto a agregar: ID = " + producto.getId() + ", Nombre: " + producto.getNombre());

            URL url = new URL(API_URL + "/agregar");
            HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
            conexion.setRequestMethod("POST");
            conexion.setRequestProperty("Content-Type", "application/json");
            conexion.setDoOutput(true);

            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(producto);

            // Imprimir el JSON a enviar para depuración
            System.out.println("JSON a enviar al carrito: " + json);

            conexion.getOutputStream().write(json.getBytes());

            int responseCode = conexion.getResponseCode();
            
            // Comprobar la respuesta del servidor
            if (responseCode == HttpURLConnection.HTTP_OK) {
                System.out.println("Producto agregado correctamente al carrito.");
                Utilidades.escribirLog(null, "[INFO]", "CarritoServicio", "agregarProducto", "Producto agregado correctamente al carrito.");
            } else {
                System.out.println("Error al agregar producto al carrito, código de respuesta: " + responseCode);
                Utilidades.escribirLog(null, "[ERROR]", "CarritoServicio", "agregarProducto", "Error al agregar producto al carrito, código de respuesta: " + responseCode);
            }

            return responseCode == HttpURLConnection.HTTP_OK;
        } catch (Exception e) {
            e.printStackTrace();
            Utilidades.escribirLog(null, "[ERROR]", "CarritoServicio", "agregarProducto", "Excepción: " + e.getMessage());
        }
        return false;
    }

    /**
     * Elimina un producto del carrito.
     * Realiza una petición HTTP DELETE a la API para eliminar el producto del carrito.
     *
     * @param id El ID del producto a eliminar.
     * @return true si el producto fue eliminado correctamente, false en caso contrario.
     */
    public boolean eliminarProducto(long id) {
        Utilidades.escribirLog(null, "[INFO]", "CarritoServicio", "eliminarProducto", "Inicio de eliminación del producto con ID: " + id);
        try {
            URL url = new URL(API_URL + "/eliminar/" + id);
            HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
            conexion.setRequestMethod("DELETE");
            conexion.setRequestProperty("Content-Type", "application/json");

            int responseCode = conexion.getResponseCode();
            
            if (responseCode == HttpURLConnection.HTTP_OK) {
                System.out.println("Producto eliminado correctamente del carrito.");
                Utilidades.escribirLog(null, "[INFO]", "CarritoServicio", "eliminarProducto", "Producto eliminado correctamente del carrito.");
            } else {
                System.out.println("Error al eliminar producto del carrito, código de respuesta: " + responseCode);
                Utilidades.escribirLog(null, "[ERROR]", "CarritoServicio", "eliminarProducto", "Error al eliminar producto del carrito, código de respuesta: " + responseCode);
            }
            return responseCode == HttpURLConnection.HTTP_OK;
        } catch (Exception e) {
            e.printStackTrace();
            Utilidades.escribirLog(null, "[ERROR]", "CarritoServicio", "eliminarProducto", "Excepción: " + e.getMessage());
        }
        return false;
    }
    /**
     * Limpia el carrito de compras.
     * Realiza una petición HTTP DELETE a la API para limpiar el carrito.
     *
     * @return true si el carrito se limpió correctamente, false en caso contrario.
     */
    public boolean limpiarCarrito() {
        Utilidades.escribirLog(null, "[INFO]", "CarritoServicio", "limpiarCarrito", "Inicio de limpieza del carrito");
        try {
            URL url = new URL(API_URL + "/limpiar");
            HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
            conexion.setRequestMethod("DELETE");
            conexion.setRequestProperty("Content-Type", "application/json");

            int responseCode = conexion.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                System.out.println("Carrito limpiado correctamente.");
                Utilidades.escribirLog(null, "[INFO]", "CarritoServicio", "limpiarCarrito", "Carrito limpiado correctamente.");
            } else {
                System.out.println("Error al limpiar carrito, código de respuesta: " + responseCode);
                Utilidades.escribirLog(null, "[ERROR]", "CarritoServicio", "limpiarCarrito", "Error al limpiar carrito, código de respuesta: " + responseCode);
            }
            return responseCode == HttpURLConnection.HTTP_OK;
        } catch (Exception e) {
            e.printStackTrace();
            Utilidades.escribirLog(null, "[ERROR]", "CarritoServicio", "limpiarCarrito", "Excepción: " + e.getMessage());
        }
        return false;
    }

}
