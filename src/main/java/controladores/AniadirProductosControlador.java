package controladores;

import java.io.IOException;
import java.io.InputStream;

import dtos.ProductoDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import servicios.ProductoServicio;
import utilidades.Utilidades; 

/**
 * Servlet encargado de manejar la adición de nuevos productos al sistema.
 * Recibe los datos del formulario de productos y los envía al servicio correspondiente.
 */
@WebServlet("/productosAniadir")
@MultipartConfig
public class AniadirProductosControlador extends HttpServlet {

    private ProductoServicio productoServicio;

    /**
     * Inicializa el servlet y el servicio de productos.
     * @throws ServletException si ocurre un error durante la inicialización.
     */
    @Override
    public void init() throws ServletException {
        // Inicializar manualmente el servicio aquí
        productoServicio = new ProductoServicio(); // Suponiendo que el servicio tiene un constructor sin parámetros
    }

    /**
     * Maneja las solicitudes POST para agregar un nuevo producto.
     * Recoge los datos del formulario, los valida y los envía al servicio.
     * 
     * @param request  Objeto {@link HttpServletRequest} con la solicitud del cliente.
     * @param response Objeto {@link HttpServletResponse} para enviar la respuesta al cliente.
     * @throws ServletException si ocurre un error en la ejecución del servlet.
     * @throws IOException si ocurre un error en la lectura o escritura de datos.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        // Log de inicio del proceso
        Utilidades.escribirLog(session, "[INFO]", "AniadirProductosControlador", "doPost", "Inicio del proceso de agregar producto");

        try {
            // Obtener datos del formulario
            String nombre = request.getParameter("nombre");
            String descripcion = request.getParameter("descripcion");
            String precioStr = request.getParameter("precio");
            String stockStr = request.getParameter("stock");
            String categoria = request.getParameter("categoria"); // Obtener la categoría seleccionada

            // Validar parámetros obligatorios
            if (nombre == null || descripcion == null || precioStr == null || stockStr == null || categoria == null ||
                nombre.isEmpty() || descripcion.isEmpty() || precioStr.isEmpty() || stockStr.isEmpty() || categoria.isEmpty()) {
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

            // Crear objeto DTO incluyendo la categoría
            ProductoDto producto = new ProductoDto(null, nombre, descripcion, precio, imagenBytes, stock, categoria);

            // Llamar al servicio para agregar el producto
            boolean exito = productoServicio.agregarProducto(producto);

            // Redirigir con parámetro para mostrar el toast y registrar log correspondiente
            if (exito) {
                Utilidades.escribirLog(session, "[INFO]", "AniadirProductosControlador", "doPost", "Producto agregado exitosamente.");
                response.sendRedirect("admin?productoAgregado=true");
            } else {
                Utilidades.escribirLog(session, "[INFO]", "AniadirProductosControlador", "doPost", "Fallo al agregar producto (resultado falso).");
                response.sendRedirect("admin?productoAgregado=false");
            }
        } catch (NumberFormatException e) {
            Utilidades.escribirLog(session, "[ERROR]", "AniadirProductosControlador", "doPost", "Error al convertir número: " + e.getMessage());
            e.printStackTrace();
            response.sendRedirect("admin?productoAgregado=false");
        } catch (Exception e) {
            Utilidades.escribirLog(session, "[ERROR]", "AniadirProductosControlador", "doPost", "Error general: " + e.getMessage());
            e.printStackTrace();
            response.sendRedirect("admin?productoAgregado=false");
        }
    }
}
