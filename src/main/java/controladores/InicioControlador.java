package controladores;

import java.io.IOException;
import java.util.List;

import dtos.ProductoDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import servicios.ProductoServicio;

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
        // Crear una instancia del servicio de productos
        ProductoServicio productoServicio = new ProductoServicio();
        
        // Obtener la lista de productos desde el servicio
        List<ProductoDto> productos = productoServicio.obtenerProductos();

        if (productos != null && !productos.isEmpty()) {
            // Si hay productos disponibles, pasarlos a la vista
            request.setAttribute("productos", productos);
            request.getRequestDispatcher("/index.jsp").forward(request, response);
        } else {
            // Si no hay productos disponibles, mostrar un mensaje en la vista
            request.setAttribute("mensaje", "No hay productos disponibles.");
            request.getRequestDispatcher("/index.jsp").forward(request, response);
        }
    }
}
