package controladores;

import java.io.IOException;
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
import utilidades.Utilidades; 

/**
 * Controlador que maneja las operaciones del carrito de compras.
 * Permite agregar y eliminar productos del carrito en la sesión.
 * La lógica de negocio se maneja en el servicio {@link CarritoServicio}.
 * 
 * @author dmp
 */
@WebServlet("/carrito")
public class CarritoControlador extends HttpServlet {

    // Se asume que este servicio es el de la API (solo persistencia)
    private CarritoServicio carritoServicio = new CarritoServicio();

    /**
     * Maneja la solicitud POST para agregar o eliminar productos del carrito.
     * 
     * @param request La solicitud HTTP.
     * @param response La respuesta HTTP.
     * @throws ServletException Si ocurre un error en la ejecución del servlet.
     * @throws IOException Si ocurre un error en la entrada o salida de datos.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        // Acción para agregar un producto al carrito
        if ("agregar".equals(action)) {
            try {
                // Log: inicio acción agregar
                Utilidades.escribirLog(request.getSession(), "[INFO]", "CarritoControlador", "doPost", "Inicio acción agregar producto");

                // Obtener parámetros enviados en el request
                long id = Long.parseLong(request.getParameter("id"));
                int cantidad = Integer.parseInt(request.getParameter("cantidad"));

                // Lógica de negocio: Validar que la cantidad sea mayor que cero
                if (cantidad <= 0) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Cantidad inválida");
                    return;
                }

                // Lógica de negocio: Obtener los detalles completos del producto.
                CarritoDto productoDetalles = carritoServicio.obtenerProductoPorId(id);
                if (productoDetalles == null) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Producto no encontrado");
                    return;
                }

                // Crear el objeto CarritoDto con la cantidad solicitada y demás detalles
                CarritoDto carritoDto = new CarritoDto(
                        productoDetalles.getId(),
                        productoDetalles.getNombre(),
                        cantidad,
                        productoDetalles.getPrecio(),
                        productoDetalles.getImagen()
                );

                // Gestión del carrito en la sesión
                HttpSession session = request.getSession();
                List<CarritoDto> carrito = (List<CarritoDto>) session.getAttribute("carrito");
                if (carrito == null) {
                    carrito = carritoServicio.obtenerCarrito();  // Se obtiene el carrito persistido
                }

                // Si el producto ya existe en el carrito, se actualiza la cantidad
                boolean productoExistente = false;
                for (CarritoDto producto : carrito) {
                    if (producto.getId() == id) {
                        producto.setCantidad(producto.getCantidad() + cantidad);
                        productoExistente = true;
                        break;
                    }
                }
                if (!productoExistente) {
                    carrito.add(carritoDto);
                }
                session.setAttribute("carrito", carrito);

                // Llamada a la API para persistir el producto (sin validación interna)
                carritoServicio.agregarProducto(carritoDto);

                // Log: producto agregado con éxito
                Utilidades.escribirLog(session, "[INFO]", "CarritoControlador", "doPost", "Producto agregado al carrito correctamente");

                session.setAttribute("productoAgregado", true);
                response.sendRedirect("productos");

            } catch (NumberFormatException e) {
                // Log: error de conversión de datos
                Utilidades.escribirLog(request.getSession(), "[ERROR]", "CarritoControlador", "doPost", "Error al convertir datos: " + e.getMessage());
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Datos inválidos");
            } catch (Exception e) {
                // Log: error interno
                Utilidades.escribirLog(request.getSession(), "[ERROR]", "CarritoControlador", "doPost", "Error interno del servidor: " + e.getMessage());
                e.printStackTrace();
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error interno del servidor");
            }
        // Acción para eliminar un producto del carrito
        } else if ("eliminar".equals(action)) {
            String method = request.getParameter("_method");
            if ("DELETE".equals(method)) {
                try {
                    // Log: inicio acción eliminar
                    Utilidades.escribirLog(request.getSession(), "[INFO]", "CarritoControlador", "doPost", "Inicio acción eliminar producto del carrito");

                    long id = Long.parseLong(request.getParameter("id"));
                    HttpSession session = request.getSession();
                    List<CarritoDto> carrito = (List<CarritoDto>) session.getAttribute("carrito");

                    if (carrito != null) {
                        carrito.removeIf(producto -> producto.getId() == id);
                        session.setAttribute("carrito", carrito);
                        carritoServicio.eliminarProducto((int) id);

                        // Log: producto eliminado con éxito
                        Utilidades.escribirLog(session, "[INFO]", "CarritoControlador", "doPost", "Producto eliminado del carrito correctamente");

                        response.sendRedirect("carrito");
                    } else {
                        // Log: carrito no encontrado
                        Utilidades.escribirLog(request.getSession(), "[ERROR]", "CarritoControlador", "doPost", "Carrito no encontrado al intentar eliminar producto");
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Carrito no encontrado");
                    }
                } catch (NumberFormatException e) {
                    // Log: error en formato de ID
                    Utilidades.escribirLog(request.getSession(), "[ERROR]", "CarritoControlador", "doPost", "ID inválido: " + e.getMessage());
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID inválido");
                } catch (Exception e) {
                    // Log: error al eliminar producto
                    Utilidades.escribirLog(request.getSession(), "[ERROR]", "CarritoControlador", "doPost", "Error al eliminar producto: " + e.getMessage());
                    e.printStackTrace();
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al eliminar producto");
                }
            }
        } else {
            // Log: acción no soportada
            Utilidades.escribirLog(request.getSession(), "[ERROR]", "CarritoControlador", "doPost", "Acción no soportada: " + action);
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Acción no soportada");
        }
    }

    /**
     * Maneja la solicitud GET para mostrar el carrito de compras.
     * 
     * @param request La solicitud HTTP.
     * @param response La respuesta HTTP.
     * @throws ServletException Si ocurre un error en la ejecución del servlet.
     * @throws IOException Si ocurre un error en la entrada o salida de datos.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        // Log: inicio acción doGet para mostrar carrito
        Utilidades.escribirLog(session, "[INFO]", "CarritoControlador", "doGet", "Mostrando carrito de compras");

        List<CarritoDto> carrito = (List<CarritoDto>) session.getAttribute("carrito");

        if (carrito == null) {
            carrito = carritoServicio.obtenerCarrito();
        }

        request.setAttribute("carrito", carrito);
        RequestDispatcher dispatcher = request.getRequestDispatcher("carrito.jsp");
        dispatcher.forward(request, response);
    }
}
