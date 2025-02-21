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

@WebServlet("/carrito")
public class CarritoControlador extends HttpServlet {

    // Se asume que este servicio es el de la API (solo persistencia)
    private CarritoServicio carritoServicio = new CarritoServicio();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("agregar".equals(action)) {
            try {
                // Obtener parámetros enviados en el request
                long id = Long.parseLong(request.getParameter("id"));
                int cantidad = Integer.parseInt(request.getParameter("cantidad"));

                // Lógica de negocio: Validar que la cantidad sea mayor que cero
                if (cantidad <= 0) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Cantidad inválida");
                    return;
                }

                // Lógica de negocio: Obtener los detalles completos del producto.
                // Este método debe estar implementado en la capa de negocio del Dynamic Web.
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

                // Redirigir a la vista del carrito
                response.sendRedirect("carrito");

            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Datos inválidos");
            } catch (Exception e) {
                e.printStackTrace();
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error interno del servidor");
            }
        } else if ("eliminar".equals(action)) {
            String method = request.getParameter("_method");
            if ("DELETE".equals(method)) {
                try {
                    long id = Long.parseLong(request.getParameter("id"));
                    HttpSession session = request.getSession();
                    List<CarritoDto> carrito = (List<CarritoDto>) session.getAttribute("carrito");

                    if (carrito != null) {
                        carrito.removeIf(producto -> producto.getId() == id);
                        session.setAttribute("carrito", carrito);
                        carritoServicio.eliminarProducto((int) id);
                        response.sendRedirect("carrito");
                    } else {
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Carrito no encontrado");
                    }
                } catch (NumberFormatException e) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID inválido");
                } catch (Exception e) {
                    e.printStackTrace();
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al eliminar producto");
                }
            }
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Acción no soportada");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        List<CarritoDto> carrito = (List<CarritoDto>) session.getAttribute("carrito");

        if (carrito == null) {
            carrito = carritoServicio.obtenerCarrito();
        }

        request.setAttribute("carrito", carrito);
        RequestDispatcher dispatcher = request.getRequestDispatcher("carrito.jsp");
        dispatcher.forward(request, response);
    }
}
