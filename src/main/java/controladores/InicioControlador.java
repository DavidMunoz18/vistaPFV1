package controladores;

import java.io.IOException;
import java.util.List;

import dtos.ProductoDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import servicios.ProductoServicio;
import utilidades.Utilidades; // Import para usar el método escribirLog

/**
 * Controlador que maneja las solicitudes para la página de inicio del ecommerce.
 * Este servlet obtiene una lista de productos y los pasa a la vista correspondiente.
 */
@WebServlet("/inicio")
public class InicioControlador extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * Maneja las solicitudes GET para la página de inicio.
     * Obtiene la lista de productos disponibles y los envía a la vista.
     * 
     * @param request La solicitud HTTP recibida.
     * @param response La respuesta HTTP que será enviada al cliente.
     * @throws ServletException Si ocurre un error al procesar la solicitud.
     * @throws IOException Si ocurre un error de entrada/salida durante el procesamiento.
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        
        // Log: Inicio del proceso de obtención de productos
        Utilidades.escribirLog(session, "[INFO]", "InicioControlador", "doGet", "Inicio del proceso de obtención de productos");

        // Crear una instancia del servicio de productos
        ProductoServicio productoServicio = new ProductoServicio();

        try {
            // Obtener la lista de productos desde el servicio
            List<ProductoDto> productos = productoServicio.obtenerProductos();

            if (productos != null && !productos.isEmpty()) {
                // Si hay productos disponibles, pasarlos a la vista
                request.setAttribute("productos", productos);
                // Log: Productos disponibles
                Utilidades.escribirLog(session, "[INFO]", "InicioControlador", "doGet", "Productos obtenidos: " + productos.size());
            } else {
                // Si no hay productos disponibles, mostrar un mensaje en la vista
                request.setAttribute("mensaje", "No hay productos disponibles.");
                // Log: No hay productos disponibles
                Utilidades.escribirLog(session, "[INFO]", "InicioControlador", "doGet", "No hay productos disponibles.");
            }

            request.getRequestDispatcher("/index.jsp").forward(request, response);
            
        } catch (Exception e) {
            // Log: Error en la obtención de productos
            Utilidades.escribirLog(session, "[ERROR]", "InicioControlador", "doGet", "Error al obtener los productos: " + e.getMessage());
            // Manejo de errores en la obtención de productos
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al obtener los productos.");
        }
    }
}
