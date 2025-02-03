package controladores;

import java.io.IOException;
import java.util.List;

import Dtos.CarritoDto;
import Servicios.CarritoServicio;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/carrito")
public class CarritoControlador extends HttpServlet {

    private CarritoServicio carritoServicio = new CarritoServicio();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("agregar".equals(action)) {
            try {
                // Cambio aquí: usar 'id' en lugar de 'productoId'
                int id = Integer.parseInt(request.getParameter("id"));
                System.out.println("Producto ID recibido: " + request.getParameter("id"));
                
                int cantidad = Integer.parseInt(request.getParameter("cantidad"));

                // Obtener los detalles del producto (nombre, precio, imagen) desde la API
                CarritoDto productoDetalles = carritoServicio.obtenerProductoPorId(id);
                
                if (productoDetalles != null) {
                    // Usamos el constructor completo de CarritoDto
                    CarritoDto carritoDto = new CarritoDto(
                            productoDetalles.getId(),  // Ahora 'id' en lugar de 'productoId'
                            productoDetalles.getNombre(),
                            cantidad,
                            productoDetalles.getPrecio(),
                            productoDetalles.getImagen()
                    );
                    
                    // Imprimir el carritoDto antes de insertarlo
                    System.out.println("Antes de agregar al carrito: " + carritoDto);

                    // Llamar al servicio para añadir el producto
                    boolean agregado = carritoServicio.agregarProducto(carritoDto);

                    // Verificar la inserción
                    if (agregado) {
                        // Imprimir el carritoDto después de agregarlo (si el servicio devuelve el ID actualizado)
                        System.out.println("Producto añadido al carrito correctamente: " + carritoDto);
                        response.sendRedirect("carrito");
                    } else {
                        System.out.println("Error al añadir el producto al carrito.");
                    }
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
                    // Cambio aquí: usar 'id' en lugar de 'productoId'
                    int id = Integer.parseInt(request.getParameter("id"));

                    // Llamar al servicio para eliminar el producto
                    boolean eliminado = carritoServicio.eliminarProducto(id);

                    if (eliminado) {
                        System.out.println("Producto eliminado del carrito.");
                    } else {
                        System.out.println("Error al eliminar el producto.");
                    }

                    // Redirigir al carrito
                    response.sendRedirect("carrito");

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
        List<CarritoDto> carrito = carritoServicio.obtenerCarrito();
        
        // Imprimir datos recibidos para depuración
        for (CarritoDto producto : carrito) {
            System.out.println("Producto en carrito: " + producto.getNombre() + ", Precio: " + producto.getPrecio());
        }

        request.setAttribute("carrito", carrito);  // Pasar los productos al JSP

        RequestDispatcher dispatcher = request.getRequestDispatcher("carrito.jsp");
        dispatcher.forward(request, response);
    }
}
