package controladores;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import dtos.ProductoDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import servicios.ProductoServicio;

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
            String categoria = request.getParameter("categoria"); // Obtener el parámetro de categoría
            String minPriceParam = request.getParameter("minPrice"); // Obtener el parámetro de precio mínimo
            String maxPriceParam = request.getParameter("maxPrice"); // Obtener el parámetro de precio máximo

            ProductoServicio productoServicio = new ProductoServicio();
            List<ProductoDto> productos = productoServicio.obtenerProductos();
            List<ProductoDto> productosFiltrados = new ArrayList<>();

            // Filtrar los productos por categoría si se especificó una
            if (categoria != null && !categoria.isEmpty()) {
                for (ProductoDto producto : productos) {
                    if (categoria.equals(producto.getCategoria())) {
                        productosFiltrados.add(producto);
                    }
                }
                productos = productosFiltrados; // Reemplazar la lista original por la filtrada
            }

            // Filtrar los productos por precio si se especificaron valores de precio
            if (minPriceParam != null && !minPriceParam.isEmpty() && maxPriceParam != null && !maxPriceParam.isEmpty()) {
                double minPrice = Double.parseDouble(minPriceParam);
                double maxPrice = Double.parseDouble(maxPriceParam);

                List<ProductoDto> productosPorPrecio = new ArrayList<>();
                for (ProductoDto producto : productos) {
                    if (producto.getPrecio() >= minPrice && producto.getPrecio() <= maxPrice) {
                        productosPorPrecio.add(producto);
                    }
                }
                productos = productosPorPrecio; // Reemplazar la lista original por la filtrada por precio
            }

            // Verificar si hay productos disponibles después de los filtros
            if (productos != null && !productos.isEmpty()) {
                request.setAttribute("productos", productos);
                request.getRequestDispatcher("/productos.jsp").forward(request, response);
            } else {
                request.setAttribute("mensaje", "No hay productos disponibles.");
                request.getRequestDispatcher("/productos.jsp").forward(request, response);
            }
        }
    }
}
