package controladores;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import servicios.ProductoServicio;

/**
 * Controlador para gestionar la eliminación de productos del sistema.
 * Este servlet se encarga de eliminar un producto a través de su ID, recibiendo la solicitud desde el formulario de administración.
 */
@WebServlet("/eliminarProducto")
public class EliminarProductosControlador extends HttpServlet {

    private ProductoServicio productoServicio;

    /**
     * Inicializa el servlet y configura el servicio de productos.
     * 
     * @throws ServletException Si ocurre un error al inicializar el servlet.
     */
    @Override
    public void init() throws ServletException {
        super.init();
        // Inicializar el servicio de productos
        productoServicio = new ProductoServicio(); // Se puede cambiar por inyección manual o usar Spring si es posible
    }

    /**
     * Maneja las solicitudes POST para eliminar un producto.
     * 
     * @param request La solicitud HTTP que contiene el ID del producto a eliminar.
     * @param response La respuesta HTTP para redirigir o mostrar errores.
     * @throws ServletException Si ocurre un error durante la manipulación de la solicitud.
     * @throws IOException Si ocurre un error de entrada/salida.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Obtener el ID del producto desde los parámetros de la solicitud
        String productoIdStr = request.getParameter("productoId");

        if (productoIdStr != null && !productoIdStr.isEmpty()) {
            try {
                // Convertir el ID del producto a tipo Long
                Long productoId = Long.parseLong(productoIdStr);

                // Llamar al servicio para eliminar el producto por su ID
                productoServicio.eliminarProducto(productoId);

                // Redirigir a la página de administración después de eliminar el producto
                response.sendRedirect("admin");

            } catch (NumberFormatException e) {
                // Si el ID no es válido, enviar un error de solicitud
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "El ID del producto no es válido.");
            }
        } else {
            // Si no se ha proporcionado un ID, enviar un error de solicitud
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID del producto no válido.");
        }
    }
}
