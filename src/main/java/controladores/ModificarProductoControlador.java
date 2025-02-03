package controladores;

import Servicios.ProductoServicio;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.IOException;

/**
 * Controlador para manejar la modificación de productos con datos multipart.
 */
@WebServlet("/modificarProducto")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10,      // 10MB
        maxRequestSize = 1024 * 1024 * 50   // 50MB
)
public class ModificarProductoControlador extends HttpServlet {

    private ProductoServicio productoServicio;

    @Override
    public void init() throws ServletException {
        productoServicio = new ProductoServicio();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Obtener los parámetros del formulario
            Long idProducto = Long.parseLong(request.getParameter("id"));
            String nuevoNombre = request.getParameter("nombre");
            String nuevaDescripcion = request.getParameter("descripcion");
            String nuevoPrecioStr = request.getParameter("precio");
            String nuevoStockStr = request.getParameter("stock");
            Part nuevaImagenPart = request.getPart("imagen");

            Double nuevoPrecio = nuevoPrecioStr != null && !nuevoPrecioStr.isEmpty() ? Double.parseDouble(nuevoPrecioStr) : null;
            Integer nuevoStock = nuevoStockStr != null && !nuevoStockStr.isEmpty() ? Integer.parseInt(nuevoStockStr) : null;

            byte[] nuevaImagenBytes = null;
            if (nuevaImagenPart != null && nuevaImagenPart.getSize() > 0) {
                nuevaImagenBytes = nuevaImagenPart.getInputStream().readAllBytes();
            }

            // Llamar al servicio para realizar la modificación
            boolean resultado = productoServicio.modificarProducto(idProducto, nuevoNombre, nuevaDescripcion, nuevoPrecio, nuevoStock, nuevaImagenBytes);

            // Redirigir dependiendo del resultado
            if (resultado) {
                response.sendRedirect("exito.jsp");
            } else {
                request.setAttribute("error", "No se encontró el producto o no se pudo modificar.");
                request.getRequestDispatcher("error.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Ocurrió un error al intentar modificar el producto.");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }
}
