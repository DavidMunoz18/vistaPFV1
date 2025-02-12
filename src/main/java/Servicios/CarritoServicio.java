package servicios;

import dtos.CarritoDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CarritoServicio {

    private static final String API_URL = "http://localhost:8081/api/carrito";

    public List<CarritoDto> obtenerCarrito() {
        List<CarritoDto> carrito = new ArrayList<>();
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
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return carrito;
    }

    public CarritoDto obtenerProductoPorId(long id) {
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
                System.out.println("Producto encontrado: ID = " + producto.getId() + ", Nombre = " + producto.getNombre());
            } else {
                System.out.println("No se encontró el producto con ID: " + id);
            }

            return producto;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean agregarProducto(CarritoDto producto) {
        try {
            // Verificar que el producto tiene el id correcto antes de enviarlo
            System.out.println("Producto a agregar: ID = " + producto.getId() + ", Nombre = " + producto.getNombre());

            URL url = new URL(API_URL + "/add");
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
            } else {
                System.out.println("Error al agregar producto al carrito, código de respuesta: " + responseCode);
            }

            return responseCode == HttpURLConnection.HTTP_OK;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean eliminarProducto(long id) {
        try {
            URL url = new URL(API_URL + "/remove/" + id);
            HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
            conexion.setRequestMethod("DELETE");
            conexion.setRequestProperty("Content-Type", "application/json");

            int responseCode = conexion.getResponseCode();
            
            if (responseCode == HttpURLConnection.HTTP_OK) {
                System.out.println("Producto eliminado correctamente del carrito.");
            } else {
                System.out.println("Error al eliminar producto del carrito, código de respuesta: " + responseCode);
            }
            return responseCode == HttpURLConnection.HTTP_OK;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}