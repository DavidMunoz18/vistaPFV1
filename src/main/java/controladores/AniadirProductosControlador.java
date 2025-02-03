package controladores;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;

import com.gestion.gestion_usuarios.dtos.ProductoDto;
import Servicios.ProductoServicio;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

@WebServlet("/productosAniadir")
@MultipartConfig
public class AniadirProductosControlador extends HttpServlet {

    private ProductoServicio productoServicio;

    @Override
    public void init() throws ServletException {
        // Inicializar manualmente el servicio aquí
        productoServicio = new ProductoServicio(); // Suponiendo que el servicio tiene un constructor sin parámetros
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Obtener datos del formulario
            String nombre = request.getParameter("nombre");
            String descripcion = request.getParameter("descripcion");
            String precioStr = request.getParameter("precio");
            String stockStr = request.getParameter("stock");

            // Validar parámetros obligatorios
            if (nombre == null || descripcion == null || precioStr == null || stockStr == null ||
                nombre.isEmpty() || descripcion.isEmpty() || precioStr.isEmpty() || stockStr.isEmpty()) {
                response.sendRedirect("productos.jsp?error=Todos los campos son obligatorios");
                return;
            }

            // Convertir valores
            double precio = Double.parseDouble(precioStr);
            int stock = Integer.parseInt(stockStr);

            // Procesar archivo de imagen
            Part imagenPart = request.getPart("imagen");
            byte[] imagenBytes = null;

            if (imagenPart != null && imagenPart.getSize() > 0) {
                try (InputStream inputStream = imagenPart.getInputStream()) {
                    imagenBytes = inputStream.readAllBytes();
                }
            }

            // Crear objeto DTO
            ProductoDto producto = new ProductoDto(null, nombre, descripcion, precio, imagenBytes, stock);

            // Llamar al servicio para agregar el producto
            boolean exito = productoServicio.agregarProducto(producto);

            // Redirigir con mensaje según el resultado
            if (exito) {
            	response.sendRedirect("productos");;
            } else {
            	response.sendRedirect("productos");;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            response.sendRedirect("productos");;
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("productos");;
        }
    }
}
