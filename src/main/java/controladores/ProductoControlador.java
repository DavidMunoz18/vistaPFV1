package controladores;

import java.io.IOException;
import java.util.List;

import Dtos.ProductoDto;
import Servicios.ProductoServicio;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/productos")
public class ProductoControlador extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Método para manejar la lista de productos
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idProducto = request.getParameter("id");

        if (idProducto != null && !idProducto.isEmpty()) {
            // Si se pasa un ID de producto, obtener el detalle del producto
            ProductoServicio productoServicio = new ProductoServicio();
            ProductoDto producto = productoServicio.obtenerProductoPorId(Integer.parseInt(idProducto));

            if (producto != null) {
                request.setAttribute("producto", producto);
                request.getRequestDispatcher("/detallesProducto.jsp").forward(request, response);
            } else {
                // Si no se encuentra el producto, mostrar un mensaje de error
                request.setAttribute("mensaje", "Producto no encontrado.");
                request.getRequestDispatcher("/error.jsp").forward(request, response);
            }
        } else {
            // Si no se pasa ID, mostrar la lista de productos
            ProductoServicio productoServicio = new ProductoServicio();
            List<ProductoDto> productos = productoServicio.obtenerProductos();
            
            if (productos != null && !productos.isEmpty()) {
                request.setAttribute("productos", productos);
                request.getRequestDispatcher("/productos.jsp").forward(request, response);
            } else {
                request.setAttribute("mensaje", "No hay productos disponibles.");
                request.getRequestDispatcher("/error.jsp").forward(request, response);
            }
        }
    }
}
