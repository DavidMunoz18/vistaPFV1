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
import utilidades.Utilidades;
 
/**
 * Controlador que maneja la visualización de productos en el ecommerce.
 * <p>
 * Este servlet procesa las solicitudes GET para mostrar los productos.
 * Puede mostrar una lista de productos o el detalle de un producto específico
 * dependiendo de los parámetros pasados en la solicitud.
 * </p>
 */
@WebServlet("/productos")
public class ProductoControlador extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * Método que maneja la solicitud GET para mostrar productos.
     * Dependiendo de los parámetros en la solicitud, puede mostrar:
     * <ul>
     * <li>Un producto específico si se pasa un ID de producto.</li>
     * <li>Una lista de productos filtrados por categoría o precio.</li>
     * </ul>
     * 
     * @param request La solicitud HTTP recibida.
     * @param response La respuesta HTTP que será enviada al cliente.
     * @throws ServletException Si ocurre un error durante el procesamiento de la solicitud.
     * @throws IOException Si ocurre un error de entrada/salida durante el procesamiento.
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Log: Inicio del procesamiento de la solicitud GET
        Utilidades.escribirLog(request.getSession(), "[INFO]", "ProductoControlador", "doGet", "Inicio del procesamiento de la solicitud GET");

        String idProducto = request.getParameter("id");

        if (idProducto != null && !idProducto.isEmpty()) {
            // Log: Se solicita el detalle del producto con el ID especificado
            Utilidades.escribirLog(request.getSession(), "[INFO]", "ProductoControlador", "doGet", "Procesando detalle de producto para ID: " + idProducto);

            // Si se pasa un ID de producto, obtener el detalle del producto
            ProductoServicio productoServicio = new ProductoServicio();
            ProductoDto producto = productoServicio.obtenerProductoPorId(Integer.parseInt(idProducto));

            if (producto != null) {
                // Log: Producto encontrado
                Utilidades.escribirLog(request.getSession(), "[INFO]", "ProductoControlador", "doGet", "Producto encontrado: " + producto.getNombre());
                request.setAttribute("producto", producto);
                request.getRequestDispatcher("/detallesProducto.jsp").forward(request, response);
            } else {
                // Log: Producto no encontrado
                Utilidades.escribirLog(request.getSession(), "[ERROR]", "ProductoControlador", "doGet", "Producto no encontrado para ID: " + idProducto);
                request.setAttribute("mensaje", "Producto no encontrado.");
                request.getRequestDispatcher("/error.jsp").forward(request, response);
            }
        } else {
            // Log: Se solicita la lista de productos sin filtro de ID
            Utilidades.escribirLog(request.getSession(), "[INFO]", "ProductoControlador", "doGet", "Mostrando lista de productos");

            // Si no se pasa ID, mostrar la lista de productos
            String categoria = request.getParameter("categoria"); // Obtener el parámetro de categoría
            String minPriceParam = request.getParameter("minPrice"); // Obtener el parámetro de precio mínimo
            String maxPriceParam = request.getParameter("maxPrice"); // Obtener el parámetro de precio máximo

            ProductoServicio productoServicio = new ProductoServicio();
            List<ProductoDto> productos = productoServicio.obtenerProductos();
            List<ProductoDto> productosFiltrados = new ArrayList<>();

            // Filtrar los productos por categoría si se especificó una
            if (categoria != null && !categoria.isEmpty()) {
                Utilidades.escribirLog(request.getSession(), "[INFO]", "ProductoControlador", "doGet", "Filtrando productos por categoría: " + categoria);
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

                Utilidades.escribirLog(request.getSession(), "[INFO]", "ProductoControlador", "doGet", "Filtrando productos por precio: min=" + minPrice + ", max=" + maxPrice);

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
                Utilidades.escribirLog(request.getSession(), "[INFO]", "ProductoControlador", "doGet", "Cantidad de productos encontrados: " + productos.size());
                request.setAttribute("productos", productos);
                request.getRequestDispatcher("/productos.jsp").forward(request, response);
            } else {
                Utilidades.escribirLog(request.getSession(), "[ERROR]", "ProductoControlador", "doGet", "No hay productos disponibles.");
                request.setAttribute("mensaje", "No hay productos disponibles.");
                request.getRequestDispatcher("/productos.jsp").forward(request, response);
            }
        }
    }
}
