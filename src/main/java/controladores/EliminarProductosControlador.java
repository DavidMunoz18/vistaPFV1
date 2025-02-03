package controladores;

import java.io.IOException;

import Servicios.ProductoServicio;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/eliminarProducto")
public class EliminarProductosControlador extends HttpServlet {

    private ProductoServicio productoServicio;

    @Override
    public void init() throws ServletException {
        super.init();
        // Inicializar el servicio
        productoServicio = new ProductoServicio(); // Cambiar por inyección manual o usar Spring si es posible
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String productoIdStr = request.getParameter("productoId");

        if (productoIdStr != null && !productoIdStr.isEmpty()) {
            try {
                Long productoId = Long.parseLong(productoIdStr); // Convertir a Long

                // Llamar al servicio para eliminar el producto
                productoServicio.eliminarProducto(productoId); // Pasamos el Long al servicio

                response.sendRedirect("menuAdministrador.jsp"); // Redirigir si todo está bien
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "El ID del producto no es válido.");
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID del producto no válido.");
        }
    }
}
