package controladores;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import servicios.ProductoServicio;
import utilidades.Utilidades;

import java.io.IOException;

/**
 * Controlador para manejar la modificación de productos con datos multipart.
 * <p>
 * Este servlet procesa solicitudes HTTP POST para modificar los datos de un producto,
 * incluyendo su nombre, descripción, precio, stock y una imagen. Los datos se reciben
 * en formato multipart para permitir la carga de archivos.
 * </p>
 */
@WebServlet("/modificarProducto")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10,      // 10MB
        maxRequestSize = 1024 * 1024 * 50   // 50MB
)
public class ModificarProductoControlador extends HttpServlet {

    private ProductoServicio productoServicio;

    /**
     * Inicializa el servicio de productos.
     * Este método se llama cuando el servlet se inicializa, y configura
     * el servicio para modificar productos.
     */
    @Override
    public void init() throws ServletException {
        productoServicio = new ProductoServicio();
    }

    /**
     * Maneja las solicitudes HTTP POST para modificar un producto.
     * Recibe los datos del formulario en formato multipart, valida y procesa
     * la información y luego llama al servicio correspondiente para realizar
     * la modificación en la base de datos.
     * 
     * @param request La solicitud HTTP recibida.
     * @param response La respuesta HTTP que será enviada al cliente.
     * @throws ServletException Si ocurre un error durante el procesamiento de la solicitud.
     * @throws IOException Si ocurre un error de entrada/salida durante el procesamiento.
     */
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

            // Convertir los parámetros a tipos adecuados
            Double nuevoPrecio = nuevoPrecioStr != null && !nuevoPrecioStr.isEmpty() ? Double.parseDouble(nuevoPrecioStr) : null;
            Integer nuevoStock = nuevoStockStr != null && !nuevoStockStr.isEmpty() ? Integer.parseInt(nuevoStockStr) : null;

            byte[] nuevaImagenBytes = null;
            if (nuevaImagenPart != null && nuevaImagenPart.getSize() > 0) {
                nuevaImagenBytes = nuevaImagenPart.getInputStream().readAllBytes();
            }

            // Llamar al servicio para realizar la modificación
            boolean resultado = productoServicio.modificarProducto(idProducto, nuevoNombre, nuevaDescripcion, nuevoPrecio, nuevoStock, nuevaImagenBytes);

            // Log de la modificación
            Utilidades.escribirLog(request.getSession(), "[INFO]", "ModificarProductoControlador", "doPost", 
                    "Producto con ID " + idProducto + " modificado con éxito.");

            // Redirigir dependiendo del resultado
            if (resultado) {
            	response.sendRedirect("admin?productoModificado=true");
            } else {
            	response.sendRedirect("admin?productoModificado=false");
                request.setAttribute("error", "No se encontró el producto o no se pudo modificar.");
                Utilidades.escribirLog(request.getSession(), "[ERROR]", "ModificarProductoControlador", "doPost", 
                        "No se pudo modificar el producto con ID " + idProducto);
                request.getRequestDispatcher("error.jsp").forward(request, response);
            }
        } catch (Exception e) {
            // Log del error
            Utilidades.escribirLog(request.getSession(), "[ERROR]", "ModificarProductoControlador", "doPost", 
                    "Error al intentar modificar el producto: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Ocurrió un error al intentar modificar el producto.");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }
}
