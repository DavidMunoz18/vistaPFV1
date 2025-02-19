package controladores;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import dtos.CarritoDto;
import servicios.CarritoServicio;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Controlador para gestionar las operaciones del carrito de compras.
 * Este servlet maneja la adición y eliminación de productos en el carrito,
 * así como la visualización de los productos del carrito.
 */
@WebServlet("/carrito")
public class CarritoControlador extends HttpServlet {

    private CarritoServicio carritoServicio = new CarritoServicio();

    /**
     * Maneja las solicitudes POST para agregar o eliminar productos del carrito.
     * 
     * @param request La solicitud HTTP.
     * @param response La respuesta HTTP.
     * @throws ServletException Si ocurre un error durante la manipulación de la solicitud.
     * @throws IOException Si ocurre un error de entrada/salida.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("agregar".equals(action)) {
            try {
                // Obtener los parámetros del producto a agregar
                long id = Long.parseLong(request.getParameter("id"));
                int cantidad = Integer.parseInt(request.getParameter("cantidad"));

                // Obtener los detalles del producto desde el servicio
                CarritoDto productoDetalles = carritoServicio.obtenerProductoPorId(id);
                
                if (productoDetalles != null) {
                    // Crear un objeto CarritoDto con los detalles del producto y la cantidad
                    CarritoDto carritoDto = new CarritoDto(
                            productoDetalles.getId(),
                            productoDetalles.getNombre(),
                            cantidad,
                            productoDetalles.getPrecio(),
                            productoDetalles.getImagen()
                    );

                    // Obtener el carrito de la sesión
                    HttpSession session = request.getSession();
                    List<CarritoDto> carrito = (List<CarritoDto>) session.getAttribute("carrito");

                    // Si el carrito no existe, crear uno nuevo
                    if (carrito == null) {
                        carrito = new ArrayList<>();
                    }

                    // Agregar el producto al carrito
                    carrito.add(carritoDto);
                    
                    // Guardar el carrito actualizado en la sesión
                    session.setAttribute("carrito", carrito);

                    // Redirigir a la vista del carrito
                    response.sendRedirect("carrito");

                } else {
                    // Si no se encuentra el producto, enviar error
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Producto no encontrado");
                }

            } catch (Exception e) {
                e.printStackTrace();
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Datos inválidos");
            }
        } else if ("eliminar".equals(action)) {
            // Verificar el método spoofing (eliminar producto)
            String method = request.getParameter("_method");
            if ("DELETE".equals(method)) {
                try {
                    // Obtener el ID del producto a eliminar
                    long id = Long.parseLong(request.getParameter("id"));

                    // Obtener el carrito de la sesión
                    HttpSession session = request.getSession();
                    List<CarritoDto> carrito = (List<CarritoDto>) session.getAttribute("carrito");

                    // Si el carrito existe, proceder con la eliminación
                    if (carrito != null) {
                        // Buscar el producto por ID y eliminarlo
                        carrito.removeIf(producto -> producto.getId() == id);

                        // Guardar el carrito actualizado en la sesión
                        session.setAttribute("carrito", carrito);

                        // Redirigir a la vista del carrito
                        response.sendRedirect("carrito");
                    } else {
                        // Si el carrito no existe, enviar error
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Carrito no encontrado");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Error al eliminar producto");
                }
            }
        } else {
            // Si la acción no está soportada, enviar error
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Acción no soportada");
        }
    }

    /**
     * Maneja las solicitudes GET para mostrar el contenido del carrito.
     * 
     * @param request La solicitud HTTP.
     * @param response La respuesta HTTP.
     * @throws ServletException Si ocurre un error durante la manipulación de la solicitud.
     * @throws IOException Si ocurre un error de entrada/salida.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Obtener el carrito de la sesión
        HttpSession session = request.getSession();
        List<CarritoDto> carrito = (List<CarritoDto>) session.getAttribute("carrito");

        // Imprimir los productos del carrito para depuración
        if (carrito != null) {
            for (CarritoDto producto : carrito) {
                System.out.println("Producto en carrito: " + producto.getNombre() + ", Precio: " + producto.getPrecio());
            }
        }

        // Pasar los productos al JSP para su visualización
        request.setAttribute("carrito", carrito);
        RequestDispatcher dispatcher = request.getRequestDispatcher("carrito.jsp");
        dispatcher.forward(request, response);
    }
}
