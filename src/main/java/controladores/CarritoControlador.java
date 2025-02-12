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

@WebServlet("/carrito")
public class CarritoControlador extends HttpServlet {

    private CarritoServicio carritoServicio = new CarritoServicio();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("agregar".equals(action)) {
            try {
                long id = Long.parseLong(request.getParameter("id"));
                int cantidad = Integer.parseInt(request.getParameter("cantidad"));

                // Obtener los detalles del producto
                CarritoDto productoDetalles = carritoServicio.obtenerProductoPorId(id);
                
                if (productoDetalles != null) {
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

                    response.sendRedirect("carrito");  // Redirigir al carrito

                } else {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Producto no encontrado");
                }

            } catch (Exception e) {
                e.printStackTrace();
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Datos inválidos");
            }
        } else if ("eliminar".equals(action)) {
            // Verificar el spoofing del método
            String method = request.getParameter("_method");
            if ("DELETE".equals(method)) {
                try {
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

                        response.sendRedirect("carrito");  // Redirigir al carrito
                    } else {
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Carrito no encontrado");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Error al eliminar producto");
                }
            }
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Acción no soportada");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Obtener el carrito de la sesión
        HttpSession session = request.getSession();
        List<CarritoDto> carrito = (List<CarritoDto>) session.getAttribute("carrito");

        // Imprimir datos recibidos para depuración
        if (carrito != null) {
            for (CarritoDto producto : carrito) {
                System.out.println("Producto en carrito: " + producto.getNombre() + ", Precio: " + producto.getPrecio());
            }
        }

        request.setAttribute("carrito", carrito); // Pasar los productos al JSP
        RequestDispatcher dispatcher = request.getRequestDispatcher("carrito.jsp");
        dispatcher.forward(request, response);
    }
}
