package controladores;

import java.io.IOException;
import java.util.List;

import dtos.ProductoDto;
import dtos.ReseniaDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import servicios.ProductoServicio;
import servicios.ResenaServicio;

/**
 * Controlador para gestionar las reseñas de productos.
 * <p>
 * Este servlet maneja las solicitudes para visualizar y agregar reseñas a los productos.
 * </p>
 */
@WebServlet("/resenas")
public class ResenaControlador extends HttpServlet {

    private ProductoServicio productoServicio;
    private ResenaServicio resenaServicio;

    /**
     * Inicializa los servicios necesarios para la gestión de productos y reseñas.
     * <p>
     * Este método se ejecuta una vez cuando se inicia el servlet y prepara los servicios para manejar las solicitudes.
     * </p>
     * 
     * @throws ServletException Si ocurre un error durante la inicialización del servlet.
     */
    @Override
    public void init() throws ServletException {
        this.productoServicio = new ProductoServicio();
        this.resenaServicio = new ResenaServicio();
    }

    /**
     * Maneja las solicitudes GET para visualizar las reseñas de un producto.
     * <p>
     * Este método obtiene el producto y sus reseñas asociadas. Si el usuario no está logueado, se redirige al login.
     * </p>
     * 
     * @param request La solicitud HTTP recibida.
     * @param response La respuesta HTTP que será enviada al cliente.
     * @throws ServletException Si ocurre un error durante el procesamiento de la solicitud.
     * @throws IOException Si ocurre un error de entrada/salida durante el procesamiento.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Comprobar si el usuario está logueado
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("idUsuario") == null) {
            // Si no está logueado, redirigir al login
            response.sendRedirect("login.jsp");
            return;
        }

        // Obtener idProducto del parámetro
        String idProducto = request.getParameter("id");
        ProductoDto producto = null;
        List<ReseniaDto> resenias = null;

        if (idProducto != null && !idProducto.isEmpty()) {
            // Obtener producto
            producto = productoServicio.obtenerProductoPorId(Integer.parseInt(idProducto));
            if (producto != null) {
                // Obtener reseñas del producto
                resenias = resenaServicio.obtenerReseniasPorProducto(producto.getId());
            }
        }

        // Establecer atributos en la solicitud para la vista JSP
        request.setAttribute("producto", producto);
        request.setAttribute("resenias", resenias);

        // Redirigir a la vista JSP
        request.getRequestDispatcher("/vistas.jsp").forward(request, response);
    }

    /**
     * Maneja las solicitudes POST para agregar una nueva reseña a un producto.
     * <p>
     * Este método recibe los datos de la reseña (contenido y calificación) y los asocia con el producto seleccionado.
     * Si el usuario no está logueado, se redirige al login.
     * </p>
     * 
     * @param request La solicitud HTTP recibida.
     * @param response La respuesta HTTP que será enviada al cliente.
     * @throws ServletException Si ocurre un error durante el procesamiento de la solicitud.
     * @throws IOException Si ocurre un error de entrada/salida durante el procesamiento.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Comprobar si el usuario está logueado
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("idUsuario") == null) {
            // Si no está logueado, redirigir al login
            response.sendRedirect("login.jsp");
            return;
        }

        String idProducto = request.getParameter("idProducto");
        String contenidoResena = request.getParameter("contenidoResena");
        String calificacion = request.getParameter("calificacion");

        if (idProducto != null && !idProducto.isEmpty() && contenidoResena != null && !contenidoResena.isEmpty() && calificacion != null) {
            // Crear objeto ReseniaDto
            ReseniaDto reseniaDto = new ReseniaDto();
            reseniaDto.setContenidoResena(contenidoResena);
            reseniaDto.setCalificacion(Integer.parseInt(calificacion));

            // Obtener idUsuario desde la sesión
            Long idUsuario = (Long) session.getAttribute("idUsuario");

            // Obtener producto y agregar reseña
            ProductoDto productoDto = productoServicio.obtenerProductoPorId(Integer.parseInt(idProducto));
            if (productoDto != null) {
                boolean exito = resenaServicio.agregarResenia(reseniaDto, productoDto, idUsuario);
                if (exito) {
                    // Redirigir para mostrar la página con la nueva reseña
                    response.sendRedirect("vistas.jsp?id=" + idProducto);
                } else {
                    request.setAttribute("error", "Hubo un problema al agregar la reseña.");
                    request.getRequestDispatcher("/vistas.jsp").forward(request, response);
                }
            }
        } else {
            request.setAttribute("error", "Todos los campos son obligatorios.");
            request.getRequestDispatcher("/vistas.jsp").forward(request, response);
        }
    }
}
